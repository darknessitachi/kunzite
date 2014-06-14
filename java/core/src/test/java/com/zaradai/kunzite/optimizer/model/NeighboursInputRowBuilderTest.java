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

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NeighboursInputRowBuilderTest {
    private static final String COL_NAME_1 = "col1";
    private static final String COL_NAME_2 = "col2";
    private static final String COL_NAME_3 = "col3";

    @Test
    public void shouldBuildAllNeighboursWithoutClamping() throws Exception {
        InputRow row = createInputRow();
        // pick centre so not clamped
        setRowValues(row, 5, 5, 5);
        Set<InputRow> values = Sets.newHashSet();
        NeighboursInputRowBuilder uut = new NeighboursInputRowBuilder(row);
        // build the neighbours set
        while (uut.next()) {
            values.add(uut.getCurrent());
        }
        // check results
        assertThat(values.size(), is(27));
        // ensure all valid values are generated
        InputRow testRow = createInputRow();
        assertValue(values, testRow, 4, 4, 4);
        assertValue(values, testRow, 4, 4, 5);
        assertValue(values, testRow, 4, 4, 6);
        assertValue(values, testRow, 4, 5, 4);
        assertValue(values, testRow, 4, 5, 5);
        assertValue(values, testRow, 4, 5, 6);
        assertValue(values, testRow, 4, 6, 4);
        assertValue(values, testRow, 4, 6, 5);
        assertValue(values, testRow, 4, 6, 6);

        assertValue(values, testRow, 5, 4, 4);
        assertValue(values, testRow, 5, 4, 5);
        assertValue(values, testRow, 5, 4, 6);
        assertValue(values, testRow, 5, 5, 4);
        assertValue(values, testRow, 5, 5, 5);
        assertValue(values, testRow, 5, 5, 6);
        assertValue(values, testRow, 5, 6, 4);
        assertValue(values, testRow, 5, 6, 5);
        assertValue(values, testRow, 5, 6, 6);

        assertValue(values, testRow, 6, 4, 4);
        assertValue(values, testRow, 6, 4, 5);
        assertValue(values, testRow, 6, 4, 6);
        assertValue(values, testRow, 6, 5, 4);
        assertValue(values, testRow, 6, 5, 5);
        assertValue(values, testRow, 6, 5, 6);
        assertValue(values, testRow, 6, 6, 4);
        assertValue(values, testRow, 6, 6, 5);
        assertValue(values, testRow, 6, 6, 6);
    }

    @Test
    public void shouldBuildAllNeighboursWithClamping() throws Exception {
        InputRow row = createInputRow();
        // pick centre so it will be clamped
        setRowValues(row, 5, 0, 5);
        Set<InputRow> values = Sets.newHashSet();
        NeighboursInputRowBuilder uut = new NeighboursInputRowBuilder(row);
        // build the neighbours set
        while (uut.next()) {
            values.add(uut.getCurrent());
        }
        // check results
        assertThat(values.size(), is(18));
        // ensure all valid values are generated
        InputRow testRow = createInputRow();
        assertValue(values, testRow, 4, 0, 4);
        assertValue(values, testRow, 4, 0, 5);
        assertValue(values, testRow, 4, 0, 6);
        assertValue(values, testRow, 4, 1, 4);
        assertValue(values, testRow, 4, 1, 5);
        assertValue(values, testRow, 4, 1, 6);

        assertValue(values, testRow, 5, 0, 4);
        assertValue(values, testRow, 5, 0, 5);
        assertValue(values, testRow, 5, 0, 6);
        assertValue(values, testRow, 5, 1, 4);
        assertValue(values, testRow, 5, 1, 5);
        assertValue(values, testRow, 5, 1, 6);

        assertValue(values, testRow, 6, 0, 4);
        assertValue(values, testRow, 6, 0, 5);
        assertValue(values, testRow, 6, 0, 6);
        assertValue(values, testRow, 6, 1, 4);
        assertValue(values, testRow, 6, 1, 5);
        assertValue(values, testRow, 6, 1, 6);
    }


    private void assertValue(Set<InputRow> values, InputRow row, int one, int two, int three) {
        // setup row values
        setRowValues(row, one, two, three);
        assertThat(values.contains(row), is(true));
    }

    private InputRow createInputRow() {
        return InputRow.fromSchema(InputRowSchema.newBuilder()
                .withName(COL_NAME_1).from(1.0).until(10.0).withStep(0.5)
                .withName(COL_NAME_2).from(1.0).until(10.0).withStep(0.5)
                .withName(COL_NAME_3).from(1.0).until(10.0).withStep(0.5)
                .build());
    }

    private void setRowValues(InputRow row, int one, int two, int three) {
        row.setValue(0, one);
        row.setValue(1, two);
        row.setValue(2, three);
    }
}
