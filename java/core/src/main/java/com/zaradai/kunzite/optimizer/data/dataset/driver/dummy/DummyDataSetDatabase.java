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

import com.google.common.collect.Lists;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDatabase;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DriverException;

import java.util.List;

public final class DummyDataSetDatabase implements DataSetDatabase {
    @Override
    public void connect() throws DriverException {
        //NOP
    }

    @Override
    public void close() {
        //NOP
    }

    @Override
    public void addOrUpdate(DataSetContext context) {
        //NOP
    }

    @Override
    public List<DataSetContext> getAllContexts() {
        return Lists.newArrayList();
    }

    @Override
    public DataSetDriver open(DataSetContext context) {
        return DummyDataSetDriver.createWithContext(context);
    }
}
