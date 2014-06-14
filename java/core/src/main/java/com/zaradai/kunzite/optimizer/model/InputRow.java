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

public final class InputRow implements Cloneable {
    private final InputRowSchema schema;
    private final int[] values;

    private InputRow(InputRowSchema schema) {
        this.schema = schema;
        values = new int[schema.getNumColumns()];
    }

    public static InputRow fromSchema(InputRowSchema schema) {
        Preconditions.checkNotNull(schema, "Input row needs valid schema to build");

        return new InputRow(schema);
    }

    public int getValue(String name) {
        return values[schema.getIndex(name)];
    }

    public void setValue(String name, int value) {
        values[schema.getIndex(name)] = value;
    }

    public int getValue(int index) {
        Preconditions.checkPositionIndex(index, values.length, "Index is out of range");
        return values[index];
    }

    public void setValue(int index, int value) {
        Preconditions.checkPositionIndex(index, values.length, "Index is out of range");
        values[index] = value;
    }

    public int getNumColumns() {
        return values.length;
    }

    public double getInputValue(int index) {
        int stepValue = getValue(index);
        return schema.getSeries(index).getValue(stepValue);
    }

    public double getInputValue(String name) {
        int stepValue = getValue(name);
        return schema.getSeries(name).getValue(stepValue);
    }

    /**
     * Hash code will only take into account the input rows positional values.
     * @return
     */
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

        final InputRow other = (InputRow) obj;
        // don't use Guava equals as it uses identity equality.
        return Arrays.equals(values, other.values);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        InputRow res = fromSchema(schema);
        System.arraycopy(values, 0, res.values, 0, values.length);

        return res;
    }

    @Override
    public String toString() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this);

        for (int i = 0; i < getNumColumns(); ++i) {
            String value = "[" + getValue(i) + "]" + getInputValue(i);
            helper.add(schema.getName(i), value);
        }

        return helper.toString();
    }
}
