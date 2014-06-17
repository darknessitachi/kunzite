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

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.config.OptimizerConfiguration;
import com.zaradai.kunzite.optimizer.control.OptimizeController;
import com.zaradai.kunzite.optimizer.control.OptimizeControllerFactory;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetFactory;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDatabase;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Optimizer  extends AbstractIdleService implements OptimizerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Optimizer.class);

    private final OptimizerConfiguration configuration;
    private final OptimizeControllerFactory controllerFactory;
    private final DataSetDatabase dataSetDatabase;
    private final DataSetFactory dataSetFactory;

    @Inject
    Optimizer(
            OptimizerConfiguration configuration,
            OptimizeControllerFactory controllerFactory,
            DataSetDatabase dataSetDatabase,
            DataSetFactory dataSetFactory) {
        this.configuration = configuration;
        this.controllerFactory = controllerFactory;
        this.dataSetDatabase = dataSetDatabase;
        this.dataSetFactory = dataSetFactory;
    }

    @Override
    protected void startUp() throws Exception {
        // connect to the database
        dataSetDatabase.connect();
        LOGGER.info("Optimizer Service started");
    }

    @Override
    protected void shutDown() throws Exception {
        dataSetDatabase.close();
        LOGGER.info("Optimizer Service stopped");
    }

    @Override
    public OptimizeController open(DataSetContext context) {
        DataSetDriver driver = dataSetDatabase.open(context);
        DataSet dataSet = dataSetFactory.create(driver, context);
        // Create and return a controller for this dataset
        OptimizeController res = controllerFactory.create(dataSet);
        // start the controller and wait for it to be initialized
        res.startAsync().awaitRunning();

        return res;
    }

    @Override
    public OptimizeController create(String name, String description, Class<? extends Evaluator> evaluator,
                                     InputRowSchema schema) {
        return open(DataSetContext.builder()
                .name(name)
                .description(description)
                .evaluator(evaluator)
                .input(schema)
                .build());
    }
}
