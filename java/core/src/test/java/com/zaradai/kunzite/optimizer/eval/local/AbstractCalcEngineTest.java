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
import static org.mockito.Mockito.*;


public class AbstractCalcEngineTest {
    private AbstractCalcEngine uut;
    private EvaluatorFactory evaluatorFactory;

    @Before
    public void setUp() throws Exception {
        evaluatorFactory = mock(EvaluatorFactory.class);

        uut = new AbstractCalcEngine(evaluatorFactory) {
            @Override
            protected void startUp() throws Exception {

            }

            @Override
            protected void shutDown() throws Exception {

            }

            @Override
            public void calculate(DataRequest request) {

            }
        };
    }

    @Test
    public void shouldSetDataManager() throws Exception {
        DataManager dataManager = mock(DataManager.class);

        uut.setDataManager(dataManager);

        assertThat(uut.getDataManager(), is(dataManager));
    }

    @Test
    public void shouldDoCalc() throws Exception {
        UUID requesterId = UUID.randomUUID();
        Class<? extends Evaluator> clazz = Evaluator.class;
        InputRow inputRow = InputRow.fromSchema(InputRowSchema.newBuilder().build());
        Evaluator evaluator = mock(Evaluator.class);
        when(evaluatorFactory.create(clazz)).thenReturn(evaluator);
        Row row = Row.newInstance();
        when(evaluator.evaluate(inputRow)).thenReturn(row);
        DataManager dataManager = mock(DataManager.class);
        uut.setDataManager(dataManager);

        uut.doCalc(DataRequest.newRequest(requesterId, inputRow, clazz));

        ArgumentCaptor<DataResult> captor = ArgumentCaptor.forClass(DataResult.class);

        verify(dataManager).onDataResult(captor.capture());

        assertThat(captor.getValue().getRequestId(), is(requesterId));
        assertThat(captor.getValue().getRow(), is(row));
    }

    @Test
    public void shouldCatchExceptionInDoCalc() throws Exception {
        UUID requesterId = UUID.randomUUID();
        Class<? extends Evaluator> clazz = Evaluator.class;
        InputRow inputRow = InputRow.fromSchema(InputRowSchema.newBuilder().build());
        Evaluator evaluator = mock(Evaluator.class);
        when(evaluatorFactory.create(clazz)).thenReturn(evaluator);
        doThrow(Exception.class).when(evaluator).evaluate(inputRow);
        DataManager dataManager = mock(DataManager.class);
        uut.setDataManager(dataManager);

        uut.doCalc(DataRequest.newRequest(requesterId, inputRow, clazz));

        verify(dataManager, never()).onDataResult(any(DataResult.class));
    }
}
