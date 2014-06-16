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
package com.zaradai.kunzite.optimizer.data.matrix;

import com.google.common.collect.Maps;

import java.util.Map;

public class SparseMatrix<T> {
    private final Map<Pair, T> values;

    public SparseMatrix() {
        values = createMap();
    }

    protected Map<Pair, T> createMap() {
        return Maps.newHashMap();
    }

    public T get(int x, int y) {
        return values.get(Pair.newPair(x, y));
    }

    public void set(int x, int y, T value) {
        values.put(Pair.newPair(x, y), value);
    }
}
