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
package com.zaradai.kunzite.trader.positions;

import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.trader.events.*;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.InstrumentMocker;
import com.zaradai.kunzite.trader.mocks.PortfolioMocker;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultPositionUpdaterTest {
    private static final String TEST_PORTFOLIO_ID = "ptr";
    private static final String TEST_INS_ID = "ins";
    private static final long TEST_LONG_POS = 4000;
    private static final long TEST_SHORT_POS = -2500;
    private static final double TEST_MULTIPLIER = 1.0;
    private static final double TEST_PRICE = 12.5;
    private static final double TEST_LONG_CASH = TEST_LONG_POS * TEST_MULTIPLIER * TEST_PRICE;
    private static final double TEST_SHORT_CASH = TEST_SHORT_POS * TEST_MULTIPLIER * TEST_PRICE;
    private static final long TEST_START_POS = 9500;
    private static final double TEST_START_CASH = 3400.5;
    private static final DateTime TEST_DATE_TIME = DateTime.now();
    private static final double TEST_ENTRY_PRICE = 23.45;
    private static final DateTime TEST_OPENED_DATE_TIME = DateTime.now();

    private Position position;
    private Portfolio portfolio;
    private Instrument instrument;
    private DefaultPositionUpdater uut;
    private EventAggregator eventAggregator;

    @Captor
    ArgumentCaptor<PositionInitiatedEvent> initiatedCaptor;
    @Captor
    ArgumentCaptor<PositionChangedEvent> changedCaptor;
    @Captor
    ArgumentCaptor<PositionLiquidatedEvent> liquidatedCaptor;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        eventAggregator = mock(EventAggregator.class);
        portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        instrument = InstrumentMocker.create(TEST_INS_ID);
        when(instrument.getMultiplier()).thenReturn(TEST_MULTIPLIER);
        position = new Position(portfolio, instrument);
        uut = new DefaultPositionUpdater(eventAggregator);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailUpdateTradeWithInvalidPosition() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, 10, 10.0, TEST_DATE_TIME);

        uut.update(null, testTrade);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailUpdateTradeWithInvalidTrade() throws Exception {
        uut.update(position, (TradeEvent) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateTradeWithInvalidInstrumentId() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PORTFOLIO_ID, "Unknown", 10, 10.0, TEST_DATE_TIME);

        uut.update(position, testTrade);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateStartWithInvalidPortfolioId() throws Exception {
        StartOfDay startOfDay = StartOfDay.newStartOfDay(null, TEST_INS_ID, TEST_START_POS, TEST_START_CASH,
                TEST_ENTRY_PRICE, TEST_OPENED_DATE_TIME);

        uut.update(position, startOfDay);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailUpdateStartWithInvalidPosition() throws Exception {
        StartOfDay startOfDay = StartOfDay.newStartOfDay(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_START_POS,
                TEST_START_CASH, TEST_ENTRY_PRICE, TEST_OPENED_DATE_TIME);

        uut.update(null, startOfDay);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailUpdateStartWithInvalidTrade() throws Exception {
        uut.update(position, (StartOfDay) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateStartWithInvalidInstrumentId() throws Exception {
        StartOfDay startOfDay = StartOfDay.newStartOfDay(TEST_PORTFOLIO_ID, null, TEST_START_POS, TEST_START_CASH,
                TEST_ENTRY_PRICE, TEST_OPENED_DATE_TIME);

        uut.update(position, startOfDay);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateTradeWithInvalidPortfolioId() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade("Unknown", TEST_INS_ID, 10, 10.0, TEST_DATE_TIME);

        uut.update(position, testTrade);
    }


    @Test
    public void shouldUpdateLong() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_LONG_POS, TEST_PRICE,
                TEST_DATE_TIME);

        uut.update(position, testTrade);

        assertThat(position.getNet(), is(TEST_LONG_POS));
        assertThat(position.getNetCashFlow(), is(TEST_LONG_CASH));
        assertThat(position.isLong(), is(true));
    }

    @Test
    public void shouldUpdateShort() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_SHORT_POS, TEST_PRICE,
                TEST_DATE_TIME);

        uut.update(position, testTrade);

        assertThat(position.getNet(), is(TEST_SHORT_POS));
        assertThat(position.getNetCashFlow(), is(TEST_SHORT_CASH));
        assertThat(position.isLong(), is(false));
    }

    @Test
    public void shouldUpdateStartOfDay() throws Exception {
        StartOfDay startOfDay = StartOfDay.newStartOfDay(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_START_POS,
                TEST_START_CASH, TEST_ENTRY_PRICE, TEST_OPENED_DATE_TIME);
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_SHORT_POS, TEST_PRICE,
                TEST_DATE_TIME);
        uut.update(position, testTrade);

        uut.update(position, startOfDay);

        assertThat(position.getStartOfDay(), is(TEST_START_POS));
        assertThat(position.getStartOfDayCashFlow(), is(TEST_START_CASH));
        assertThat(position.getIntradayLong(), is(0L));
        assertThat(position.getIntradayShort(), is(0L));
        assertThat(position.getIntradayLongCashFlow(), is(0.0));
        assertThat(position.getIntradayShortCashFlow(), is(0.0));
        assertThat(position.getNet(), is(TEST_START_POS));
        assertThat(position.getNetCashFlow(), is(TEST_START_CASH));
    }

    @Test
    public void shouldInitiateAPosition() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_LONG_POS, TEST_PRICE,
                TEST_DATE_TIME);

        uut.update(position, testTrade);

        assertThat(position.getEntryPrice(), is(TEST_PRICE));
        assertThat(position.getOpened(), is(TEST_DATE_TIME));
        assertThat(position.isActive(), is(true));

        verify(eventAggregator).publish(initiatedCaptor.capture());
        assertThat(initiatedCaptor.getValue().getPosition(), is(position));
    }

    @Test
    public void shouldChangeAPosition() throws Exception {
        TradeEvent initial = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_LONG_POS, TEST_PRICE,
                TEST_DATE_TIME);
        uut.update(position, initial);
        // do an update
        TradeEvent update = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_LONG_POS, TEST_PRICE,
                TEST_DATE_TIME);

        uut.update(position, update);

        assertThat(position.getEntryPrice(), is(TEST_PRICE));
        assertThat(position.getOpened(), is(TEST_DATE_TIME));
        assertThat(position.isActive(), is(true));
        assertThat(position.getNet(), is(TEST_LONG_POS*2));

        verify(eventAggregator, times(2)).publish(changedCaptor.capture());
        assertThat(changedCaptor.getValue().getPosition(), is(position));
    }

    @Test
    public void shouldLiquidateAPosition() throws Exception {
        TradeEvent initial = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_LONG_POS, TEST_PRICE,
                TEST_DATE_TIME);
        uut.update(position, initial);
        // do an update
        TradeEvent update = TradeEvent.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, -TEST_LONG_POS, TEST_PRICE,
                TEST_DATE_TIME);

        uut.update(position, update);

        assertThat(position.isActive(), is(false));
        assertThat(position.getNet(), is(0L));

        verify(eventAggregator, times(2)).publish(liquidatedCaptor.capture());
        assertThat(liquidatedCaptor.getValue().getPosition(), is(position));
    }
}
