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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PairTest {
    private static final int TEST_X = 23;
    private static final int TEST_Y = 42;
    private static final int TEST_HASH_CODE = 1716;
    private static final String TEST_STRING = "Pair{X=23, Y=42}";

    @Test
    public void shouldCreateNewPair() throws Exception {
        Pair uut = createPair();

        assertThat(uut.getX(), is(TEST_X));
        assertThat(uut.getY(), is(TEST_Y));
    }

    @Test
    public void shouldGenerateHashCode() throws Exception {
        Pair uut = createPair();

        assertThat(uut.hashCode(), is(TEST_HASH_CODE));
    }

    @Test
    public void shouldEqualsIfSameValues() throws Exception {
        Pair uut = createPair();
        Pair other = createPair();

        assertThat(uut.equals(other), is(true));
    }

    @Test
    public void shouldNotEqualIfValuesDifferent() throws Exception {
        Pair uut = createPair();
        Pair other = Pair.newPair(TEST_X+1, TEST_Y);

        assertThat(uut.equals(other), is(false));
    }

    @Test
    public void shouldNotEqualIfOtherIsNull() throws Exception {
        Pair uut = createPair();

        assertThat(uut.equals(null), is(false));
    }

    @Test
    public void shouldNotEqualIfOtherIsDifferentType() throws Exception {
        Pair uut = createPair();

        assertThat(uut.equals(new Object()), is(false));
    }

    @Test
    public void shouldGenerateToString() throws Exception {
        Pair uut = createPair();

        assertThat(uut.toString(), is(TEST_STRING));
    }

    private Pair createPair() {
        return Pair.newPair(TEST_X, TEST_Y);
    }
}
