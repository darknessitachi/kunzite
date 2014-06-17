/**
 * Copyright 2014 Zaradai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zaradai.kunzite.optimizer.tactic;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.optimizer.data.DataRequestManager;
import com.zaradai.kunzite.optimizer.data.DataRequester;
import com.zaradai.kunzite.optimizer.data.ResultListener;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class AbstractTactic implements OptimizerTactic, ResultListener {
    private InputRowSchema schema;
    private final DataRequester dataRequester;
    private OptimizerResult optimizerResult;
    private InputRow start;
    private Class<? extends Evaluator> evaluator;

    private int toProcess;
    private int processed;
    private int generations;
    private final List<Row> processedBuffer;
    private CountDownLatch barrier;

    protected AbstractTactic(DataRequestManager dataRequestManager) {
        this.dataRequester = dataRequestManager.getRequester();
        this.dataRequester.addListener(this);
        processedBuffer = createProcessedList();
    }

    private ArrayList<Row> createProcessedList() {
        return Lists.newArrayList();
    }

    @Override
    public OptimizerResult optimize(boolean wantMaxima, InputRowSchema inputRowSchema,
                                    Class<? extends Evaluator> eval, InputRow startingRow, String target)
            throws InterruptedException {
        schema = inputRowSchema;
        evaluator = eval;
        optimizerResult = OptimizerResult.newInstance(target, wantMaxima);
        start = startingRow;
        // setup the counters
        processed = 0;
        generations = 0;
        processedBuffer.clear();
        // setup the barrier
        barrier = createBarrier();
        // ask implementers to setup
        initialize();
        // do the prepare phase
        sendRequest(prepare());
        // now await for completion
        barrier.await();

        return optimizerResult;
    }

    private CountDownLatch createBarrier() {
        return new CountDownLatch(1);
    }

    protected abstract void initialize();
    protected abstract List<InputRow> prepare();
    protected abstract boolean process(List<Row> results);

    protected OptimizerResult getResult() {
        return optimizerResult;
    }

    protected InputRow getStart() {
        return start;
    }

    protected DataRequester getRequester() {
        return this.dataRequester;
    }

    protected InputRowSchema getSchema() {
        return schema;
    }

    protected Class<? extends Evaluator> getEvaluator() {
        return evaluator;
    }

    protected String getTarget() {
        return optimizerResult.getTarget();
    }

    protected boolean testValue(Row row) {
        return getResult().testValue(row);
    }

    protected Row getOptimizedRow() {
        return optimizerResult.getOptimizedRow();
    }

    @Override
    public void onResult(Row row) {
        processed++;
        processedBuffer.add(row);

        if (processedBuffer.size() == toProcess) {
            // we have our batch
            boolean done = process(processedBuffer);
            // clear our buffer
            processedBuffer.clear();
            // check for complete
            if (done) {
                optimizerResult.setOptimized();
                optimizerResult.setCalculations(processed);
                optimizerResult.setGenerations(generations);
                // signal finished
                barrier.countDown();
            } else {
                // continue with next generation
                generations++;
                sendRequest(prepare());
            }
        }
    }

    private void sendRequest(List<InputRow> requests) {
        toProcess = requests.size();

        if (toProcess == 0) {
            // were done
            barrier.countDown();
        } else {
            // send the request
            for (InputRow request : requests) {
                dataRequester.request(request, evaluator);
            }
        }
    }
}
