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

import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.InstrumentMocker;
import com.zaradai.kunzite.trader.mocks.PortfolioMocker;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PositionTest {
    private static final String TEST_PORTFOLIO_ID = "ptf_1";
    private static final String TEST_INS_ID = "ins_1";
    private static final String TEST_STRING = "Position{Instrument=ins_1, Portfolio=ptf_1, Net=2500, Net Cash=2500.0, Day Long=2000, Day Long Cash=1500.0, Day Short=500, Day Short Cash=1000.0}";
    private final long START_OF_DAY = 1000;
    private final long INTRADAY_LONG = 2000;
    private final long INTRADAY_SHORT = 500;
    private final double START_OF_DAY_CASH_FLOW = 2000.0;
    private final double INTRADAY_LONG_CASH_FLOW = 1500.0;
    private final double INTRADAY_SHORT_CASH_FLOW = 1000.0;
    private final long NET_POS = START_OF_DAY + INTRADAY_LONG - INTRADAY_SHORT;
    private final double NET_CASH_FLOW = START_OF_DAY_CASH_FLOW + INTRADAY_LONG_CASH_FLOW - INTRADAY_SHORT_CASH_FLOW;

    private Position uut;

    @Before
    public void setUp() throws Exception {
        uut = new Position(null, null);
    }

    @Test
    public void shouldCreateProperly() throws Exception {
        Portfolio portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        Instrument instrument = InstrumentMocker.create(TEST_INS_ID);
        uut = new Position(portfolio, instrument);

        uut = new Position(portfolio, instrument);

        assertThat(uut.getPortfolio(), is(portfolio));
        assertThat(uut.getPortfolioId(), is(TEST_PORTFOLIO_ID));
        assertThat(uut.getInstrument(), is(instrument));
        assertThat(uut.getInstrumentId(), is(TEST_INS_ID));
    }

    @Test
    public void newPositionShouldHaveZeroNetPos() throws Exception {
        assertThat(uut.getNet(), is(0L));
        assertThat(uut.getNetCashFlow(), is(0.0));
    }

    @Test
    public void shouldGetStartOfDay() throws Exception {
        uut.setStartOfDay(START_OF_DAY);

        assertThat(uut.getStartOfDay(), is(START_OF_DAY));
    }

    @Test
    public void shouldAddPositions() throws Exception {
        uut.setStartOfDay(START_OF_DAY);
        uut.addLong(INTRADAY_LONG);
        uut.addShort(INTRADAY_SHORT);

        assertThat(uut.getNet(), is(NET_POS));
        assertThat(uut.getIntradayLong(), is(INTRADAY_LONG));
        assertThat(uut.getIntradayShort(), is(INTRADAY_SHORT));
    }

    @Test
    public void shouldGetStartOfDayCash() throws Exception {
        uut.setStartOfDayCashFlow(START_OF_DAY_CASH_FLOW);

        assertThat(uut.getStartOfDayCashFlow(), is(START_OF_DAY_CASH_FLOW));
    }

    @Test
    public void shouldAddCash() throws Exception {
        uut.setStartOfDayCashFlow(START_OF_DAY_CASH_FLOW);
        uut.addLongCashFlow(INTRADAY_LONG_CASH_FLOW);
        uut.addShortCashFlow(INTRADAY_SHORT_CASH_FLOW);

        assertThat(uut.getNetCashFlow(), is(NET_CASH_FLOW));
        assertThat(uut.getIntradayLongCashFlow(), is(INTRADAY_LONG_CASH_FLOW));
        assertThat(uut.getIntradayShortCashFlow(), is(INTRADAY_SHORT_CASH_FLOW));
    }

    @Test
    public void shouldBeLongIfZero() throws Exception {
        assertThat(uut.isLong(), is(true));
    }

    @Test
    public void shouldBeLongIfGreaterThaZero() throws Exception {
        uut.addLong(INTRADAY_LONG);

        assertThat(uut.isLong(), is(true));
    }

    @Test
    public void shouldBeShortIfLessThaZero() throws Exception {
        uut.addShort(INTRADAY_SHORT);

        assertThat(uut.isLong(), is(false));
    }

    @Test
    public void shouldGenerateString() throws Exception {
        Portfolio portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        Instrument instrument = InstrumentMocker.create(TEST_INS_ID);
        uut = new Position(portfolio, instrument);
        uut.setStartOfDay(START_OF_DAY);
        uut.addLong(INTRADAY_LONG);
        uut.addShort(INTRADAY_SHORT);
        uut.setStartOfDayCashFlow(START_OF_DAY_CASH_FLOW);
        uut.addLongCashFlow(INTRADAY_LONG_CASH_FLOW);
        uut.addShortCashFlow(INTRADAY_SHORT_CASH_FLOW);

        assertThat(uut.toString(), is(TEST_STRING));
    }

    @Test
    public void shouldBeEqualOnInstrumentAndPortfolioOnly() throws Exception {
        Portfolio portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        Instrument instrument = InstrumentMocker.create(TEST_INS_ID);
        uut = new Position(portfolio, instrument);
        Position test = new Position(portfolio, instrument);

        assertThat(uut.equals(test), is(true));
    }

    @Test
    public void shouldNotBeEqualIfOtherNull() throws Exception {
        Portfolio portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        Instrument instrument = InstrumentMocker.create(TEST_INS_ID);
        uut = new Position(portfolio, instrument);

        assertThat(uut.equals(null), is(false));
    }

    @Test
    public void shouldNotBeEqualIfOtherType() throws Exception {
        Portfolio portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        Instrument instrument = InstrumentMocker.create(TEST_INS_ID);
        uut = new Position(portfolio, instrument);

        assertThat(uut.equals(new Object()), is(false));
    }
}
