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
import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.config.Configuration;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ShotgunHillClimber implements OptimizerTactic {
    public static final String OPTIMIZER_NAME = "Shotgun";

    private final OptimizerTacticFactory factory;
    private final int numShotgunClimbers;
    private final ExecutorService executorService;
    private OptimizerResult result;
    private ServiceManager serviceManager;

    @Inject
    ShotgunHillClimber(OptimizerTacticFactory factory, Configuration configuration) {
        this.factory = factory;
        this.numShotgunClimbers = configuration.getNumShotgunClimbers();
        executorService = createExecutorService();
    }

    private ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public String getName() {
        return OPTIMIZER_NAME;
    }

    @Override
    public OptimizerResult optimize(final boolean wantMaxima, final InputRowSchema schema,
                                    final Class<? extends Evaluator> evaluator, final InputRow start,
                                    final String target) throws Exception {
        List<Future<OptimizerResult>> resultFutures = Lists.newArrayList();

        for (int i = 0; i < numShotgunClimbers; ++i) {
            final OptimizerTactic tactic = factory.create(HillClimberTactic.class);
            resultFutures.add(executorService.submit(new Callable<OptimizerResult>() {
                @Override
                public OptimizerResult call() throws Exception {
                    return tactic.optimize(wantMaxima, schema, evaluator, start, target);
                }
            }));
        }

        return processOptimizations(resultFutures, wantMaxima, target);
    }

    private OptimizerResult processOptimizations(List<Future<OptimizerResult>> resultFutures, boolean wantMaxima,
                                                 String target) throws Exception {
        OptimizerResult aggregateResult = OptimizerResult.newInstance(target, wantMaxima);
        // Todo: Use a completion service to process newest results
        for (Future<OptimizerResult> resultFuture : resultFutures) {
            OptimizerResult res = resultFuture.get();
            // update if better
            aggregateResult.testValue(res.getOptimizedRow());
            aggregateResult.setGenerations(result.getGenerations() + res.getGenerations());
            aggregateResult.setCalculations(result.getCalculations() + res.getCalculations());
        }

        return aggregateResult;
    }
}
