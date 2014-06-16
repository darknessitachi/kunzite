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
package com.zaradai.kunzite.optimizer.data;

import com.google.inject.Inject;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zaradai.kunzite.optimizer.config.Configuration;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorDataRequester implements DataRequester, EventHandler<Row>, EventTranslatorOneArg<Row, Row> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorDataRequester.class);

    private final DataRequestManager dataRequestManager;
    private final UUID id;
    private final ExecutorService executorService;
    private final Disruptor<Row> disruptor;

    private static final EventFactory<Row> FACTORY = new EventFactory<Row>() {
        @Override
        public Row newInstance() {
            return Row.newInstance();
        }
    };
    private ResultListener listener;

    @Inject
    DisruptorDataRequester(DataRequestManager dataRequestManager, Configuration configuration) {
        this.dataRequestManager = dataRequestManager;
        id = UUID.randomUUID();
        executorService = createExecutorService();
        disruptor = new Disruptor<Row>(FACTORY, configuration.getResultRingSize(), executorService,
                ProducerType.MULTI, new BusySpinWaitStrategy());
        disruptor.handleEventsWith(this);
        disruptor.start();
    }

    public void shutdown() {
        disruptor.shutdown();
        executorService.shutdown();
    }

    protected ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void request(InputRow row, Class<? extends Evaluator> evaluator) {
        dataRequestManager.addRequest(this, row, evaluator);
    }

    @Override
    public void addResult(Row row) {
        disruptor.publishEvent(this, row);
    }

    @Override
    public void addListener(ResultListener resultListener) {
        listener = resultListener;
    }

    @Override
    public void onEvent(Row event, long sequence, boolean endOfBatch) throws Exception {
        if (listener != null) {
            listener.onResult(event);
        }
    }

    @Override
    public void translateTo(Row event, long sequence, Row arg0) {
        event.setInput(arg0.getInput());
        event.setOutput(arg0.getOutput());
    }
}
