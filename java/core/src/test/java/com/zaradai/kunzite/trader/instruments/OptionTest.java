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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class OptionTest {
    private static final OptionType TEST_OPTION_TEST = OptionType.Call;
    private static final double TEST_STRIKE = 42.42;
    private Option uut;
    private InstrumentResolver instrumentResolver;

    @Before
    public void setUp() throws Exception {
        instrumentResolver = mock(InstrumentResolver.class);
        uut = new Option(instrumentResolver);
    }

    @Test
    public void shouldGetOptionType() throws Exception {
        uut.setOptionType(TEST_OPTION_TEST);

        assertThat(uut.getOptionType(), is(TEST_OPTION_TEST));
    }

    @Test
    public void shouldGetStrike() throws Exception {
        uut.setStrike(TEST_STRIKE);

        assertThat(uut.getStrike(), is(TEST_STRIKE));
    }

    @Test
    public void shouldBeOptionType() throws Exception {
        assertThat(uut.getType(), is(InstrumentType.Option));
    }
}
