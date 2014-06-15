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
package com.zaradai.kunzite.optimizer.data.dataset.driver.dummy;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.Iterator;
import java.util.List;

public final class DummyDataSetDriver implements DataSetDriver {
    private final List<Row> data = Lists.newArrayList();
    private final DataSetContext context;

    private DummyDataSetDriver(DataSetContext context) {
        this.context = context;
    }

    public static DummyDataSetDriver createWithContext(DataSetContext context) {
        Preconditions.checkNotNull(context, "Invalid context");

        return new DummyDataSetDriver(context);
    }

    @Override
    public void close() {
        //NOP
    }

    @Override
    public void add(Row row) {
        //NOP
    }

    @Override
    public Row get(InputRow key) {
        return null;  //NOP
    }

    @Override
    public long getRowCount() {
        return data.size();
    }

    @Override
    public Iterator<Row> iterator() {
        return data.iterator();
    }
}
