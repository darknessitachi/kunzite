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
package com.zaradai.kunzite.trader.control;

import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.marketdata.MarketBook;
import com.zaradai.kunzite.trader.marketdata.MarketBookFactory;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.book.OrderBookFactory;
import com.zaradai.kunzite.trader.positions.PositionBook;
import com.zaradai.kunzite.trader.positions.PositionBookFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InstrumentTradingStateTest {
    private Instrument instrument;
    private MarketBook marketBook;
    private InstrumentTradingState uut;
    private PositionBook positionBook;
    private OrderBook orderBook;

    @Before
    public void setUp() throws Exception {
        instrument = mock(Instrument.class);
        MarketBookFactory marketBookFactory = mock(MarketBookFactory.class);
        marketBook = mock(MarketBook.class);
        when(marketBookFactory.create(instrument)).thenReturn(marketBook);
        PositionBookFactory positionBookFactory = mock(PositionBookFactory.class);
        positionBook = mock(PositionBook.class);
        when(positionBookFactory.create(instrument)).thenReturn(positionBook);
        OrderBookFactory orderBookFactory = mock(OrderBookFactory.class);
        orderBook = mock(OrderBook.class);
        when(orderBookFactory.create()).thenReturn(orderBook);

        uut = new InstrumentTradingState(marketBookFactory, positionBookFactory, orderBookFactory, instrument);
    }

    @Test
    public void shouldGetInstrument() throws Exception {
        assertThat(uut.getInstrument(), is(instrument));
    }

    @Test
    public void shouldGetMarketBook() throws Exception {
        assertThat(uut.getMarketBook(), is(marketBook));
    }

    @Test
    public void shouldGetPositionBook() throws Exception {
        assertThat(uut.getPositionBook(), is(positionBook));
    }

    @Test
    public void shouldGetOrderBook() throws Exception {
        assertThat(uut.getOrderBook(), is(orderBook));
    }
}
