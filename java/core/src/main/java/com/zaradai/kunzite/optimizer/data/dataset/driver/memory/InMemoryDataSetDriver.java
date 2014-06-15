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

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class InMemoryDataSetDriver implements DataSetDriver {
    private final Map<InputRow, Row> data;
    private final DataSetContext context;
    private final ReadWriteLock dataLock;

    private InMemoryDataSetDriver(DataSetContext context) {
        this.data = createDataMap();
        this.context = context;
        this.dataLock = createDataLock();
    }

    private ReadWriteLock createDataLock() {
        return new ReentrantReadWriteLock();
    }

    private Map<InputRow, Row> createDataMap() {
        return Maps.newConcurrentMap();
    }

    public static InMemoryDataSetDriver createWithContext(DataSetContext context) {
        Preconditions.checkNotNull(context, "Invalid context");

        return new InMemoryDataSetDriver(context);
    }

    @Override
    public void close() {
        data.clear();
    }

    @Override
    public void add(Row row) {
        Preconditions.checkNotNull(row, "Its invalid to add null rows to a DataSet");
        Preconditions.checkNotNull(row.getInput(), "Its invalid to add rows with invalid input row to a DataSet");
        Preconditions.checkNotNull(row.getOutput(), "Its invalid to add rows with invalid output row to a DataSet");

        dataLock.writeLock().lock();
        try {
            data.put(row.getInput(), row);
        } finally {
            dataLock.writeLock().unlock();
        }
    }

    @Override
    public Row get(InputRow key) {
        Row res = null;

        dataLock.readLock().lock();
        try {
            res = data.get(key);
        } finally {
            dataLock.readLock().unlock();
        }

        return res;
    }

    @Override
    public long getRowCount() {
        dataLock.readLock().lock();
        try {
            return data.size();
        } finally {
            dataLock.readLock().unlock();
        }
    }

    @Override
    public Iterator<Row> iterator() {
        return data.values().iterator();
    }
}
