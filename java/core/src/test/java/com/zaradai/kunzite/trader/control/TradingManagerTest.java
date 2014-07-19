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

import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TradingManagerTest {
    private TradingManager uut;
    private ContextLogger logger;
    private TradingBuilder builder;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        builder = mock(TradingBuilder.class);
        uut = new TradingManager(logger, builder);
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

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenResolvingBadAlgo() throws Exception {
        uut.resolveAlgo(null);
    }

    @Test
    public void shouldRun() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();

        uut.run(configuration);

        verify(builder).build(uut, configuration);
        verify(logger).info();
    }
}
