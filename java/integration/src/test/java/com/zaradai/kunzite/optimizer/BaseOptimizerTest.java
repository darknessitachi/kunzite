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
import com.zaradai.kunzite.optimizer.control.OptimizeController;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.evaluators.DualMaximaEqEvaluator;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import org.junit.After;
import org.junit.Before;

public class BaseOptimizerTest {
    private InputRowSchema schema;
    private Injector injector;
    private OptimizerService optimizerService;
    private ConfigurationSource source;
    private OptimizeController controller;
    @Before
    public void setUp() throws Exception {
        schema = buildSchema();
        injector = Guice.createInjector(getOptimizerModule());
        // get the config source
        source = injector.getInstance(ConfigurationSource.class);
        // get the optimizer service
        optimizerService = injector.getInstance(OptimizerService.class);
        // start and wait to be initialized
        optimizerService.startAsync().awaitRunning();
        // create a controller for this run with a new database
        controller = optimizerService.create("test", "A test database", getEvaluator(), getSchema());
    }

    protected OptimizerModule getOptimizerModule() {
        return new OptimizerModule(CacheStrategy.None, DataStrategy.None,
                EvaluationStrategy.LocalSingleThreaded);
    }

    protected InputRowSchema buildSchema() {
        return InputRowSchema.newBuilder()
                .withName(DualMaximaEqEvaluator.INPUT_X).from(-100).step(0.1).withSteps(2000)
                .withName(DualMaximaEqEvaluator.INPUT_Y).from(-100).step(0.1).withSteps(2000)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        // stop the controller
        controller.stopAsync().awaitTerminated();
        // stop the service
        optimizerService.stopAsync().awaitTerminated();
    }

    protected InputRowSchema getSchema() {
        return schema;
    }

    protected Class<? extends Evaluator> getEvaluator() {
        return DualMaximaEqEvaluator.class;
    }

    public Injector getInjector() {
        return injector;
    }

    public OptimizerService getOptimizerService() {
        return optimizerService;
    }

    public ConfigurationSource getSource() {
        return source;
    }

    public OptimizeController getController() {
        return controller;
    }
}
