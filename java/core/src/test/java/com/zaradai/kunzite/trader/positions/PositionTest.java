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
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PositionTest {
    private static final String TEST_PORTFOLIO_ID = "ptf_1";
    private static final String TEST_INS_ID = "ins_1";
    private static final String TEST_STRING = "Position{Instrument=ins_1, Portfolio=ptf_1, Net=2500, Opened=2014-07-11 14:03:00.000, Entry=12.34}";
    private static final DateTime TEST_OPEN = new DateTime(2014,7, 11, 14, 3);
    private static final double TEST_ENTRY_PRICE = 12.34;
    private static final int TEST_HASH_CODE = 890697266;
    private static final int PTF_HASH = 234;
    private static final int INST_HASH = 543;
    private final long START_OF_DAY = 1000;
    private final long INTRADAY_LONG = 2000;
    private final long INTRADAY_SHORT = 500;
    private final long NET_POS = START_OF_DAY + INTRADAY_LONG - INTRADAY_SHORT;

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
    }

    @Test
    public void shouldAddPositions() throws Exception {
        uut.add(INTRADAY_LONG);
        uut.add(INTRADAY_SHORT);

        assertThat(uut.getNet(), is(NET_POS));
    }

    @Test
    public void shouldGetEntryPrice() throws Exception {
        double entryPrice = 32.56;
        uut.setEntryPrice(entryPrice);

        assertThat(uut.getEntryPrice(), is(entryPrice));
    }

    @Test
    public void shouldGetOpened() throws Exception {
        DateTime opened = DateTime.now();
        uut.setOpened(opened);

        assertThat(uut.getOpened(), is(opened));
    }

    @Test
    public void shouldBeInactiveForZeroPosition() throws Exception {
        assertThat(uut.isActive(), is(false));

        uut.add(10);

        assertThat(uut.isActive(), is(true));

        uut.reset();

        uut.add(-10);

        assertThat(uut.isActive(), is(true));
    }

    @Test
    public void shouldBeLongIfZero() throws Exception {
        assertThat(uut.isLong(), is(true));
    }

    @Test
    public void shouldBeLongIfGreaterThaZero() throws Exception {
        uut.add(INTRADAY_LONG);

        assertThat(uut.isLong(), is(true));
    }

    @Test
    public void shouldBeShortIfLessThaZero() throws Exception {
        uut.add(-INTRADAY_SHORT);

        assertThat(uut.isLong(), is(false));
    }

    @Test
    public void shouldGenerateString() throws Exception {
        Portfolio portfolio = PortfolioMocker.create(TEST_PORTFOLIO_ID);
        Instrument instrument = InstrumentMocker.create(TEST_INS_ID);
        uut = new Position(portfolio, instrument);
        uut.add(INTRADAY_LONG);
        uut.add(INTRADAY_SHORT);
        uut.setOpened(TEST_OPEN);
        uut.setEntryPrice(TEST_ENTRY_PRICE);

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
