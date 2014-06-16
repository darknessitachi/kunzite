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
package com.zaradai.kunzite.optimizer.control;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.optimizer.data.DataManager;
import com.zaradai.kunzite.optimizer.data.DataManagerFactory;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.matrix.ResultMatrix;
import com.zaradai.kunzite.optimizer.eval.CalcEngine;
import com.zaradai.kunzite.optimizer.eval.CalcEngineFactory;
import com.zaradai.kunzite.optimizer.tactic.OptimizerResult;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTactic;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTacticFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DefaultOptimizerController extends AbstractIdleService implements OptimizeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOptimizerController.class);

    private final DataManager dataManager;
    private final CalcEngine calcEngine;
    private final OptimizerTacticFactory optimizerTacticFactory;
    private final ListeningExecutorService executorService;
    private final Slf4jReporter reporter;
    private final DataSet dataSet;

    @Inject
    DefaultOptimizerController(
            DataManagerFactory dataManagerFactory,
            CalcEngineFactory calcEngineFactory,
            OptimizerTacticFactory optimizerTacticFactory,
            MetricRegistry metricRegistry,
            @Assisted DataSet dataSet) {
        this.optimizerTacticFactory = optimizerTacticFactory;
        this.dataSet = dataSet;
        reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger("com.zaradai.kunzite.optimizer"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        // build the executor service
        this.executorService = createExecutorService();
        // build the calc engine
        this.calcEngine = calcEngineFactory.create();
        // build the data manager
        this.dataManager = dataManagerFactory.create(calcEngine, dataSet);
    }

    protected ListeningExecutorService createExecutorService() {
        return MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
    }

    @Override
    protected void startUp() throws Exception {
        // start the metric reporter
        reporter.start(1, TimeUnit.MINUTES);
        // Start the calc engine and wait for it to start
        calcEngine.startAsync().awaitRunning();
        // log it
        LOGGER.info("Engine running");
    }

    @Override
    protected void shutDown() throws Exception {
        // shutdown the executor
        executorService.shutdown();
        // Stop the calc engine and wait for it to terminate
        calcEngine.stopAsync().awaitTerminated();
        // close the DataSet flushing any outstanding edits
        dataSet.close();
        // do final report
        reporter.stop();
        reporter.report();
        // log it
        LOGGER.info("Engine stopped");
    }

    @Override
    public DataSetContext getContext() {
        return dataSet.getContext();
    }

    @Override
    public DataSet getDataSet() {
        return dataSet;
    }

    @Override
    public ListenableFuture<OptimizerResult> optimize(final OptimizeRequest request) {
        if (isRunning()) {
            return executorService.submit(new Callable<OptimizerResult>() {
                @Override
                public OptimizerResult call() throws Exception {
                    OptimizerTactic tactic = optimizerTacticFactory.create(request.getTactic());
                    OptimizerResult res = tactic.optimize(request.isLookForMaxima(), getContext().getInputSchema(),
                            getContext().getEvaluator(), request.getStart(), request.getTarget());
                    logResults(tactic.getName(), res);
                    return res;
                }
            });
        }

        return null;
    }

    private void logResults(String optimizerName, OptimizerResult result) {
        LOGGER.info("Result: [{}]: {}", optimizerName, result);
    }

    @Override
    public ResultMatrix requestMatrix(String x, String y, String target) {
        return dataSet.getMatrixManager().get(x, y, target);
    }
}
