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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.Arrays;

public final class OutputRow implements Cloneable {
    private final OutputRowSchema schema;
    private final double[] values;

    private OutputRow(OutputRowSchema schema) {
        this.schema = schema;
        values = new double[schema.getNumColumns()];
    }

    public static OutputRow fromSchema(OutputRowSchema schema) {
        Preconditions.checkNotNull(schema, "Must be created with valid schema");

        return new OutputRow(schema);
    }

    public double getValue(String name) {
        return values[schema.getIndex(name)];
    }

    public void setValue(String name, double value) {
        values[schema.getIndex(name)] = value;
    }

    public double getValue(int index) {
        Preconditions.checkPositionIndex(index, values.length, "Index is out of range");

        return values[index];
    }

    public void setValue(int index, double value) {
        Preconditions.checkPositionIndex(index, values.length, "Index is out of range");

        values[index] = value;
    }

    public int getNumColumns() {
        return values.length;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final OutputRow other = (OutputRow) obj;
        // don't use Guava equals as it uses identity equality.
        return Arrays.equals(values, other.values);
    }

    @Override
    public Object clone() {
        OutputRow res = fromSchema(schema);
        System.arraycopy(values, 0, res.values, 0, values.length);

        return res;
    }

    @Override
    public String toString() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this);

        for (int i = 0; i < getNumColumns(); ++i) {
            helper.add(schema.getName(i), getValue(i));
        }

        return helper.toString();
    }

    public OutputRowSchema getSchema() {
        return schema;
    }
}
