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
package com.zaradai.kunzite.trader.marketdata;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MarketBookItemTest {
    private static final long TEST_SIZE = 42;
    private static final double TEST_PRICE = 42.42;

    @Test
    public void shouldCreateWithArgs() throws Exception {
        MarketBookItem uut = new MarketBookItem(TEST_SIZE, TEST_PRICE);

        assertThat(uut.getSize(), is(TEST_SIZE));
        assertThat(uut.getPrice(), is(TEST_PRICE));
    }

    @Test
    public void shouldGetSize() throws Exception {
        MarketBookItem uut = new MarketBookItem();

        uut.setSize(TEST_SIZE);

        assertThat(uut.getSize(), is(TEST_SIZE));
    }

    @Test
    public void shouldGetPrice() throws Exception {
        MarketBookItem uut = new MarketBookItem();

        uut.setPrice(TEST_PRICE);

        assertThat(uut.getPrice(), is(TEST_PRICE));
    }
}
