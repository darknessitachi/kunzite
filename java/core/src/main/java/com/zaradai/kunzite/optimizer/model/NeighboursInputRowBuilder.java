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

public final class NeighboursInputRowBuilder extends AbstractInputRowBuilder {
    private final InputRow centre;

    public NeighboursInputRowBuilder(InputRow centre) {
        super(centre.getSchema());
        this.centre = centre;
    }

    @Override
    public boolean next() {
        boolean res = true;

        if (getCurrent() == null) {
            initialize();
        } else {
            // clone current and set so that the cloned version is operated on
            setCurrent((InputRow) getCurrent().clone());
            // now flip lsb
            res = increment(0);
        }

        return res;
    }

    private void initialize() {
        setCurrent(createRow());
        // reset row to either 1 before reference or bound to 0
        for (int i = 0; i < getSchema().getNumColumns(); ++i) {
            reset(i);
        }
    }

    private void reset(int i) {
        int centreValue = centre.getValue(i);
        if (centreValue > 0) {
            // set to previous of ref row
            getCurrent().setValue(i, centreValue - 1);
        } else {
            getCurrent().setValue(i, 0);
        }
    }

    private boolean increment(int index) {
        boolean res = true;

        if (index >= 0 && index < getSchema().getNumColumns()) {
            // increment the column value pointed to by index
            int value = getCurrent().getValue(index) + 1;
            int max = centre.getValue(index) + 1;
            // ensure column value is not
            // 1. Greater than reference row value + 1, or
            // 2. greater than number of defined steps in this column
            if (value > max || value >= getStepsInColumn(index)) {
                // otherwise reset current column value based on ref row
                reset(index);
                // and increment next column
                return increment(index + 1);
            } else {
                // set value
                getCurrent().setValue(index, value);
            }
        } else {
            // all available columns incremented
            res = false;
        }

        return res;
    }
}
