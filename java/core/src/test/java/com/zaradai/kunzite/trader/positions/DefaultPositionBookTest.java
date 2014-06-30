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

import com.zaradai.kunzite.trader.events.StartOfDay;
import com.zaradai.kunzite.trader.events.TradeEvent;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.InstrumentMocker;
import com.zaradai.kunzite.trader.mocks.PortfolioMocker;
import com.zaradai.kunzite.trader.mocks.PositionMocker;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultPositionBookTest {
    private static final String TEST_INST_ID = "test";
    private static final String TEST_PTF_ID = "ptf";
    private static final long TEST_NET_POS = 2500L;
    private static final double TEST_NET_CASH = 25.55;

    private PositionFactory positionFactory;
    private PortfolioResolver portfolioResolver;
    private Instrument instrument;
    private DefaultPositionBook uut;
    private Portfolio portfolio;
    private Position position;
    private PositionUpdater positionUpdater;

    @Before
    public void setUp() throws Exception {
        positionFactory = mock(PositionFactory.class);
        portfolioResolver = mock(PortfolioResolver.class);
        instrument = InstrumentMocker.create(TEST_INST_ID);
        portfolio = PortfolioMocker.create(TEST_PTF_ID);
        position = PositionMocker.create(TEST_PTF_ID, TEST_INST_ID);
        when(portfolioResolver.resolvePortfolio(TEST_PTF_ID)).thenReturn(portfolio);
        when(positionFactory.create(portfolio, instrument)).thenReturn(position);
        positionUpdater = mock(PositionUpdater.class);

        uut = new DefaultPositionBook(positionFactory, positionUpdater, portfolioResolver, instrument);
    }

    @Test
    public void shouldAlwaysGetPosition() throws Exception {
        Position res = uut.getPositionFor(TEST_PTF_ID);

        assertThat(res, is(position));
    }

    @Test
    public void shouldGetNetPositions() throws Exception {
        when(position.getNet()).thenReturn(TEST_NET_POS);

        assertThat(uut.getNetPosition(TEST_PTF_ID), is(TEST_NET_POS));
        assertThat(uut.getTotalNetPosition(), is(TEST_NET_POS));
    }

    @Test
    public void shouldGetNetCashFlows() throws Exception {
        when(position.getNetCashFlow()).thenReturn(TEST_NET_CASH);

        assertThat(uut.getNetCashFlow(TEST_PTF_ID), is(TEST_NET_CASH));
        assertThat(uut.getTotalNetCashFlow(), is(TEST_NET_CASH));
    }

    @Test
    public void shouldUpdateTrade() throws Exception {
        TradeEvent testTrade = TradeEvent.newTrade(TEST_PTF_ID, TEST_INST_ID, 1050, 12.5, DateTime.now());

        uut.onTrade(testTrade);

        verify(positionUpdater).update(position, testTrade);
    }

    @Test
    public void shouldUpdateStartOfDay() throws Exception {
        StartOfDay startOfDay = StartOfDay.newStartOfDay(TEST_PTF_ID, TEST_INST_ID, 0, 0);

        uut.onStartOfDay(startOfDay);

        verify(positionUpdater).update(position, startOfDay);
    }
}
