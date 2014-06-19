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
package com.zaradai.kunzite.trader.instruments;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class TickDefinitionTest {
    private static final double TEST_MIN = 12.34;
    private static final double TEST_MAX = 56.78;
    private static final double TEST_VALUE = 9.0;
    private static final int TEST_HASH_CODE = -204852602;
    private static final String TEST_STRING = "TickDefinition{min=12.34, max=56.78, value=9.0}";

    @Test
    public void shouldConstructWithAllArgs() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();

        assertThat(uut.getMin(), is(TEST_MIN));
        assertThat(uut.getMax(), is(TEST_MAX));
        assertThat(uut.getValue(), is(TEST_VALUE));
    }

    @Test
    public void shouldGetMin() throws Exception {
        TickDefinition uut = new TickDefinition();
        uut.setMin(TEST_MIN);

        assertThat(uut.getMin(), is(TEST_MIN));
    }

    @Test
    public void shouldGetMax() throws Exception {
        TickDefinition uut = new TickDefinition();
        uut.setMax(TEST_MAX);

        assertThat(uut.getMax(), is(TEST_MAX));
    }

    @Test
    public void shouldGetValue() throws Exception {
        TickDefinition uut = new TickDefinition();
        uut.setValue(TEST_VALUE);

        assertThat(uut.getValue(), is(TEST_VALUE));
    }

    @Test
    public void shouldCompareOnMinLessThan() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();
        TickDefinition more = new TickDefinition(TEST_MIN + 1.0, TEST_MAX, TEST_VALUE);

        assertThat(TickDefinition.TickComparator.compare(uut, more), lessThan(0));
    }

    @Test
    public void shouldCompareIfMinEqual() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();
        TickDefinition same = createDefinitionAllArgs();

        assertThat(TickDefinition.TickComparator.compare(uut, same), is(0));
    }

    @Test
    public void shouldCompareOnMinMoreThan() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();
        TickDefinition less = new TickDefinition(TEST_MIN - 1.0, TEST_MAX, TEST_VALUE);
        assertThat(TickDefinition.TickComparator.compare(uut, less), greaterThan(0));

    }

    @Test
    public void shouldHashCode() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();

        assertThat(uut.hashCode(), is(TEST_HASH_CODE));
    }

    @Test
    public void shouldEqualIfArgsSame() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();
        TickDefinition test = createDefinitionAllArgs();

        assertThat(uut.equals(test), is(true));
    }

    @Test
    public void shouldNotEqualIfArgsDifferent() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();
        TickDefinition test = new TickDefinition(TEST_VALUE, TEST_MIN, TEST_MAX);

        assertThat(uut.equals(test), is(false));
    }

    @Test
    public void shouldNotEqualIfNullOther() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();

        assertThat(uut.equals(null), is(false));
    }

    @Test
    public void shouldNotEqualIfOtherTypeDifferent() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();

        assertThat(uut.equals(new Object()), is(false));
    }

    @Test
    public void shouldToString() throws Exception {
        TickDefinition uut = createDefinitionAllArgs();

        assertThat(uut.toString(), is(TEST_STRING));
    }

    private TickDefinition createDefinitionAllArgs() {
        return new TickDefinition(TEST_MIN, TEST_MAX, TEST_VALUE);
    }
}
