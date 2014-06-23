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

import com.google.common.collect.Lists;
import com.zaradai.kunzite.trader.config.statics.InstrumentConfig;
import com.zaradai.kunzite.trader.config.statics.MarketConfig;
import com.zaradai.kunzite.trader.config.statics.PortfolioConfig;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.instruments.*;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TradingManagerTest {
    private static final String TEST_INST_ID = "inst";
    private static final String TEST_PTF_ID = "ptf";
    private static final String TEST_MKT_ID = "mkt";

    private StaticConfiguration config;
    private PortfolioFactory portfolioFactory;
    private MarketFactory marketFactory;
    private InstrumentFactory instrumentFactory;
    private TradingStateFactory tradingStateFactory;
    private TradingManager uut;

    @Before
    public void setUp() throws Exception {
        config = mock(StaticConfiguration.class);
        portfolioFactory = mock(PortfolioFactory.class);
        marketFactory = mock(MarketFactory.class);
        tradingStateFactory = mock(TradingStateFactory.class);
        instrumentFactory = mock(InstrumentFactory.class);
        uut = new TradingManager(config, portfolioFactory, marketFactory, tradingStateFactory, instrumentFactory);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenResolvingBadInstrumentId() throws Exception {
        uut.resolveInstrument(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenResolvingBadMarketId() throws Exception {
        uut.resolveMarket(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenResolvingBadPortfolioId() throws Exception {
        uut.resolvePortfolio(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenResolvingBadTradingState() throws Exception {
        uut.resolveTradingState(null);
    }

    @Test
    public void shouldBuild() throws Exception {
        setupConfig();
        uut.build();

        assertThat(uut.resolveInstrument(TEST_INST_ID), not(nullValue()));
        assertThat(uut.resolveInstrument("other"), is(nullValue()));

        assertThat(uut.resolvePortfolio(TEST_PTF_ID), not(nullValue()));
        assertThat(uut.resolveInstrument("other"), is(nullValue()));

        assertThat(uut.resolveMarket(TEST_MKT_ID), not(nullValue()));
        assertThat(uut.resolveInstrument("other"), is(nullValue()));

        assertThat(uut.resolveTradingState(TEST_INST_ID), not(nullValue()));
        assertThat(uut.resolveTradingState("other"), is(nullValue()));
    }

    private void setupConfig() {
        PortfolioConfig portfolioConfig = new PortfolioConfig();
        portfolioConfig.setId(TEST_PTF_ID);
        MarketConfig marketConfig = new MarketConfig();
        marketConfig.setId(TEST_MKT_ID);
        InstrumentConfig instrumentConfig= new InstrumentConfig();
        instrumentConfig.setId(TEST_INST_ID);
        instrumentConfig.setType(InstrumentType.Stock);
        when(config.getInstruments()).thenReturn(Lists.newArrayList(instrumentConfig));
        when(config.getMarkets()).thenReturn(Lists.newArrayList(marketConfig));
        when(config.getPortfolios()).thenReturn(Lists.newArrayList(portfolioConfig));
        Stock stock = mock(Stock.class);
        when(stock.getId()).thenReturn(TEST_INST_ID);
        when(instrumentFactory.createStock()).thenReturn(stock);
        Portfolio portfolio = mock(Portfolio.class);
        when(portfolio.getId()).thenReturn(TEST_PTF_ID);
        when(portfolioFactory.create(TEST_PTF_ID)).thenReturn(portfolio);
        Market market = mock(Market.class);
        when(market.getId()).thenReturn(TEST_MKT_ID);
        when(marketFactory.create(TEST_MKT_ID)).thenReturn(market);
        when(tradingStateFactory.create(stock)).thenReturn(mock(TradingState.class));
    }
}
