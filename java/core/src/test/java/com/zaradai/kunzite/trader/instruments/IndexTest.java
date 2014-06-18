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
import static org.mockito.Mockito.when;

public class IndexTest {
    private static final String TEST_ID = "test";

    private Index uut;
    private InstrumentResolver instrumentResolver;

    @Before
    public void setUp() throws Exception {
        instrumentResolver = mock(InstrumentResolver.class);
        uut = new Index(instrumentResolver);
    }

    @Test
    public void shouldGetMembers() throws Exception {
        Stock testInstrument = createStock();
        when(instrumentResolver.resolveInstrument(TEST_ID)).thenReturn(testInstrument);

        uut.addToIndex(testInstrument);
        uut.addToIndex(TEST_ID);

        assertThat(uut.getIndexMembers().size(), is(2));
    }


    @Test
    public void shouldBeIndexType() throws Exception {
        assertThat(uut.getType(), is(InstrumentType.Index));
    }

    private Stock createStock() {
        return new Stock(instrumentResolver);
    }
}
