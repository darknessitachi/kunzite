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
package com.zaradai.kunzite.optimizer.data.dataset.cache.memory;

import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.config.Configuration;
import com.zaradai.kunzite.optimizer.data.dataset.cache.DataCache;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryDataCache implements DataCache {
    private static final float LOAD_FACTOR = 0.75f;
    private final Map<InputRow, Row> cache;

    @Inject
    InMemoryDataCache(Configuration configuration) {
        cache = createCache(configuration.getMaxCacheSize());
    }

    protected Map<InputRow, Row> createCache(final int maxCacheSize) {
        return new LinkedHashMap<InputRow, Row>(maxCacheSize, LOAD_FACTOR, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<InputRow, Row> eldest) {
                return size() > maxCacheSize;
            }
        };
    }

    @Override
    public Row get(InputRow key) {
        return cache.get(key);
    }

    @Override
    public void put(Row value) {
        cache.put(value.getInput(), value);
    }
}
