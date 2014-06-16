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
package com.zaradai.kunzite.optimizer.eval.local;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.config.OptimizerConfiguration;
import com.zaradai.kunzite.optimizer.data.DataRequest;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.eval.EvaluatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedCalcEngine extends AbstractCalcEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiThreadedCalcEngine.class);
    private static final int SHUTDOWN_TIMEOUT = 10;

    private final ExecutorService executorService;
    private final int numThreads;

    @Inject
    MultiThreadedCalcEngine(EvaluatorFactory evaluatorFactory, OptimizerConfiguration configuration) {
        super(evaluatorFactory);
        this.numThreads = configuration.getEvaluatorThreadSize();
        executorService = createExecutor();
    }

    protected Map<Class<? extends Evaluator>, Evaluator> createEvaluatorCache() {
        return Maps.newConcurrentMap();
    }

    protected ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(numThreads);
    }

    @Override
    protected void startUp() throws Exception {
        LOGGER.info("Calc Engine started with {} execution threads", numThreads);
    }

    @Override
    protected void shutDown() throws Exception {
        executorService.shutdown();
        if (!executorService.awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.SECONDS)) {
            LOGGER.debug("Unable to shutdown calc threads, forcing shutdown");
            executorService.shutdownNow();
        }

        LOGGER.info("Calc Engine stopped");
    }

    @Override
    public void calculate(final DataRequest request) {
        // schedule calc on next available thread
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                doCalc(request);
            }
        });
    }
}
