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

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zaradai.kunzite.optimizer.config.OptimizerConfiguration;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.codahale.metrics.MetricRegistry.name;

public class DisruptorDataRequestManager implements DataRequestManager, EventHandler<DataRequest>,
        EventTranslatorOneArg<DataRequest, DataRequest> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisruptorDataRequestManager.class);
    public static final String METRIC_NAME_REQUESTS = "requests";

    private final Map<UUID, DataRequester> requesterById;
    private final DataRequesterFactory factory;
    private final ExecutorService executorService;
    private final Disruptor<DataRequest> disruptor;
    private final Meter requests;

    private static final EventFactory<DataRequest> FACTORY = new EventFactory<DataRequest>() {
        @Override
        public DataRequest newInstance() {
            return DataRequest.newRequest();
        }
    };
    private RequestListener listener;

    @Inject
    DisruptorDataRequestManager(MetricRegistry metrics, DataRequesterFactory factory, OptimizerConfiguration configuration) {
        this.factory = factory;
        requests = metrics.meter(name(DisruptorDataRequestManager.class, METRIC_NAME_REQUESTS));
        requesterById = createRequesterMap();
        executorService = createExecutorService();
        disruptor = new Disruptor<DataRequest>(FACTORY, configuration.getRequestRingSize(), executorService,
                ProducerType.MULTI, new BusySpinWaitStrategy());
        disruptor.handleEventsWith(this);
        disruptor.start();
    }

    protected ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }

    protected Map<UUID, DataRequester> createRequesterMap() {
        return Maps.newConcurrentMap();
    }

    @Override
    public DataRequester getRequester() {
        DataRequester res = factory.create();
        requesterById.put(res.getId(), res);

        return res;
    }

    @Override
    public void addRequest(DataRequester from, InputRow inputRow, Class<? extends Evaluator> evaluator) {
        DataRequest request = DataRequest.newRequest(from.getId(), inputRow, evaluator);
        // push it to the disruptor
        disruptor.publishEvent(this, request);
        //  update metric counter
        requests.mark();
    }

    @Override
    public void handleResult(DataResult dataResult) {
        Preconditions.checkNotNull(dataResult, "Result cannot be null");
        // get the requester
        DataRequester requester = requesterById.get(dataResult.getRequestId());

        if (requester != null) {
            requester.addResult(dataResult.getRow());
        } else {
            logStaleResult(dataResult.getRequestId());
        }
    }

    @Override
    public void addListener(RequestListener requestListener) {
        listener = requestListener;
    }

    private void logStaleResult(UUID requestId) {
        LOGGER.warn("Stale result");
    }

    @Override
    public void onEvent(DataRequest event, long sequence, boolean endOfBatch) throws Exception {
        if (listener != null) {
            listener.onRequest(event);
        }
    }

    @Override
    public void translateTo(DataRequest event, long sequence, DataRequest arg0) {
        event.setRequester(arg0.getRequester());
        event.setRequest(arg0.getRequest());
        event.setEvaluator(arg0.getEvaluator());
    }
}
