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
package com.zaradai.kunzite.optimizer.data.dataset.driver.memory;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class InMemoryDataSetDatabaseTest {
    @Mock
    private Map<DataSetContext, DataSetDriver> mapMock;
    @Captor
    private ArgumentCaptor<InMemoryDataSetDriver> driverArgumentCaptor;
    @Captor
    private ArgumentCaptor<DataSetContext> contextArgumentCaptor;

    private InMemoryDataSetDatabase uut;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        uut = new InMemoryDataSetDatabase() {
            @Override
            protected Map<DataSetContext, DataSetDriver> createDataMap() {
                return mapMock;
            }
        };
    }

    @Test
    public void shouldAddNewContextAndAssignToDriver() throws Exception {
        DataSetContext context = buildContext();

        uut.addOrUpdate(context);

        verify(mapMock).put(contextArgumentCaptor.capture(), driverArgumentCaptor.capture());
        assertThat(driverArgumentCaptor.getValue(), not(nullValue()));
        assertThat(contextArgumentCaptor.getValue(), is(context));
    }

    @Test
    public void shouldRemoveThenReinsertOnUpdate() throws Exception {
        DataSetDriver driver = mock(DataSetDriver.class);
        DataSetContext context = buildContext();
        when(mapMock.remove(context)).thenReturn(driver);

        uut.addOrUpdate(context);

        verify(mapMock).put(context, driver);
    }

    @Test
    public void shouldCloseAllDriversOnClose() throws Exception {
        List<DataSetDriver> drivers = Lists.newArrayList();
        DataSetDriver driver = mock(DataSetDriver.class);
        drivers.add(driver);
        when(mapMock.values()).thenReturn(drivers);

        uut.close();

        verify(driver).close();
        verify(mapMock).clear();
    }

    @Test
    public void shouldGetAllContexts() throws Exception {
        DataSetContext context = buildContext();
        uut = new InMemoryDataSetDatabase();
        uut.addOrUpdate(context);

        List<DataSetContext> res = uut.getAllContexts();

        assertThat(res.get(0), is(context));
    }

    @Test
    public void shouldOpenAndReturnDriverForContextIfNotExisting() throws Exception {
        DataSetContext context = buildContext();
        uut = new InMemoryDataSetDatabase();

        DataSetDriver res = uut.open(context);

        assertThat(res, not(nullValue()));
    }

    @Test
    public void shouldReturnExistingDriverForMatchingContextOnOpen() throws Exception {
        DataSetContext context = buildContext();
        uut = new InMemoryDataSetDatabase();
        DataSetDriver driver = uut.open(context);

        DataSetDriver res = uut.open(context);

        assertThat(res, is(driver));
    }

    private DataSetContext buildContext() {
        return DataSetContext.builder().name("test").build();
    }
}
