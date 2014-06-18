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

public class InstrumentTest {
    private static final String TEST_STRING = "test";
    private static final int TEST_INT = 42;
    private static final double TEST_DOUBLE = 42.42;

    private Instrument uut;

    @Before
    public void setUp() throws Exception {
        uut = new Instrument() {
            @Override
            public InstrumentType getType() {
                return InstrumentType.Option;
            }
        };
    }

    @Test
    public void shouldGetId() throws Exception {
        uut.setId(TEST_STRING);

        assertThat(uut.getId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetName() throws Exception {
        uut.setName(TEST_STRING);

        assertThat(uut.getName(), is(TEST_STRING));
    }

    @Test
    public void shouldGetMultiplier() throws Exception {
        uut.setMultiplier(TEST_DOUBLE);

        assertThat(uut.getMultiplier(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetLotSize() throws Exception {
        uut.setLotSize(TEST_INT);

        assertThat(uut.getLotSize(), is(TEST_INT));
    }

    @Test
    public void shouldGetMarketId() throws Exception {
        uut.setMarketId(TEST_STRING);

        assertThat(uut.getMarketId(), is(TEST_STRING));
    }
}
