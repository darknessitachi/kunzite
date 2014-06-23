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

import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.instruments.InstrumentFactory;
import com.zaradai.kunzite.trader.instruments.MarketFactory;
import com.zaradai.kunzite.trader.positions.PortfolioFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class TradingManagerTest {
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
}
