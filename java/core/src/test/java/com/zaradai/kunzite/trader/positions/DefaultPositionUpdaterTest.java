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

import com.zaradai.kunzite.trader.events.Trade;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.InstrumentMocker;
import com.zaradai.kunzite.trader.mocks.PortfolioMocker;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class DefaultPositionUpdaterTest {
    private static final String TEST_PORTFOLIO_ID = "ptr";
    private static final String TEST_INS_ID = "ins";
    private static final long TEST_LONG_POS = 4000;
    private static final long TEST_SHORT_POS = -2500;
    private static final double TEST_MULTIPLIER = 1.0;
    private static final double TEST_PRICE = 12.5;
    private static final double TEST_LONG_CASH = TEST_LONG_POS * TEST_MULTIPLIER * TEST_PRICE;
    private static final double TEST_SHORT_CASH = TEST_SHORT_POS * TEST_MULTIPLIER * TEST_PRICE;

    private Position position;
    private Portfolio portfolio;
    private Instrument instrument;
    private DefaultPositionUpdater uut;

    @Before
    public void setUp() throws Exception {
        portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        instrument = InstrumentMocker.create(TEST_INS_ID);
        position = new Position(portfolio, instrument);
        uut = new DefaultPositionUpdater();
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailUpdateWithInvalidPosition() throws Exception {
        Trade testTrade = Trade.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, 10, 10.0);

        uut.update(null, testTrade);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailUpdateWithInvalidTrade() throws Exception {
        uut.update(position, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateWithInvalidInstrumentId() throws Exception {
        Trade testTrade = Trade.newTrade(TEST_PORTFOLIO_ID, "Unknown", 10, 10.0);

        uut.update(position, testTrade);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailUpdateWithInvalidPortfolioId() throws Exception {
        Trade testTrade = Trade.newTrade("Unknown", TEST_INS_ID, 10, 10.0);

        uut.update(position, testTrade);
    }

    @Test
    public void shouldUpdateLong() throws Exception {
        when(instrument.getMultiplier()).thenReturn(TEST_MULTIPLIER);
        Trade testTrade = Trade.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_LONG_POS, TEST_PRICE);

        uut.update(position, testTrade);

        assertThat(position.getNet(), is(TEST_LONG_POS));
        assertThat(position.getNetCashFlow(), is(TEST_LONG_CASH));
        assertThat(position.isLong(), is(true));
    }

    @Test
    public void shouldUpdateShort() throws Exception {
        when(instrument.getMultiplier()).thenReturn(TEST_MULTIPLIER);
        Trade testTrade = Trade.newTrade(TEST_PORTFOLIO_ID, TEST_INS_ID, TEST_SHORT_POS, TEST_PRICE);

        uut.update(position, testTrade);

        assertThat(position.getNet(), is(TEST_SHORT_POS));
        assertThat(position.getNetCashFlow(), is(TEST_SHORT_CASH));
        assertThat(position.isLong(), is(false));
    }
}
