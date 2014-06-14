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

import com.zaradai.kunzite.optimizer.model.InputSpec;
import com.zaradai.kunzite.optimizer.model.Series;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InputSpecTest {
    private static final int TEST_POSITION = 0;
    private static final int TEST_HASH_CODE = -1301302555;
    private static final String TEST_STRING = "InputSpec{Pos=0, Series{Min=0.0, Max=0.8999999999999999, Steps=10}}";
    private InputSpec uut;
    private Series series;

    @Before
    public void setUp() throws Exception {
        series = Series.newStepSeries(0, 0.1, 10);
        uut = InputSpec.newInstance(TEST_POSITION, series);
    }

    @Test
    public void shouldCreateNewInstance() throws Exception {
        assertThat(uut, not(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateWithInvalidPosition() throws Exception {
        uut = InputSpec.newInstance(-1, series);
    }

    @Test
    public void shouldReturnValues() throws Exception {
        assertThat(uut.getPosition(), is(TEST_POSITION));
        assertThat(uut.getSeries(), is(series));
    }

    @Test
    public void shouldGenerateHashCode() throws Exception {
        assertThat(uut.hashCode(), is(TEST_HASH_CODE));
    }

    @Test
    public void shouldEqualIfSame() throws Exception {
        InputSpec other = InputSpec.newInstance(TEST_POSITION, series);

        assertThat(uut.equals(other), is(true));
    }

    @Test
    public void shouldNotEqualWithDifferentPosition() throws Exception {
        InputSpec other = InputSpec.newInstance(TEST_POSITION + 1, series);

        assertThat(uut.equals(other), is(false));
    }

    @Test
    public void shouldNotBeEqualIfOneIsNull() throws Exception {
        assertThat(uut.equals(null), is(false));
    }

    @Test
    public void shouldNotBeEqualIfDifferentType() throws Exception {
        assertThat(uut.equals(new Object()), is(false));
    }

    @Test
    public void shouldGenerateString() throws Exception {
        assertThat(uut.toString(), is(TEST_STRING));
    }
}
