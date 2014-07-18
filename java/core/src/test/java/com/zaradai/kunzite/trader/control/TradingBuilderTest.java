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
import com.zaradai.kunzite.trader.algo.Algo;
import com.zaradai.kunzite.trader.algo.AlgoFactory;
import com.zaradai.kunzite.trader.config.statics.*;
import com.zaradai.kunzite.trader.instruments.*;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TradingBuilderTest {
    private static final String TEST_ID = "test";
    private static final String TEST_ALGO_ID = "algo";
    private static final String TEST_ALGO_NAME = "name";

    private ContextLogger logger;
    private PortfolioFactory portfolioFactory;
    private MarketFactory marketFactory;
    private TradingStateFactory tradingStateFactory;
    private InstrumentFactory instrumentFactory;
    private AlgoFactory algoFactory;
    private TradingBuilder uut;
    private TradingManager tradingManager;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        portfolioFactory = mock(PortfolioFactory.class);
        marketFactory = mock(MarketFactory.class);
        tradingStateFactory = mock(TradingStateFactory.class);
        instrumentFactory = mock(InstrumentFactory.class);
        algoFactory = mock(AlgoFactory.class);
        tradingManager = mock(TradingManager.class);

        uut = new TradingBuilder(logger, portfolioFactory, marketFactory, tradingStateFactory, instrumentFactory,
                algoFactory);
    }

    @Test
    public void shouldBuildPortfolios() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        PortfolioConfig config = new PortfolioConfig();
        config.setId(TEST_ID);
        configuration.add(config);
        Portfolio portfolio = mock(Portfolio.class);
        when(portfolioFactory.create(TEST_ID)).thenReturn(portfolio);

        uut.build(tradingManager, configuration);

        verify(portfolioFactory).create(TEST_ID);
        verify(tradingManager).add(portfolio);
    }

    @Test
    public void shouldBuildMarkets() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        MarketConfig config = new MarketConfig();
        config.setId(TEST_ID);
        configuration.add(config);
        Market market = mock(Market.class);
        when(marketFactory.create(TEST_ID)).thenReturn(market);

        uut.build(tradingManager, configuration);

        verify(marketFactory).create(TEST_ID);
        verify(tradingManager).add(market);
    }

    @Test
    public void shouldBuildBasketInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Basket basket = mock(Basket.class);
        when(instrumentFactory.createBasket()).thenReturn(basket);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Basket);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createBasket();
        verify(tradingManager).add(basket);
    }

    @Test
    public void shouldBuildBondInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Bond bond = mock(Bond.class);
        when(instrumentFactory.createBond()).thenReturn(bond);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Bond);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createBond();
        verify(tradingManager).add(bond);
    }

    @Test
    public void shouldBuildForwardInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Forward forward = mock(Forward.class);
        when(instrumentFactory.createForward()).thenReturn(forward);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Forward);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createForward();
        verify(tradingManager).add(forward);
    }

    @Test
    public void shouldBuildFutureInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Future future = mock(Future.class);
        when(instrumentFactory.createFuture()).thenReturn(future);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Future);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createFuture();
        verify(tradingManager).add(future);
    }

    @Test
    public void shouldBuildIndexInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Index index = mock(Index.class);
        when(instrumentFactory.createIndex()).thenReturn(index);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Index);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createIndex();
        verify(tradingManager).add(index);
    }

    @Test
    public void shouldBuildOptionInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Option option = mock(Option.class);
        when(instrumentFactory.createOption()).thenReturn(option);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Option);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createOption();
        verify(tradingManager).add(option);
    }

    @Test
    public void shouldBuildStockInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Stock stock = mock(Stock.class);
        when(instrumentFactory.createStock()).thenReturn(stock);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Stock);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createStock();
        verify(tradingManager).add(stock);
    }

    @Test
    public void shouldBuildWarrantInstrument() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Warrant warrant = mock(Warrant.class);
        when(instrumentFactory.createWarrant()).thenReturn(warrant);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Warrant);
        configuration.add(config);

        uut.build(tradingManager, configuration);

        verify(instrumentFactory).createWarrant();
        verify(tradingManager).add(warrant);
    }

    @Test
    public void shouldBuildTradingStates() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Stock stock = mock(Stock.class);
        when(instrumentFactory.createStock()).thenReturn(stock);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Stock);
        configuration.add(config);
        when(tradingManager.resolveInstrument(TEST_ID)).thenReturn(stock);
        TradingState state = mock(TradingState.class);
        when(tradingStateFactory.create(stock)).thenReturn(state);

        uut.build(tradingManager, configuration);

        verify(tradingStateFactory).create(stock);
        verify(tradingManager).add(state);
    }

    @Test
    public void shouldBuildAlgos() throws Exception {
        StaticConfiguration configuration = new StaticConfiguration();
        InstrumentConfig config = new InstrumentConfig();
        Stock stock = mock(Stock.class);
        when(instrumentFactory.createStock()).thenReturn(stock);
        config.setId(TEST_ID);
        config.setType(InstrumentType.Stock);
        configuration.add(config);
        when(tradingManager.resolveInstrument(TEST_ID)).thenReturn(stock);
        TradingState tradingState = mock(TradingState.class);
        when(tradingManager.resolveTradingState(TEST_ID)).thenReturn(tradingState);
        AlgoConfig algoConfig = new AlgoConfig();
        algoConfig.setName(TEST_ALGO_ID);
        algoConfig.setAlgo(TEST_ALGO_NAME);
        algoConfig.addInstrument(TEST_ID);
        configuration.add(algoConfig);
        Algo algo = mock(Algo.class);
        when(algoFactory.create(TEST_ALGO_NAME, tradingState)).thenReturn(algo);

        uut.build(tradingManager, configuration);

        verify(tradingManager).add(algo);
    }

}
