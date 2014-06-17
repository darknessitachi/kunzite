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
package com.zaradai.kunzite.optimizer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zaradai.kunzite.config.ConfigurationSource;
import com.zaradai.kunzite.optimizer.config.OptimizerConfigurationImpl;
import com.zaradai.kunzite.optimizer.control.OptimizeController;
import com.zaradai.kunzite.optimizer.control.OptimizeRequest;
import com.zaradai.kunzite.optimizer.evaluators.DualMaximaEqEvaluator;
import com.zaradai.kunzite.optimizer.model.InputRowGenerator;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.tactic.FloodFillTactic;
import com.zaradai.kunzite.optimizer.tactic.OptimizerResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MultiThreadedEvaluationTest {
    private InputRowSchema schema;
    private Injector injector;
    private OptimizerService optimizerService;
    private ConfigurationSource source;
    private OptimizeController controller;

    @Before
    public void setUp() throws Exception {
        schema = InputRowSchema.newBuilder()
                .withName(DualMaximaEqEvaluator.INPUT_X).from(-100).step(0.1).withSteps(2000)
                .withName(DualMaximaEqEvaluator.INPUT_Y).from(-100).step(0.1).withSteps(2000)
                .build();
        injector = Guice.createInjector(new OptimizerModule(CacheStrategy.None, DataStrategy.None,
                EvaluationStrategy.LocalMultiThreaded));
        // get the config source
        source = injector.getInstance(ConfigurationSource.class);
        // get the optimizer service
        optimizerService = injector.getInstance(OptimizerService.class);
        // start and wait to be initialized
        optimizerService.startAsync().awaitRunning();
        // create a controller for this run with a new database
        controller = optimizerService.create("test", "A test database", DualMaximaEqEvaluator.class, schema);
    }

    @After
    public void tearDown() throws Exception {
        // stop the controller
        controller.stopAsync().awaitTerminated();
        // stop the service
        optimizerService.stopAsync().awaitTerminated();
    }

    @Test
    public void shouldRunFloodFill() throws Exception {
        // prepare the request
        OptimizeRequest request = OptimizeRequest.newRequest(FloodFillTactic.class, DualMaximaEqEvaluator.OUTPUT_Z,
                true, InputRowGenerator.getRandom(schema));
        // configure the tactic
        source.set(OptimizerConfigurationImpl.FLOOD_BATCH_SIZE, 4000);
        // submit and wait for it to finish
        OptimizerResult res = controller.optimize(request).get();

        assertThat(res, not(nullValue()));
    }
}
