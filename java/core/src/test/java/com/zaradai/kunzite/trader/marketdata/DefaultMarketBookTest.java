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

import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.InstrumentMocker;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DefaultMarketBookTest {
    private static final String TEST_INST_ID = "test";
    private static final double TEST_PRICE = 52.45;
    private static final long TEST_QTY = 2345;
    private Instrument instrument;
    private DefaultMarketBook uut;

    @Before
    public void setUp() throws Exception {
        instrument = InstrumentMocker.create(TEST_INST_ID);
        uut = new DefaultMarketBook(instrument);
    }

    @Test
    public void shouldGetInstrumentId() throws Exception {
        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
    }

    @Test
    public void shouldGetLastTrade() throws Exception {
        assertThat(uut.getLastTrade(), not(nullValue()));
        assertThat(uut.getLastTradedPrice(), is(0.0));
        assertThat(uut.getLastTradedSize(), is(0L));
    }

    @Test
    public void shouldGetBids() throws Exception {
        uut.setPrice(Side.Bid, 0, TEST_PRICE);
        uut.setPrice(Side.Bid, 1, TEST_PRICE);
        uut.setPrice(Side.Bid, 2, TEST_PRICE);
        uut.setPrice(Side.Bid, 3, TEST_PRICE);
        uut.setPrice(Side.Bid, 4, TEST_PRICE);
        uut.setPrice(Side.Bid, 5, TEST_PRICE);
        uut.setPrice(Side.Bid, 6, TEST_PRICE);
        uut.setPrice(Side.Bid, 7, TEST_PRICE);
        uut.setPrice(Side.Bid, 8, TEST_PRICE);
        uut.setPrice(Side.Bid, 9, TEST_PRICE);
        uut.setSize(Side.Bid, 0, TEST_QTY);
        uut.setSize(Side.Bid, 1, TEST_QTY);
        uut.setSize(Side.Bid, 2, TEST_QTY);
        uut.setSize(Side.Bid, 3, TEST_QTY);
        uut.setSize(Side.Bid, 4, TEST_QTY);
        uut.setSize(Side.Bid, 5, TEST_QTY);
        uut.setSize(Side.Bid, 6, TEST_QTY);
        uut.setSize(Side.Bid, 7, TEST_QTY);
        uut.setSize(Side.Bid, 8, TEST_QTY);
        uut.setSize(Side.Bid, 9, TEST_QTY);

        assertThat(uut.bestBid(), is(TEST_PRICE));
        assertThat(uut.bestBidSize(), is(TEST_QTY));
        assertThat(uut.getBid(0), is(TEST_PRICE));
        assertThat(uut.getBid(1), is(TEST_PRICE));
        assertThat(uut.getBid(2), is(TEST_PRICE));
        assertThat(uut.getBid(3), is(TEST_PRICE));
        assertThat(uut.getBid(4), is(TEST_PRICE));
        assertThat(uut.getBid(5), is(TEST_PRICE));
        assertThat(uut.getBid(6), is(TEST_PRICE));
        assertThat(uut.getBid(7), is(TEST_PRICE));
        assertThat(uut.getBid(8), is(TEST_PRICE));
        assertThat(uut.getBid(9), is(TEST_PRICE));
        assertThat(uut.getBidSize(0), is(TEST_QTY));
        assertThat(uut.getBidSize(1), is(TEST_QTY));
        assertThat(uut.getBidSize(2), is(TEST_QTY));
        assertThat(uut.getBidSize(3), is(TEST_QTY));
        assertThat(uut.getBidSize(4), is(TEST_QTY));
        assertThat(uut.getBidSize(5), is(TEST_QTY));
        assertThat(uut.getBidSize(6), is(TEST_QTY));
        assertThat(uut.getBidSize(7), is(TEST_QTY));
        assertThat(uut.getBidSize(8), is(TEST_QTY));
        assertThat(uut.getBidSize(9), is(TEST_QTY));
    }

    @Test
    public void shouldGetAsks() throws Exception {
        uut.setPrice(Side.Ask, 0, TEST_PRICE);
        uut.setPrice(Side.Ask, 1, TEST_PRICE);
        uut.setPrice(Side.Ask, 2, TEST_PRICE);
        uut.setPrice(Side.Ask, 3, TEST_PRICE);
        uut.setPrice(Side.Ask, 4, TEST_PRICE);
        uut.setPrice(Side.Ask, 5, TEST_PRICE);
        uut.setPrice(Side.Ask, 6, TEST_PRICE);
        uut.setPrice(Side.Ask, 7, TEST_PRICE);
        uut.setPrice(Side.Ask, 8, TEST_PRICE);
        uut.setPrice(Side.Ask, 9, TEST_PRICE);
        uut.setSize(Side.Ask, 0, TEST_QTY);
        uut.setSize(Side.Ask, 1, TEST_QTY);
        uut.setSize(Side.Ask, 2, TEST_QTY);
        uut.setSize(Side.Ask, 3, TEST_QTY);
        uut.setSize(Side.Ask, 4, TEST_QTY);
        uut.setSize(Side.Ask, 5, TEST_QTY);
        uut.setSize(Side.Ask, 6, TEST_QTY);
        uut.setSize(Side.Ask, 7, TEST_QTY);
        uut.setSize(Side.Ask, 8, TEST_QTY);
        uut.setSize(Side.Ask, 9, TEST_QTY);

        assertThat(uut.bestAsk(), is(TEST_PRICE));
        assertThat(uut.bestAskSize(), is(TEST_QTY));
        assertThat(uut.getAsk(0), is(TEST_PRICE));
        assertThat(uut.getAsk(1), is(TEST_PRICE));
        assertThat(uut.getAsk(2), is(TEST_PRICE));
        assertThat(uut.getAsk(3), is(TEST_PRICE));
        assertThat(uut.getAsk(4), is(TEST_PRICE));
        assertThat(uut.getAsk(5), is(TEST_PRICE));
        assertThat(uut.getAsk(6), is(TEST_PRICE));
        assertThat(uut.getAsk(7), is(TEST_PRICE));
        assertThat(uut.getAsk(8), is(TEST_PRICE));
        assertThat(uut.getAsk(9), is(TEST_PRICE));
        assertThat(uut.getAskSize(0), is(TEST_QTY));
        assertThat(uut.getAskSize(1), is(TEST_QTY));
        assertThat(uut.getAskSize(2), is(TEST_QTY));
        assertThat(uut.getAskSize(3), is(TEST_QTY));
        assertThat(uut.getAskSize(4), is(TEST_QTY));
        assertThat(uut.getAskSize(5), is(TEST_QTY));
        assertThat(uut.getAskSize(6), is(TEST_QTY));
        assertThat(uut.getAskSize(7), is(TEST_QTY));
        assertThat(uut.getAskSize(8), is(TEST_QTY));
        assertThat(uut.getAskSize(9), is(TEST_QTY));
    }

    @Test
    public void shouldHandleOutOfRange() throws Exception {
        uut.setPrice(Side.Bid, -1, TEST_PRICE);
        uut.setPrice(Side.Bid, 10, TEST_PRICE);
        uut.setPrice(Side.Ask, -1, TEST_PRICE);
        uut.setPrice(Side.Ask, 10, TEST_PRICE);
        uut.setSize(Side.Bid, -1, TEST_QTY);
        uut.setSize(Side.Bid, 10, TEST_QTY);
        uut.setSize(Side.Ask, -1, TEST_QTY);
        uut.setSize(Side.Ask, 10, TEST_QTY);

        assertThat(uut.getAskSize(10), is(0L));
        assertThat(uut.getBidSize(10), is(0L));
        assertThat(uut.getAskSize(-1), is(0L));
        assertThat(uut.getBidSize(-1), is(0L));
        assertThat(uut.getAsk(10), is(0.0));
        assertThat(uut.getBid(10), is(0.0));
        assertThat(uut.getAsk(-1), is(0.0));
        assertThat(uut.getBid(-1), is(0.0));
    }

    @Test
    public void shouldGetDepth() throws Exception {
        uut.setPrice(Side.Ask, 0, TEST_PRICE);
        uut.setPrice(Side.Ask, 1, TEST_PRICE);
        uut.setPrice(Side.Ask, 2, TEST_PRICE);
        uut.setPrice(Side.Bid, 0, TEST_PRICE);
        uut.setPrice(Side.Bid, 1, TEST_PRICE);
        uut.setPrice(Side.Bid, 2, TEST_PRICE);
        uut.setPrice(Side.Bid, 3, TEST_PRICE);
        uut.setPrice(Side.Bid, 4, TEST_PRICE);
        uut.setPrice(Side.Bid, 5, TEST_PRICE);

        assertThat(uut.getAskDepth(), is(3));
        assertThat(uut.getBidDepth(), is(6));
    }

    @Test
    public void shouldGetOhlc() throws Exception {
        uut.setOpen(TEST_PRICE);
        uut.setHigh(TEST_PRICE);
        uut.setLow(TEST_PRICE);
        uut.setPrevClose(TEST_PRICE);

        assertThat(uut.getOpen(), is(TEST_PRICE));
        assertThat(uut.getHigh(), is(TEST_PRICE));
        assertThat(uut.getLow(), is(TEST_PRICE));
        assertThat(uut.getPrevClose(), is(TEST_PRICE));
    }

    @Test
    public void shouldSetRandomDepth() throws Exception {
        uut.setSize(Side.Bid, 5, TEST_QTY);
        uut.setSize(Side.Ask, 5, TEST_QTY);
        uut.setPrice(Side.Bid, 5, TEST_PRICE);
        uut.setPrice(Side.Ask, 5, TEST_PRICE);

        assertThat(uut.getAsk(5), is(TEST_PRICE));
        assertThat(uut.getBid(5), is(TEST_PRICE));
        assertThat(uut.getAskSize(5), is(TEST_QTY));
        assertThat(uut.getBidSize(5), is(TEST_QTY));
    }

    @Test
    public void shouldResetData() throws Exception {
        uut.setSize(Side.Bid, 0, TEST_QTY);
        uut.setSize(Side.Bid, 1, TEST_QTY);
        uut.setSize(Side.Bid, 2, TEST_QTY);
        uut.setPrice(Side.Bid, 0, TEST_PRICE);
        uut.setPrice(Side.Bid, 1, TEST_PRICE);
        uut.setPrice(Side.Bid, 2, TEST_PRICE);
        uut.reset();

        assertThat(uut.getBid(0), is(0.0));
        assertThat(uut.getBidSize(0), is(0L));
    }
}
