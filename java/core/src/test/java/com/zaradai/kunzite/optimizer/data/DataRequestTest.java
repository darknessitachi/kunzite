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

import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DataRequestTest {
    private static final UUID TEST_ID = UUID.randomUUID();
    private static final InputRow TEST_REQUEST = InputRow.fromSchema(InputRowSchema.newBuilder().build());
    private static final Class<Evaluator> TEST_EVALUATOR = Evaluator.class;

    @Test
    public void shouldGetRequester() throws Exception {
        DataRequest uut = DataRequest.newRequest();
        uut.setRequester(TEST_ID);

        assertThat(uut.getRequester(), is(TEST_ID));
    }

    @Test
    public void shouldGetRequest() throws Exception {
        DataRequest uut = DataRequest.newRequest();
        uut.setRequest(TEST_REQUEST);

        assertThat(uut.getRequest(), is(TEST_REQUEST));
    }

    @Test
    public void shouldGetEvaluator() throws Exception {
        DataRequest uut = DataRequest.newRequest();
        uut.setEvaluator(TEST_EVALUATOR);

        assertThat(uut.getEvaluator().equals(TEST_EVALUATOR), is(true));
    }
}
