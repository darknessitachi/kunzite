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

import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.data.DataRequest;
import com.zaradai.kunzite.optimizer.eval.EvaluatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCalcEngine extends AbstractCalcEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCalcEngine.class);

    @Inject
    SimpleCalcEngine(EvaluatorFactory evaluatorFactory) {
        super(evaluatorFactory);
    }

    @Override
    public void calculate(DataRequest request) {
        doCalc(request);
    }

    @Override
    protected void startUp() throws Exception {
        LOGGER.info("Calc Engine running");
    }

    @Override
    protected void shutDown() throws Exception {
        LOGGER.info("Calc Engine stopped");
    }
}
