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

import com.zaradai.kunzite.optimizer.data.DataManager;
import com.zaradai.kunzite.optimizer.data.DataRequest;
import com.zaradai.kunzite.optimizer.data.DataResult;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.eval.EvaluatorFactory;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.model.Row;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SimpleCalcEngineTest {
    private EvaluatorFactory evaluatorFactory;
    private SimpleCalcEngine uut;
    private DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        evaluatorFactory = mock(EvaluatorFactory.class);
        dataManager = mock(DataManager.class);

        uut = new SimpleCalcEngine(evaluatorFactory);
        uut.setDataManager(dataManager);
    }

    @Test
    public void shouldCalculate() throws Exception {
        UUID requesterId = UUID.randomUUID();
        InputRow inputRow = InputRow.fromSchema(InputRowSchema.newBuilder().build());
        Row row = Row.newInstance();
        Evaluator evaluator = mock(Evaluator.class);
        Class<Evaluator> evaluatorClass = Evaluator.class;
        when(evaluatorFactory.create(evaluatorClass)).thenReturn(evaluator);
        when(evaluator.evaluate(inputRow)).thenReturn(row);

        uut.calculate(DataRequest.newRequest(requesterId, inputRow, evaluatorClass));

        ArgumentCaptor<DataResult> captor = ArgumentCaptor.forClass(DataResult.class);

        verify(dataManager).onDataResult(captor.capture());

        assertThat(captor.getValue().getRequestId(), is(requesterId));
        assertThat(captor.getValue().getRow(), is(row));
    }

    @Test
    public void shouldStartUp() throws Exception {

    }

    @Test
    public void shouldShutDown() throws Exception {

    }
}
