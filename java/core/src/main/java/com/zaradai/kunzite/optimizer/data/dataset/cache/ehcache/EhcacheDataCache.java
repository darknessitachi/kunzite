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
package com.zaradai.kunzite.optimizer.data.dataset.cache.ehcache;

import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.data.dataset.cache.DataCache;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheDataCache implements DataCache {
    private final Ehcache cache;

    @Inject
    EhcacheDataCache(Ehcache cache) {
        this.cache = cache;
    }

    @Override
    public Row get(InputRow key) {
        Element element = cache.get(key);

        if (element != null) {
            return (Row) element.getObjectValue();
        }

        return null;
    }

    @Override
    public void put(Row value) {
        cache.put(new Element(value.getInput(), value));
    }
}
