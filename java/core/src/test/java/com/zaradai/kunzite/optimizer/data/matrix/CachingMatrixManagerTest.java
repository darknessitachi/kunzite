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
package com.zaradai.kunzite.optimizer.data.matrix;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.model.Row;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class CachingMatrixManagerTest {
    private List<Row> rows = Lists.newArrayList();
    private DataSet dataSet;
    private CachingMatrixManager uut;
    @Mock
    Map<String, ResultMatrix> mockList;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        DataSetContext context = DataSetContext.builder()
                .name("test")
                .input(ResultMatrixTest.TEST_INPUT_SCHEMA)
                .build();
        dataSet = mock(DataSet.class);
        when(dataSet.getContext()).thenReturn(context);
        when(dataSet.iterator()).thenReturn(rows.iterator());
        uut = new CachingMatrixManager(dataSet);
    }

    @Test
    public void shouldCreateProperly() throws Exception {
        verify(dataSet).registerUpdateListener(uut);
    }

    @Test
    public void shouldCreateNewMatrixOnGetIfNotExists() throws Exception {
        ResultMatrix res = uut.get(ResultMatrixTest.TEST_COL_X, ResultMatrixTest.TEST_COL_Y,
                ResultMatrixTest.TEST_TARGET);

        assertThat(res, not(nullValue()));
    }

    @Test
    public void shouldReturnReversedMatrixIfExists() throws Exception {
        // prime matrix
        uut.get(ResultMatrixTest.TEST_COL_X, ResultMatrixTest.TEST_COL_Y, ResultMatrixTest.TEST_TARGET);
        // request reverse of it
        ResultMatrix res = uut.get(ResultMatrixTest.TEST_COL_Y, ResultMatrixTest.TEST_COL_X,
                ResultMatrixTest.TEST_TARGET);

        assertThat(res, not(nullValue()));
    }

    @Test
    public void shouldUpdateMatrixWithUpdatedRows() throws Exception {
        ResultMatrix resultMatrix = mock(ResultMatrix.class);
        List<ResultMatrix> resultMatrices = Lists.newArrayList(resultMatrix);
        when(mockList.values()).thenReturn(resultMatrices);

        uut = new CachingMatrixManager(dataSet) {
            @Override
            protected Map<String, ResultMatrix> createMatrixMap() {
                return mockList;
            }
        };
        Row toUpdate = ResultMatrixTest.createTestRow(1.0);

        uut.onUpdate(toUpdate);

        verify(resultMatrix).update(toUpdate);
    }
}
