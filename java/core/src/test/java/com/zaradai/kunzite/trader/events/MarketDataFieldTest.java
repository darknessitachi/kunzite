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
package com.zaradai.kunzite.trader.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MarketDataFieldTest {
    private static final MarketDataFieldType TEST_TYPE = MarketDataFieldType.ASK4_SIZE;
    private static final double TEST_DOUBLE = 42.42;
    private static final long TEST_LONG = 42;

    @Test
    public void shouldCreateNewDoubleValue() throws Exception {
        MarketDataField uut = MarketDataField.newDoubleValue(TEST_TYPE, TEST_DOUBLE);

        assertThat(uut.getType(), is(TEST_TYPE));
        assertThat(uut.getDoubleValue(), is(TEST_DOUBLE));
        assertThat(uut.getLongValue(), is(0L));
    }


    @Test
    public void shouldCreateNewLongValue() throws Exception {
        MarketDataField uut = MarketDataField.newLongValue(TEST_TYPE, TEST_LONG);

        assertThat(uut.getType(), is(TEST_TYPE));
        assertThat(uut.getDoubleValue(), is(0.0));
        assertThat(uut.getLongValue(), is(TEST_LONG));
    }
}
