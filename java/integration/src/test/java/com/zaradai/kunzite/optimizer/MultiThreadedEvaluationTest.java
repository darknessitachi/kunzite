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

import com.zaradai.kunzite.optimizer.config.OptimizerConfigurationImpl;
import com.zaradai.kunzite.optimizer.control.OptimizeRequest;
import com.zaradai.kunzite.optimizer.evaluators.DualMaximaEqEvaluator;
import com.zaradai.kunzite.optimizer.model.InputRowGenerator;
import com.zaradai.kunzite.optimizer.tactic.FloodFillTactic;
import com.zaradai.kunzite.optimizer.tactic.HillClimberTactic;
import com.zaradai.kunzite.optimizer.tactic.OptimizerResult;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MultiThreadedEvaluationTest extends BaseOptimizerTest {
    @Override
    protected OptimizerModule getOptimizerModule() {
        return new OptimizerModule(CacheStrategy.None, DataStrategy.None, EvaluationStrategy.LocalMultiThreaded);
    }

    @Test
    public void shouldRunFloodFill() throws Exception {
        // prepare the request
        OptimizeRequest request = OptimizeRequest.newRequest(FloodFillTactic.class, DualMaximaEqEvaluator.OUTPUT_Z,
                true, InputRowGenerator.getRandom(getSchema()));
        // configure the tactic
        getSource().set(OptimizerConfigurationImpl.FLOOD_BATCH_SIZE, 4000);
        // submit and wait for it to finish
        OptimizerResult res = getController().optimize(request).get();

        assertThat(res, not(nullValue()));
    }

    @Test
    public void shouldRunHillClimber() throws Exception {
        // prepare the request
        OptimizeRequest request = OptimizeRequest.newRequest(HillClimberTactic.class, DualMaximaEqEvaluator.OUTPUT_Z,
                true, InputRowGenerator.getRandom(getSchema()));
        // submit and wait for it to finish
        OptimizerResult res = getController().optimize(request).get();

        assertThat(res, not(nullValue()));
    }
}
