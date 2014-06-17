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

import com.zaradai.kunzite.optimizer.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ResultMatrixTest {
    public static final String TEST_COL_X = "x";
    public static final String TEST_COL_Y = "y";
    public static final String TEST_TARGET = "t";
    public static final OutputRowSchema TEST_OUTPUT_SCHEMA = OutputRowSchema.newBuilder().withName(TEST_TARGET).build();
    public static final Series TEST_X_SERIES = Series.newMinMaxSeries(1, 10, 1);
    public static final Series TEST_Y_SERIES = Series.newMinMaxSeries(5, 20, 1);
    public static final InputRowSchema TEST_INPUT_SCHEMA = InputRowSchema.newBuilder()
            .with(TEST_COL_X, TEST_X_SERIES)
            .with(TEST_COL_Y, TEST_Y_SERIES)
            .build();
    public static final int TEST_VALUE_X_POS = 7;
    public static final int TEST_VALUE_Y_POS = 9;
    public static final double TEST_VALUE = 42.42;

    private ResultMatrix uut;

    @Before
    public void setUp() throws Exception {
        uut = new ResultMatrix(TEST_COL_X, TEST_COL_Y, TEST_TARGET, TEST_INPUT_SCHEMA);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToCreateWithInvalidSchema() throws Exception {
        new ResultMatrix(TEST_COL_X, TEST_COL_Y, TEST_TARGET, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateWithInvalidTarget() throws Exception {
        new ResultMatrix(TEST_COL_X, TEST_COL_Y, "", TEST_INPUT_SCHEMA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateWithUnknownXColumnInSchema() throws Exception {
        new ResultMatrix("whatever", TEST_COL_Y, TEST_TARGET, TEST_INPUT_SCHEMA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToCreateWithUnknownYColumnInSchema() throws Exception {
        new ResultMatrix(TEST_COL_X, "whatever", TEST_TARGET, TEST_INPUT_SCHEMA);
    }

    @Test
    public void shouldCreateValidInstance() throws Exception {
        assertThat(uut.getX(), is(TEST_COL_X));
        assertThat(uut.getY(), is(TEST_COL_Y));
        assertThat(uut.getSchema(), is(TEST_INPUT_SCHEMA));
        assertThat(uut.getTarget(), is(TEST_TARGET));
        assertThat(uut.getColumnXSize(), is(TEST_X_SERIES.getSteps()));
        assertThat(uut.getColumnYSize(), is(TEST_Y_SERIES.getSteps()));
        assertThat(uut.getMinValue(), is(Double.NaN));
        assertThat(uut.getMaxValue(), is(Double.NaN));
        assertThat(uut.getYMin(), is(TEST_Y_SERIES.getMin()));
        assertThat(uut.getYMax(), is(TEST_Y_SERIES.getMax()));
        assertThat(uut.getXMin(), is(TEST_X_SERIES.getMin()));
        assertThat(uut.getXMax(), is(TEST_X_SERIES.getMax()));
    }

    @Test
    public void shouldReturnNullForInvalidGet() throws Exception {
        Row res = uut.get(-1, -1);

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldGetIfExists() throws Exception {
        Row row = createTestRow(TEST_VALUE);
        uut.update(row);

        assertThat(uut.get(TEST_VALUE_X_POS, TEST_VALUE_Y_POS), is(row));
    }

    @Test
    public void shouldReturnNanForInvalidGetValue() throws Exception {
        assertThat(uut.getValue(-1, -1), is(Double.NaN));
    }

    @Test
    public void shouldGetValueIfExists() throws Exception {
        Row row = createTestRow(TEST_VALUE);
        uut.update(row);

        assertThat(uut.getValue(TEST_VALUE_X_POS, TEST_VALUE_Y_POS), is(TEST_VALUE));
    }

    @Test
    public void shouldOverwriteExistingValueIfGreater() throws Exception {
        double updatedValue = TEST_VALUE + 1.0;
        Row row = createTestRow(TEST_VALUE);
        uut.update(row);
        Row updated = createTestRow(updatedValue);

        uut.update(updated);

        assertThat(uut.getValue(TEST_VALUE_X_POS, TEST_VALUE_Y_POS), is(updatedValue));
    }

    @Test
    public void shouldNotOverwriteExistingValueIfLesser() throws Exception {
        double updatedValue = TEST_VALUE - 1.0;
        Row row = createTestRow(TEST_VALUE);
        uut.update(row);
        Row updated = createTestRow(updatedValue);

        uut.update(updated);

        assertThat(uut.getValue(TEST_VALUE_X_POS, TEST_VALUE_Y_POS), is(TEST_VALUE));
    }

    @Test
    public void shouldReverse() throws Exception {
        Row row = createTestRow(TEST_VALUE);
        uut.update(row);

        ResultMatrix res = uut.reverse();
        // check value has changed place appropriately
        assertThat(res.getValue(TEST_VALUE_Y_POS, TEST_VALUE_X_POS), is(TEST_VALUE));
    }

    public static Row createTestRow(double testValue) {
        OutputRow outputRow = OutputRow.fromSchema(TEST_OUTPUT_SCHEMA);
        outputRow.setValue(TEST_TARGET, testValue);
        InputRow inputRow = InputRow.fromSchema(TEST_INPUT_SCHEMA);
        inputRow.setValue(TEST_COL_Y, TEST_VALUE_Y_POS);
        inputRow.setValue(TEST_COL_X, TEST_VALUE_X_POS);

        return Row.newInstance(inputRow, outputRow);
    }
}
