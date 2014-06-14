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

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class InputRowGeneratorTest {
    private static final String COL_NAME_1 = "col1";
    private static final String COL_NAME_2 = "col2";
    private static final String COL_NAME_3 = "col3";

    @Test
    public void shouldGetFirst() throws Exception {
        InputRow row = createInputRow();
        InputRow uut = InputRowGenerator.getFirst(row.getSchema());

        assertThat(uut.getValue(0), is(0));
        assertThat(uut.getValue(1), is(0));
        assertThat(uut.getValue(2), is(0));
    }

    @Test
    public void shouldGetRandom() throws Exception {
        InputRow row = createInputRow();
        InputRow uut = InputRowGenerator.getRandom(row.getSchema());

        assertThat(uut, not(nullValue()));
    }

    @Test
    public void shouldGetNext() throws Exception {
        InputRow row = createInputRow();
        InputRow uut = InputRowGenerator.getNext(row);

        assertThat(uut.getValue(0), is(1));
        assertThat(uut.getValue(1), is(0));
        assertThat(uut.getValue(2), is(0));
    }

    @Test
    public void shouldWrapGetNext() throws Exception {
        InputRow row = createInputRow();
        setRowValues(row, 18, 18, 18);

        InputRow uut = InputRowGenerator.getNext(row);

        assertThat(uut.getValue(0), is(0));
        assertThat(uut.getValue(1), is(0));
        assertThat(uut.getValue(2), is(0));
    }

    @Test
    public void shouldGetPrev() throws Exception {
        InputRow row = createInputRow();
        setRowValues(row, 0, 5, 5);

        InputRow uut = InputRowGenerator.getPrev(row);

        assertThat(uut.getValue(0), is(18));
        assertThat(uut.getValue(1), is(4));
        assertThat(uut.getValue(2), is(5));
    }

    @Test
    public void shouldWrapGetPrev() throws Exception {
        InputRow row = createInputRow();
        setRowValues(row, 0, 0, 0);

        InputRow uut = InputRowGenerator.getPrev(row);

        assertThat(uut.getValue(0), is(18));
        assertThat(uut.getValue(1), is(18));
        assertThat(uut.getValue(2), is(18));
    }

    @Test
    public void shouldGetNeighbours() throws Exception {
        InputRow row = createInputRow();
        setRowValues(row, 5, 5, 5);

        List<InputRow> res = InputRowGenerator.getNeighbours(row);
        assertThat(res.size(), is(27));
        // no need to test values as the NeighboursInputRowBuilder will be tested by its unit tests
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
