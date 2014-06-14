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
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public final class InputRowGenerator {
    private InputRowGenerator() {
    }

    public static InputRow getFirst(InputRowSchema schema) {
        return InputRow.fromSchema(schema);
    }

    public static InputRow getRandom(InputRowSchema schema) {
        InputRow res = getFirst(schema);
        Random r = new Random();

        for (int i = 0; i < schema.getNumColumns(); ++i) {
            int steps = getStepsInColumn(res, i);
            res.setValue(i, r.nextInt(steps));
        }

        return res;
    }

    public static InputRow getNext(InputRow from) {
        Preconditions.checkNotNull(from, "InputRow cannot be null");

        InputRow res = (InputRow) from.clone();
        increment(res, 0);

        return res;
    }

    public static InputRow getPrev(InputRow from) {
        Preconditions.checkNotNull(from, "InputRow cannot be null");

        InputRow res = (InputRow) from.clone();
        decrement(res, 0);

        return res;
    }

    public static boolean increment(InputRow row, int index) {
        Preconditions.checkNotNull(row, "InputRow cannot be null");

        boolean res = false;

        if (index >= 0 && index < row.getNumColumns()) {
            // increment the column value pointed to by index
            int value = row.getValue(index) + 1;
            // ensure column value is not greater than number of defined steps in the column
            if (value >= getStepsInColumn(row, index)) {
                // otherwise reset current column value
                row.setValue(index, 0);
                // and increment next column
                return increment(row, index + 1);
            } else {
                // set value
                row.setValue(index, value);
            }

            res = true;
        }

        return res;
    }

    public static boolean decrement(InputRow row, int index) {
        Preconditions.checkNotNull(row, "InputRow cannot be null");

        boolean res = false;

        if (index >= 0 && index < row.getNumColumns()) {
            // decrement the column value pointed to by index
            int value = row.getValue(index) - 1;
            // ensure column value is not less than zero
            if (value < 0) {
                // otherwise set Max
                row.setValue(index, getStepsInColumn(row, index) - 1);
                // and decrement next column
                return decrement(row, index + 1);
            } else {
                // set value
                row.setValue(index, value);
            }

            res = true;
        }

        return res;
    }

    public static List<InputRow> getNeighbours(InputRow start) {
        Preconditions.checkNotNull(start, "InputRow cannot be null");

        List<InputRow> res = Lists.newArrayList();
        NeighboursInputRowBuilder rowBuilder = new NeighboursInputRowBuilder(start);

        while (rowBuilder.next()) {
            res.add(rowBuilder.getCurrent());
        }

        return res;
    }

    private static int getStepsInColumn(InputRow inputRow, int index) {
        return inputRow.getSeries(index).getSteps();
    }
}
