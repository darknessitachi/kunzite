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

import com.codahale.metrics.MetricRegistry;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.zaradai.kunzite.optimizer.data.DataManagerFactory;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.matrix.MatrixManager;
import com.zaradai.kunzite.optimizer.data.matrix.ResultMatrix;
import com.zaradai.kunzite.optimizer.eval.CalcEngine;
import com.zaradai.kunzite.optimizer.eval.CalcEngineFactory;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.model.Series;
import com.zaradai.kunzite.optimizer.tactic.OptimizerResult;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTactic;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTacticFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Callable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultOptimizerControllerTest {
    private static final DataSetContext TEST_CONTEXT = DataSetContext.builder().name("test").build();

    private DataManagerFactory dataManagerFactory;
    private CalcEngineFactory calcEngineFactory;
    private OptimizerTacticFactory optimizerTacticFactory;
    private MetricRegistry metricRegistry;
    private DataSet dataSet;
    private DefaultOptimizerController uut;
    @Mock
    private ListeningExecutorService listeningExecutorService;
    @Captor
    private ArgumentCaptor<Callable<OptimizerResult>> callableCaptor;
    private CalcEngine calcEngine;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        dataManagerFactory = mock(DataManagerFactory.class);
        calcEngineFactory = mock(CalcEngineFactory.class);
        calcEngine = mock(CalcEngine.class);
        when(calcEngineFactory.create()).thenReturn(calcEngine);
        when(calcEngine.startAsync()).thenReturn(calcEngine);
        when(calcEngine.stopAsync()).thenReturn(calcEngine);
        optimizerTacticFactory = mock(OptimizerTacticFactory.class);
        metricRegistry = mock(MetricRegistry.class);
        dataSet = mock(DataSet.class);
        when(dataSet.getContext()).thenReturn(TEST_CONTEXT);
        uut = new DefaultOptimizerController(dataManagerFactory, calcEngineFactory,
                optimizerTacticFactory, metricRegistry, dataSet) {
            @Override
            protected ListeningExecutorService createExecutorService() {
                return listeningExecutorService;
            }
        };
    }

    @Test
    public void shouldConstructProperly() throws Exception {
        verify(calcEngineFactory).create();
        verify(dataManagerFactory).create(calcEngine, dataSet);
    }

    @Test
    public void shouldStartUp() throws Exception {
        uut.startUp();

        verify(calcEngine).startAsync();
        verify(calcEngine).awaitRunning();
    }

    @Test
    public void shouldShutDown() throws Exception {
        uut.shutDown();

        verify(calcEngine).stopAsync();
        verify(calcEngine).awaitTerminated();
        verify(listeningExecutorService).shutdown();
        verify(dataSet).close();
    }

    @Test
    public void shouldGetContext() throws Exception {
        assertThat(uut.getContext(), is(TEST_CONTEXT));
    }

    @Test
    public void shouldGetDataSet() throws Exception {
        assertThat(uut.getDataSet(), is(dataSet));
    }

    @Test
    public void shouldOptimize() throws Exception {
        uut.startAsync().awaitRunning();

        uut.optimize(OptimizeRequest.newRequest(OptimizerTactic.class, "target", true,
                InputRow.fromSchema(InputRowSchema.newBuilder().build())));

        verify(listeningExecutorService).submit(callableCaptor.capture());
    }

    @Test
    public void shouldRequestMatrix() throws Exception {
        String columnX = "x";
        String columnY = "y";
        String target = "t";
        MatrixManager matrixManager = mock(MatrixManager.class);
        InputRowSchema inputRowSchema = buildInputSchema(columnX, columnY);
        ResultMatrix resultMatrix = ResultMatrix.newMatrix(columnX, columnY, target, inputRowSchema);
        when(dataSet.getMatrixManager()).thenReturn(matrixManager);
        when(matrixManager.get(columnX, columnY, target)).thenReturn(resultMatrix);

        ResultMatrix res = uut.requestMatrix(columnX, columnY, target);

        assertThat(res, is(resultMatrix));
    }

    private InputRowSchema buildInputSchema(String columnX, String columnY) {
        return InputRowSchema.newBuilder()
                .with(columnX, Series.newMinMaxSeries(1, 10, 1))
                .with(columnY, Series.newMinMaxSeries(1, 10, 1))
                .build();
    }
}
