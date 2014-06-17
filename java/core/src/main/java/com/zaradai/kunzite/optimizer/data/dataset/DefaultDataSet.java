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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.optimizer.data.dataset.cache.DataCache;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.data.matrix.MatrixManager;
import com.zaradai.kunzite.optimizer.data.matrix.MatrixManagerFactory;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultDataSet implements DataSet {
    private final DataCache cache;
    private final DataSetDriver driver;
    private final DataSetContext context;
    private final MatrixManager matrixManager;
    private final List<DataSetUpdateListener> listeners;
    private final ReadWriteLock listenerLock;
    private long rowCount;

    @Inject
    DefaultDataSet(DataCache cache, MatrixManagerFactory matrixManagerFactory, @Assisted DataSetDriver driver,
                   @Assisted DataSetContext context) {
        this.cache = cache;
        this.driver = driver;
        this.context = context;
        listeners = createListenerList();
        listenerLock = createListenerLock();

        this.matrixManager = matrixManagerFactory.create(this);
        this.rowCount = driver.getRowCount();
    }

    protected ReadWriteLock createListenerLock() {
        return new ReentrantReadWriteLock();
    }

    protected List<DataSetUpdateListener> createListenerList() {
        return Lists.newArrayList();
    }

    @Override
    public DataSetContext getContext() {
        return context;
    }

    @Override
    public void registerUpdateListener(DataSetUpdateListener listener) {
        listenerLock.writeLock().lock();

        try {
            listeners.add(listener);
        } finally {
            listenerLock.writeLock().unlock();
        }
    }

    @Override
    public void unRegisterUpdateListener(DataSetUpdateListener listener) {
        listenerLock.writeLock().lock();

        try {
            listeners.remove(listener);
        } finally {
            listenerLock.writeLock().unlock();
        }
    }

    @Override
    public void close() {
        driver.close();
        //?? empty cache
    }

    @Override
    public void add(Row row) {
        // add to the store
        driver.add(row);
        // add to the cache
        addToCache(row);
        // increase row count
        this.rowCount++;
        // update listeners
        updateListeners(row);
    }

    private void updateListeners(Row row) {
        listenerLock.readLock().lock();

        try {
            for (DataSetUpdateListener listener : listeners) {
                listener.onUpdate(row);
            }
        } finally {
            listenerLock.readLock().unlock();
        }
    }

    @Override
    public Row get(InputRow key) {
        Row res = null;
        // try the cache
        res = cache.get(key);
        // if not try the datastore
        if (res == null) {
            res = driver.get(key);
            // if we retrieve a row ensure the cache gets it
            if (res != null) {
                addToCache(res);
            }
        }

        return res;
    }

    private void addToCache(Row res) {
        cache.put(res);
    }

    @Override
    public long getRowCount() {
        return this.rowCount;
    }

    @Override
    public Iterator<Row> iterator() {
        return driver.iterator();
    }

    @Override
    public MatrixManager getMatrixManager() {
        return matrixManager;
    }
}
