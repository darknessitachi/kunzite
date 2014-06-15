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
package com.zaradai.kunzite.optimizer.data.dataset.cache.dummy;

import com.zaradai.kunzite.optimizer.data.dataset.cache.DataCache;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;

public final class DummyDataCache implements DataCache {
    @Override
    public Row get(InputRow key) {
        return null;  //NOP
    }

    @Override
    public void put(Row value) {
        //NOP
    }
}
