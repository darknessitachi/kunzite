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

import com.zaradai.kunzite.optimizer.model.Series;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class SeriesTest {
    private static final double TEST_MIN = 1.0;
    private static final double TEST_MAX = 5.0;
    private static final double TEST_STEP = 0.5;
    private static final double TEST_START = 1.0;
    private static final int TEST_NUM_STEPS = 9;
    private static final int TEST_HASH_CODE = -1366593218;
    private static final String TEST_STRING_VALUE = "Series{Min=1.0, Max=5.0, Steps=9}";

    private Series uut;

    @Before
    public void setUp() throws Exception {
        uut = Series.newMinMaxSeries(TEST_MIN, TEST_MAX, TEST_STEP);
    }

    @Test
    public void shouldConstructBasedOnMinMax() throws Exception {
        assertThat(uut.getSteps(), is(TEST_NUM_STEPS));
        assertThat(uut.getMin(), is(TEST_MIN));
        assertThat(uut.getMax(), is(TEST_MAX));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBasedOnStepsIfStepIsZero() throws Exception {
        Series uut = Series.newStepSeries(TEST_START, 0, TEST_NUM_STEPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBasedOnStepsIfStepsIsLessThanZero() throws Exception {
        Series uut = Series.newStepSeries(TEST_START, -1.0, TEST_NUM_STEPS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBasedMinMaxIfStepIsZero() throws Exception {
        Series uut = Series.newMinMaxSeries(TEST_MIN, TEST_MAX, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBasedMinMaxIfStepsIsLessThanZero() throws Exception {
        Series uut = Series.newMinMaxSeries(TEST_MIN, TEST_MAX, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBasedMinMaxIfMinGreaterThanMax() throws Exception {
        Series uut = Series.newMinMaxSeries(TEST_MAX, TEST_MIN, TEST_STEP);
    }

    @Test
    public void shouldConstructBasedOnSteps() throws Exception {
        Series uut = Series.newStepSeries(TEST_START, TEST_STEP, TEST_NUM_STEPS);

        assertThat(uut.getSteps(), is(TEST_NUM_STEPS));
        assertThat(uut.getMin(), is(TEST_START));
        assertThat(uut.getMax(), is(TEST_MAX));
    }

    @Test
    public void shouldGetValue() throws Exception {
        assertThat(uut.getValue(1), is(1.5));
    }

    @Test
    public void shouldGetStepIndex() throws Exception {
        assertThat(uut.getStepIndex(1.5), is(1));
    }

    @Test
    public void shouldReturnEdgeValueIfValueForIndexExceedsMax() throws Exception {
        double value = uut.getMax() + 1.0;
        assertThat(uut.getStepIndex(value), is(-1));
    }

    @Test
    public void shouldGetPreviousValue() throws Exception {
        assertThat(uut.getPreviousValue(3.7), is(3.5));
    }

    @Test
    public void shouldGetNextValue() throws Exception {
        assertThat(uut.getNextValue(3.7), is(4.0));
    }

    @Test
    public void shouldReturnIterator() throws Exception {
        assertThat(uut.iterator(), not(nullValue()));
    }

    @Test
    public void shouldGenerateHashCode() throws Exception {
        assertThat(uut.hashCode(), is(TEST_HASH_CODE));
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
    public void shouldNotBeEqualIfDifferentSeries() throws Exception {
        Series other = Series.newStepSeries(TEST_START, TEST_STEP, TEST_NUM_STEPS+1);

        assertThat(uut.equals(other), is(false));
    }

    @Test
    public void shouldBeEqualIfSameStepSeries() throws Exception {
        Series other = Series.newMinMaxSeries(TEST_MIN, TEST_MAX, TEST_STEP);

        assertThat(uut.equals(other), is(true));
    }

    @Test
    public void shouldProvideValidStringForToString() throws Exception {
        assertThat(uut.toString(), is(TEST_STRING_VALUE));

    }
}
