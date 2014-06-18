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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class BondTest {
    private static final DateTime TEST_DATE_TIME = DateTime.now();
    private static final double TEST_DOUBLE = 42.42;

    private Bond uut;
    private InstrumentResolver instrumentResolver;

    @Before
    public void setUp() throws Exception {
        instrumentResolver = mock(InstrumentResolver.class);
        uut = new Bond(instrumentResolver);
    }

    @Test
    public void shouldGetFirstCoupon() throws Exception {
        uut.setFirstCoupon(TEST_DATE_TIME);

        assertThat(uut.getFirstCoupon(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetCoupon() throws Exception {
        uut.setCoupon(TEST_DOUBLE);

        assertThat(uut.getCoupon(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetNotional() throws Exception {
        uut.setNotional(TEST_DOUBLE);

        assertThat(uut.getNotional(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetIssue() throws Exception {
        uut.setIssue(TEST_DATE_TIME);

        assertThat(uut.getIssue(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetMaturity() throws Exception {
        uut.setMaturity(TEST_DATE_TIME);

        assertThat(uut.getMaturity(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldBeBondType() throws Exception {
        assertThat(uut.getType(), is(InstrumentType.Bond));
    }
}
