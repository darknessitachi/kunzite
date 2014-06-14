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
package com.zaradai.kunzite.optimizer.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public final class OutputRowSchema {
    private final Map<String, Integer> columnByName;
    private final List<String> columns;
    private int numColumns;

    private OutputRowSchema() {
        columnByName = createColumnMap();
        columns = createColumnList();
        numColumns = 0;
    }

    private List<String> createColumnList() {
        return Lists.newArrayList();
    }

    private Map<String, Integer> createColumnMap() {
        return Maps.newHashMap();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getIndex(String name) {
        Preconditions.checkArgument(columnByName.containsKey(name), "Unknown column name");

        return columnByName.get(name);
    }

    public String getName(int index) {
        Preconditions.checkElementIndex(index, getNumColumns(), "Index is out of range");

        for (Map.Entry<String, Integer> entry : columnByName.entrySet()) {
            if (entry.getValue() == index) {
                return entry.getKey();
            }
        }

        return null;
    }

    public List<String> getColumns() {
        return ImmutableList.copyOf(columns);
    }

    public static OutputRowSchemaBuilder newBuilder() {
        return new OutputRowSchemaBuilder();
    }

    private void addColumn(String name) {
        int position = getNumColumns();
        columnByName.put(name, position);
        numColumns++;
        columns.add(name);
    }

    public static final class OutputRowSchemaBuilder {
        private final OutputRowSchema rowSchema;

        private OutputRowSchemaBuilder() {
            rowSchema = new OutputRowSchema();
        }

        public OutputRowSchemaBuilder withName(String name) {
            rowSchema.addColumn(name);

            return this;
        }

        public OutputRowSchema build() {
            return rowSchema;
        }
    }
}
