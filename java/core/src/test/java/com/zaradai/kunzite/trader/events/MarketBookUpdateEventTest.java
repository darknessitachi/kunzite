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

import com.zaradai.kunzite.trader.marketdata.MarketBook;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MarketBookUpdateEventTest {
    private static final boolean TEST_BEST_PRICE = true;
    private static final boolean TEST_BEST_SIZE = true;
    private static final MarketBook TEST_BOOK = mock(MarketBook.class);
    private static final boolean TEST_DEPTH = true;
    private static final String TEST_INST_ID = "test";
    private static final boolean TEST_LAST_TRADE = true;
    private static final boolean TEST_OHLC = true;
    private static final DateTime TEST_TIMESTAMP = DateTime.now();

    @Test
    public void shouldBuild() throws Exception {
        MarketBookUpdateEvent uut = MarketBookUpdateEvent.builder()
                .bestPrice(TEST_BEST_PRICE)
                .bestSize(TEST_BEST_SIZE)
                .book(TEST_BOOK)
                .depth(TEST_DEPTH)
                .instrument(TEST_INST_ID)
                .lastTrade(TEST_LAST_TRADE)
                .ohlc(TEST_OHLC)
                .timestamp(TEST_TIMESTAMP)
                .build();

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getMarketBook(), is(TEST_BOOK));
        assertThat(uut.getTimestamp(), is(TEST_TIMESTAMP));
        assertThat(uut.isBestPriceUpdated(), is(TEST_BEST_PRICE));
        assertThat(uut.isBestSizeUpdated(), is(TEST_BEST_SIZE));
        assertThat(uut.isDepthUpdated(), is(TEST_DEPTH));
        assertThat(uut.isLastTradeUpdated(), is(TEST_LAST_TRADE));
        assertThat(uut.isOhlcUpdated(), is(TEST_OHLC));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithInvalidInstrument() throws Exception {
        MarketBookUpdateEvent uut = MarketBookUpdateEvent.builder().build();
    }

    @Test
    public void shouldSetTimestampIfNotGiven() throws Exception {
        MarketBookUpdateEvent uut = MarketBookUpdateEvent.builder().instrument(TEST_INST_ID).build();

        assertThat(uut.getTimestamp(), not(nullValue()));
    }
}
