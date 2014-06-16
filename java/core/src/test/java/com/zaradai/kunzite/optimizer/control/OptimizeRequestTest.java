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

import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTactic;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OptimizeRequestTest {
    private static final Class<? extends OptimizerTactic> TEST_TACTIC = OptimizerTactic.class;
    private static final String TEST_TARGET = "test";
    private static final boolean TEST_LOOK_FOR = true;
    private static final InputRow TEST_START = InputRow.fromSchema(InputRowSchema.newBuilder().build());

    @Test
    public void shouldCreateNewRequest() throws Exception {
        OptimizeRequest uut = OptimizeRequest.newRequest(TEST_TACTIC, TEST_TARGET, TEST_LOOK_FOR, TEST_START);

        assertThat(uut.getStart(), is(TEST_START));
        assertThat(uut.getTactic().equals(TEST_TACTIC), is(true));
        assertThat(uut.getTarget(), is(TEST_TARGET));
        assertThat(uut.isLookForMaxima(), is(TEST_LOOK_FOR));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailForInvalidTactic() throws Exception {
        OptimizeRequest uut = OptimizeRequest.newRequest(null, TEST_TARGET, TEST_LOOK_FOR, TEST_START);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForInvalidTarget() throws Exception {
        OptimizeRequest uut = OptimizeRequest.newRequest(TEST_TACTIC, "", TEST_LOOK_FOR, TEST_START);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailForInvalidStart() throws Exception {
        OptimizeRequest uut = OptimizeRequest.newRequest(TEST_TACTIC, TEST_TARGET, TEST_LOOK_FOR, null);
    }
}
