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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDatabase;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDriver;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public final class InMemoryDataSetDatabase implements DataSetDatabase {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryDataSetDatabase.class);
    private final Map<DataSetContext, DataSetDriver> activeDataSets;

    @Inject
    InMemoryDataSetDatabase() {
        activeDataSets = createDataMap();
    }

    protected Map<DataSetContext, DataSetDriver> createDataMap() {
        return Maps.newHashMap();
    }

    @Override
    public void connect() throws DriverException {
        LOGGER.info("In Memory DB Opened");
    }

    @Override
    public void close() {
        // close and flush all open datasets
        for (DataSetDriver driver : activeDataSets.values()) {
            driver.close();
        }
        activeDataSets.clear();
        LOGGER.info("In Memory DB Closed");
    }

    @Override
    public void addOrUpdate(DataSetContext context) {
        DataSetDriver res = activeDataSets.remove(context);

        if (res == null) {
            open(context);
        } else {
            activeDataSets.put(context, res);
        }
    }

    @Override
    public List<DataSetContext> getAllContexts() {
        return ImmutableList.copyOf(activeDataSets.keySet());
    }


    @Override
    public DataSetDriver open(DataSetContext context) {
        // do we have one open?
        DataSetDriver res = activeDataSets.get(context);

        if (res == null) {
            res = InMemoryDataSetDriver.createWithContext(context);
            activeDataSets.put(context, res);
        }

        return res;
    }
}
