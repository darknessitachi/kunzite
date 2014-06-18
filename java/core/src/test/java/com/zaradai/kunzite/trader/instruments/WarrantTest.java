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

public class WarrantTest {
    private static final double TEST_DOUBLE = 42.42;
    private static final long TEST_LONG = 42L;
    private static final String TEST_STRING = "test";
    private static final DateTime TEST_DATE_TIME = DateTime.now();

    private Warrant uut;
    private InstrumentResolver instrumentResolver;

    @Before
    public void setUp() throws Exception {
        instrumentResolver = mock(InstrumentResolver.class);
        uut = new Warrant(instrumentResolver);
    }

    @Test
    public void shouldBeWarrantType() throws Exception {
        assertThat(uut.getType(), is(InstrumentType.Warrant));
    }

    @Test
    public void shouldGetConversionRatio() throws Exception {
        uut.setConversionRatio(TEST_DOUBLE);

        assertThat(uut.getConversionRatio(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetIssueSize() throws Exception {
        uut.setIssueSize(TEST_LONG);

        assertThat(uut.getIssueSize(), is(TEST_LONG));
    }

    @Test
    public void shouldGetIssuer() throws Exception {
        uut.setIssuer(TEST_STRING);

        assertThat(uut.getIssuer(), is(TEST_STRING));
    }

    @Test
    public void shouldGetIssueDate() throws Exception {
        uut.setIssueDate(TEST_DATE_TIME);

        assertThat(uut.getIssueDate(), is(TEST_DATE_TIME));
    }
}
