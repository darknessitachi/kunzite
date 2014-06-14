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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InputRowTest {
    private static final String TEST_1 = "test1";
    private static final String TEST_2 = "test2";
    private static final String TEST_3 = "test3";

    private static final String TEST_STRING = "InputRow{test1=[4]3.0, test2=[2]2.0, test3=[6]4.0}";
    private static final int TEST_HASH_CODE = 33703;
    public static final int TEST_VALUE_1 = 4;
    public static final int TEST_VALUE_2 = 2;
    private static final int TEST_VALUE_3 = 6;

    private InputRow uut;

    @Before
    public void setUp() throws Exception {
        uut = InputRow.fromSchema(
                InputRowSchema.newBuilder()
                        .withName(TEST_1).from(1.0).until(10.0).withStep(0.5)
                        .withName(TEST_2).from(1.0).until(10.0).withStep(0.5)
                        .withName(TEST_3).from(1.0).until(10.0).withStep(0.5)
                        .build());
        // give the test some starting values
        uut.setValue(0, TEST_VALUE_1);
        uut.setValue(1, TEST_VALUE_2);
        uut.setValue(2, TEST_VALUE_3);
    }

    @Test
    public void shouldGenerateString() throws Exception {
        assertThat(uut.toString(), is(TEST_STRING));
    }

    @Test
    public void shouldGenerateHashCode() throws Exception {
        assertThat(uut.hashCode(), is(TEST_HASH_CODE));
    }

    @Test
    public void shouldGenerateClone() throws Exception {
        InputRow cloned = (InputRow) uut.clone();

        assertThat(cloned, not(nullValue()));
        assertThat(uut.equals(cloned), is(true));
    }

    @Test
    public void shouldNotBeEqualIfDifferentValues() throws Exception {
        InputRow cloned = (InputRow) uut.clone();
        // make a change
        cloned.setValue(0, 8);

        assertThat(uut.equals(cloned), is(false));
    }

    @Test
    public void shouldFailEqualityOnNull() throws Exception {
        assertThat(uut.equals(null), is(false));
    }

    @Test
    public void shouldFailEqualityOnOtherType() throws Exception {
        assertThat(uut.equals(new Object()), is(false));
    }

    @Test
    public void shouldGetValueByName() throws Exception {
        assertThat(uut.getValue(TEST_1), is(TEST_VALUE_1));
        assertThat(uut.getValue(TEST_2), is(TEST_VALUE_2));
        assertThat(uut.getValue(TEST_3), is(TEST_VALUE_3));
    }

    @Test
    public void shouldGetValueByIndex() throws Exception {
        assertThat(uut.getValue(0), is(TEST_VALUE_1));
        assertThat(uut.getValue(1), is(TEST_VALUE_2));
        assertThat(uut.getValue(2), is(TEST_VALUE_3));
    }

    @Test
    public void shouldGetNumberOfColumns() throws Exception {
        assertThat(uut.getNumColumns(), is(3));
    }

    @Test
    public void shouldGetInputValueByName() throws Exception {
        assertThat(uut.getInputValue(TEST_1), is(3.0));
        assertThat(uut.getInputValue(TEST_2), is(2.0));
        assertThat(uut.getInputValue(TEST_3), is(4.0));
    }

    @Test
    public void shouldGetInputValueByIndex() throws Exception {
        assertThat(uut.getInputValue(0), is(3.0));
        assertThat(uut.getInputValue(1), is(2.0));
        assertThat(uut.getInputValue(2), is(4.0));
    }

    @Test
    public void shouldSetValueByName() throws Exception {
        int newValue = 5;
        uut.setValue(TEST_1, newValue);

        assertThat(uut.getValue(TEST_1), is(newValue));
    }

    @Test
    public void shouldSetValueByIndex() throws Exception {
        int newValue = 5;
        uut.setValue(0, newValue);

        assertThat(uut.getValue(0), is(newValue));
    }

    @Test(expected = NullPointerException.class)
    public void shouldCatchInvalidConstructFromNullSchema() throws Exception {
        InputRow.fromSchema(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldCatchInvalidGetValueByIndex() throws Exception {
        uut.getValue(24);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldCatchInvalidSetValueByIndex() throws Exception {
        uut.setValue(24, 0);
    }
}
