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
package com.zaradai.kunzite.optimizer.data.dataset;

import com.zaradai.kunzite.optimizer.data.dataset.cache.DataCache;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.model.OutputRowSchema;
import com.zaradai.kunzite.optimizer.model.Row;
import com.zaradai.kunzite.optimizer.model.RowSchema;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultDataSetTest {

    private DataCache cache;
    private DataSetDriver dataSetDriver;
    private DefaultDataSet uut;
    private DataSetContext dataSetContext;
    @Mock
    private List<DataSetUpdateListener> listenList;
    @Mock
    private Iterator<Row> iterator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        cache = mock(DataCache.class);
        dataSetDriver = mock(DataSetDriver.class);
        dataSetContext = DataSetContext.builder().name("test").build();
        uut = new DefaultDataSet(cache, dataSetDriver, dataSetContext);
    }

    @Test
    public void shouldGetContext() throws Exception {
        assertThat(uut.getContext(), is(dataSetContext));
    }

    @Test
    public void shouldRegisterListeners() throws Exception {
        uut = new DefaultDataSet(cache, dataSetDriver, dataSetContext) {
            @Override
            protected List<DataSetUpdateListener> createListenerList() {
                return listenList;
            }
        };
        DataSetUpdateListener listener = mock(DataSetUpdateListener.class);

        uut.registerUpdateListener(listener);

        verify(listenList).add(listener);
    }

    @Test
    public void shouldLockUnlockOnUpdateListener() throws Exception {
        DataSetUpdateListener listener = mock(DataSetUpdateListener.class);
        final Lock lock = mock(Lock.class);
        final ReadWriteLock readWriteLock = mock(ReadWriteLock.class);
        when(readWriteLock.writeLock()).thenReturn(lock);
        uut = new DefaultDataSet(cache, dataSetDriver, dataSetContext) {
            @Override
            protected List<DataSetUpdateListener> createListenerList() {
                return listenList;
            }

            @Override
            protected ReadWriteLock createListenerLock() {
                return readWriteLock;
            }
        };
        InOrder inOrder = inOrder(lock);

        uut.registerUpdateListener(listener);

        inOrder.verify(lock).lock();
        inOrder.verify(lock).unlock();
    }

    @Test
    public void shouldUnRegisterListeners() throws Exception {
        uut = new DefaultDataSet(cache, dataSetDriver, dataSetContext) {
            @Override
            protected List<DataSetUpdateListener> createListenerList() {
                return listenList;
            }
        };
        DataSetUpdateListener listener = mock(DataSetUpdateListener.class);

        uut.unRegisterUpdateListener(listener);

        verify(listenList).remove(listener);
    }

    @Test
    public void shouldLockUnlockOnRemoveListener() throws Exception {
        DataSetUpdateListener listener = mock(DataSetUpdateListener.class);
        final Lock lock = mock(Lock.class);
        final ReadWriteLock readWriteLock = mock(ReadWriteLock.class);
        when(readWriteLock.writeLock()).thenReturn(lock);
        uut = new DefaultDataSet(cache, dataSetDriver, dataSetContext) {
            @Override
            protected List<DataSetUpdateListener> createListenerList() {
                return listenList;
            }

            @Override
            protected ReadWriteLock createListenerLock() {
                return readWriteLock;
            }
        };
        InOrder inOrder = inOrder(lock);

        uut.unRegisterUpdateListener(listener);

        inOrder.verify(lock).lock();
        inOrder.verify(lock).unlock();
    }

    @Test
    public void shouldCloseDriverOnClose() throws Exception {
        uut.close();

        verify(dataSetDriver).close();
    }

    @Test
    public void shouldAddRow() throws Exception {
        Row row = createRow();
        DataSetUpdateListener listener = mock(DataSetUpdateListener.class);
        uut.registerUpdateListener(listener);

        uut.add(row);

        verify(dataSetDriver).add(row);
        verify(cache).put(row);
        verify(listener).onUpdate(row);
    }

    private Row createRow() {
        return Row.fromSchema(RowSchema.newInstance(
                InputRowSchema.newBuilder().build(),
                OutputRowSchema.newBuilder().build()
        ));
    }

    @Test
    public void shouldGetFromCache() throws Exception {
        Row row = createRow();
        when(cache.get(row.getInput())).thenReturn(row);

        Row res = uut.get(row.getInput());

        assertThat(row.equals(res), is(true));
    }

    @Test
    public void shouldGetFromDriverAndAddToCache() throws Exception {
        Row row = createRow();
        when(dataSetDriver.get(row.getInput())).thenReturn(row);

        Row res = uut.get(row.getInput());

        assertThat(row.equals(res), is(true));
        verify(cache).put(row);
    }

    @Test
    public void shouldGetRowCount() throws Exception {
        Row row = createRow();
        uut.add(row);

        assertThat(uut.getRowCount(), is(1L));
    }

    @Test
    public void shouldReturnDriverIterator() throws Exception {
        when(dataSetDriver.iterator()).thenReturn(iterator);

        assertThat(uut.iterator(), is(iterator));
    }
}
