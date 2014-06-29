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

import com.google.common.collect.Lists;
import com.zaradai.kunzite.trader.events.MarketBookUpdateEvent;
import com.zaradai.kunzite.trader.events.MarketData;
import com.zaradai.kunzite.trader.events.MarketDataField;
import com.zaradai.kunzite.trader.events.MarketDataFieldType;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.InstrumentMocker;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DefaultMarketBookUpdaterTest {
    private static final String TEST_INST_ID = "test";
    private static final double TEST_PRICE = 52.45;
    private static final long TEST_QTY = 2345;
    private Instrument instrument;
    private DefaultMarketBook marketBook;
    private DefaultMarketBookUpdater uut;

    @Before
    public void setUp() throws Exception {
        instrument = InstrumentMocker.create(TEST_INST_ID);
        marketBook = new DefaultMarketBook(instrument);
        uut = new DefaultMarketBookUpdater();
    }

    @Test
    public void shouldUpdate() throws Exception {
        MarketData md = MarketData.newInstance(TEST_INST_ID, Lists.newArrayList(
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK2, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK3, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK4, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK5, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK6, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK7, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK8, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK9, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_ASK10, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID2, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID3, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID4, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID5, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID6, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID7, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID8, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID9, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.BEST_BID10, TEST_PRICE),
                MarketDataField.newLongValue(MarketDataFieldType.BID_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID2_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID3_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID4_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID5_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID6_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID7_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID8_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID9_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.BID10_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK2_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK3_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK4_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK5_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK6_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK7_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK8_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK9_SIZE, TEST_QTY),
                MarketDataField.newLongValue(MarketDataFieldType.ASK10_SIZE, TEST_QTY),
                MarketDataField.newDoubleValue(MarketDataFieldType.HIGH, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.LOW, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.OPEN, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.PREV_CLOSE, TEST_PRICE),
                MarketDataField.newDoubleValue(MarketDataFieldType.TRADE_PRICE, TEST_PRICE),
                MarketDataField.newLongValue(MarketDataFieldType.TRADE_SIZE, TEST_QTY)
                ));

        MarketBookUpdateEvent update = uut.update(marketBook, md);

        assertThat(marketBook.getBid(0), is(TEST_PRICE));
        assertThat(marketBook.getBid(1), is(TEST_PRICE));
        assertThat(marketBook.getBid(2), is(TEST_PRICE));
        assertThat(marketBook.getBid(3), is(TEST_PRICE));
        assertThat(marketBook.getBid(4), is(TEST_PRICE));
        assertThat(marketBook.getBid(5), is(TEST_PRICE));
        assertThat(marketBook.getBid(6), is(TEST_PRICE));
        assertThat(marketBook.getBid(7), is(TEST_PRICE));
        assertThat(marketBook.getBid(8), is(TEST_PRICE));
        assertThat(marketBook.getBid(9), is(TEST_PRICE));
        assertThat(marketBook.getAsk(0), is(TEST_PRICE));
        assertThat(marketBook.getAsk(1), is(TEST_PRICE));
        assertThat(marketBook.getAsk(2), is(TEST_PRICE));
        assertThat(marketBook.getAsk(3), is(TEST_PRICE));
        assertThat(marketBook.getAsk(4), is(TEST_PRICE));
        assertThat(marketBook.getAsk(5), is(TEST_PRICE));
        assertThat(marketBook.getAsk(6), is(TEST_PRICE));
        assertThat(marketBook.getAsk(7), is(TEST_PRICE));
        assertThat(marketBook.getAsk(8), is(TEST_PRICE));
        assertThat(marketBook.getAsk(9), is(TEST_PRICE));
        assertThat(marketBook.getBidSize(0), is(TEST_QTY));
        assertThat(marketBook.getBidSize(1), is(TEST_QTY));
        assertThat(marketBook.getBidSize(2), is(TEST_QTY));
        assertThat(marketBook.getBidSize(3), is(TEST_QTY));
        assertThat(marketBook.getBidSize(4), is(TEST_QTY));
        assertThat(marketBook.getBidSize(5), is(TEST_QTY));
        assertThat(marketBook.getBidSize(6), is(TEST_QTY));
        assertThat(marketBook.getBidSize(7), is(TEST_QTY));
        assertThat(marketBook.getBidSize(8), is(TEST_QTY));
        assertThat(marketBook.getBidSize(9), is(TEST_QTY));
        assertThat(marketBook.getAskSize(0), is(TEST_QTY));
        assertThat(marketBook.getAskSize(1), is(TEST_QTY));
        assertThat(marketBook.getAskSize(2), is(TEST_QTY));
        assertThat(marketBook.getAskSize(3), is(TEST_QTY));
        assertThat(marketBook.getAskSize(4), is(TEST_QTY));
        assertThat(marketBook.getAskSize(5), is(TEST_QTY));
        assertThat(marketBook.getAskSize(6), is(TEST_QTY));
        assertThat(marketBook.getAskSize(7), is(TEST_QTY));
        assertThat(marketBook.getAskSize(8), is(TEST_QTY));
        assertThat(marketBook.getAskSize(9), is(TEST_QTY));

        assertThat(update.getMarketBook(), is((MarketBook) marketBook));
        assertThat(update.isDepthUpdated(), is(true));
        assertThat(update.isBestPriceUpdated(), is(true));
        assertThat(update.isBestSizeUpdated(), is(true));
        assertThat(update.isOhlcUpdated(), is(true));
        assertThat(update.isLastTradeUpdated(), is(true));
    }
}
