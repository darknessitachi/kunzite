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

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.eval.CalcEngine;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
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

public class DefaultDataManagerTest {
    private DataRequestManager dataRequestManager;
    private CalcEngine calcEngine;
    private DataSet dataSet;
    private DefaultDataManager uut;
    private MetricRegistry metricRegistry;

    @Before
    public void setUp() throws Exception {
        dataRequestManager = mock(DataRequestManager.class);
        metricRegistry = mock(MetricRegistry.class);
        Meter fromDbMeter = mock(Meter.class);
        Meter newCalc = mock(Meter.class);
        when(metricRegistry.meter(DefaultDataManager.METRIC_NAME_FROM_DB)).thenReturn(fromDbMeter);
        when(metricRegistry.meter(DefaultDataManager.METRIC_NAME_NEW_CALC)).thenReturn(newCalc);
        calcEngine = mock(CalcEngine.class);
        dataSet = mock(DataSet.class);
        uut = new DefaultDataManager(metricRegistry, dataRequestManager, calcEngine, dataSet);
    }

    @Test
    public void shouldConstructProperly() throws Exception {
        verify(dataRequestManager).addListener(uut);
        verify(calcEngine).setDataManager(uut);
        verify(metricRegistry).meter(DefaultDataManager.METRIC_NAME_FROM_DB);
        verify(metricRegistry).meter(DefaultDataManager.METRIC_NAME_NEW_CALC);
    }

    @Test
    public void shouldHandleDataResult() throws Exception {
        UUID id = UUID.randomUUID();
        Row row = Row.newInstance();
        DataResult dataResult = DataResult.newResult(id, row);

        uut.onDataResult(dataResult);

        verify(dataSet).add(row);
        verify(dataRequestManager).handleResult(dataResult);
    }

    @Test
    public void shouldHandleRequestWithCalcEngineIfNoResult() throws Exception {
        UUID id = UUID.randomUUID();
        InputRow request = InputRow.fromSchema(InputRowSchema.newBuilder().build());
        Class<Evaluator> evaluator = Evaluator.class;
        DataRequest dataRequest = DataRequest.newRequest(id, request, evaluator);
        when(dataSet.get(request)).thenReturn(null);
        uut.onRequest(dataRequest);

        verify(calcEngine).calculate(dataRequest);
    }

    @Test
    public void shouldHandleRequestWithManagerIfResultExists() throws Exception {
        UUID id = UUID.randomUUID();
        InputRow request = InputRow.fromSchema(InputRowSchema.newBuilder().build());
        Class<Evaluator> evaluator = Evaluator.class;
        DataRequest dataRequest = DataRequest.newRequest(id, request, evaluator);
        Row row = Row.newInstance();
        when(dataSet.get(request)).thenReturn(row);
        uut.onRequest(dataRequest);

        ArgumentCaptor<DataResult> captor = ArgumentCaptor.forClass(DataResult.class);
        verify(dataRequestManager).handleResult(captor.capture());

        assertThat(captor.getValue().getRequestId(), is(id));
        assertThat(captor.getValue().getRow(), is(row));
    }
}
