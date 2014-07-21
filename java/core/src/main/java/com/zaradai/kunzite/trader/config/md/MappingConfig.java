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
package com.zaradai.kunzite.trader.config.md;

import com.google.common.collect.Lists;

import java.util.List;

public class MappingConfig {
    private String name;
    private final List<MappingValue> mappings;

    public MappingConfig() {
        mappings = Lists.newArrayList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Iterable<MappingValue> getMappings() {
        return mappings;
    }

    public void add(MappingValue value) {
        mappings.add(value);
    }
}
