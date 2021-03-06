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

public final class InputRowSchema {
    private final Map<String, InputSpec> columnByName;
    private final List<String> columns;
    private int numColumns;

    private InputRowSchema() {
        columnByName = createColumnMap();
        columns = createColumnList();
    }

    private List<String> createColumnList() {
        return Lists.newArrayList();
    }

    private Map<String, InputSpec> createColumnMap() {
        return Maps.newHashMap();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getIndex(String name) {
        Preconditions.checkArgument(columnByName.containsKey(name), "Unknown column name");
        return columnByName.get(name).getPosition();
    }

    public String getName(int index) {
        Preconditions.checkElementIndex(index, getNumColumns(), "Index is out of range");

        for (Map.Entry<String, InputSpec> entry : columnByName.entrySet()) {
            if (entry.getValue().getPosition() == index) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Series getSeries(String name) {
        Preconditions.checkArgument(columnByName.containsKey(name), "Unknown column name");

        return columnByName.get(name).getSeries();
    }

    public Series getSeries(int index) {
        Preconditions.checkElementIndex(index, getNumColumns(), "Index is out of range");

        for (Map.Entry<String, InputSpec> entry : columnByName.entrySet()) {
            if (entry.getValue().getPosition() == index) {
                return entry.getValue().getSeries();
            }
        }

        return null;
    }

    public boolean hasColumn(String name) {
        return columnByName.containsKey(name);
    }

    public List<String> getColumns() {
        return ImmutableList.copyOf(columns);
    }

    public int getMaxCalculations() {
        int res = 1;

        for (InputSpec spec : columnByName.values()) {
            res *= spec.getSeries().getSteps();
        }

        return res;
    }

    public static InputRowSchemaBuilder newBuilder() {
        return new InputRowSchemaBuilder();
    }

    private void addColumn(String name, Series series) {
        int position = getNumColumns();
        columnByName.put(name, InputSpec.newInstance(position, series));
        numColumns++;
        columns.add(name);
    }

    public static final class InputRowSchemaBuilder {
        private final InputRowSchema inputRowSchema;

        private InputRowSchemaBuilder() {
            inputRowSchema = new InputRowSchema();
        }

        public SeriesBuilder withName(String name) {
            return new SeriesBuilder(this, name);
        }

        public InputRowSchemaBuilder with(String name, Series series) {
            inputRowSchema.addColumn(name, series);

            return this;
        }

        public InputRowSchema build() {
            return inputRowSchema;
        }
    }

    public static final class SeriesBuilder {
        private final InputRowSchemaBuilder builder;
        private final String name;
        private double start;
        private double end;
        private double step;

        private SeriesBuilder(InputRowSchemaBuilder builder, String name) {
            this.builder = builder;
            this.name = name;
        }

        public SeriesBuilder from(double from) {
            start = from;
            return this;
        }

        public SeriesBuilder until(double until) {
            end = until;
            return this;
        }

        public InputRowSchemaBuilder withStep(double stepValue) {
            // no need to do preconditions here as Series will do necessary checks
            return builder.with(name, Series.newMinMaxSeries(start, end, stepValue));
        }

        public SeriesBuilder step(double stepValue) {
            step = stepValue;
            return this;
        }

        public InputRowSchemaBuilder withSteps(int numSteps) {
            // no need to do preconditions here as Series will do necessary checks
            return builder.with(name, Series.newStepSeries(start, step, numSteps));
        }
    }
}
