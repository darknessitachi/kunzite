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

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.algo.Algo;
import com.zaradai.kunzite.trader.algo.AlgoResolver;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.instruments.InstrumentResolver;
import com.zaradai.kunzite.trader.instruments.Market;
import com.zaradai.kunzite.trader.instruments.MarketResolver;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioResolver;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class TradingManager implements InstrumentResolver, TradingStateResolver, MarketResolver, PortfolioResolver,
        AlgoResolver {
    private final Map<String, Portfolio> portfolioByPortfolioId;
    private final Map<String, Market> marketByMarketId;
    private final Map<String, Instrument> instrumentByInstrumentId;
    private final Map<String, TradingState> tradingStateByInstrumentId;
    private final Map<String, Algo> algoByAlgoId;
    private final ContextLogger logger;
    private final TradingBuilder builder;


    @Inject
    TradingManager(ContextLogger logger, TradingBuilder builder) {
        this.logger = logger;
        this.builder = builder;
        tradingStateByInstrumentId = Maps.newHashMap();
        instrumentByInstrumentId = Maps.newHashMap();
        marketByMarketId = Maps.newHashMap();
        portfolioByPortfolioId = Maps.newHashMap();
        algoByAlgoId = Maps.newHashMap();
    }

    public void build(StaticConfiguration configuration) {
        builder.build(this, configuration);
        logBuilt();
    }

    private void logBuilt() {
        LogHelper.info(logger)
                .addContext("Trading Manager built")
                .add("Portfolios", portfolioByPortfolioId.size())
                .add("Markets", marketByMarketId.size())
                .add("Instruments", instrumentByInstrumentId.size())
                .add("Algos", algoByAlgoId.size())
                .add("States", tradingStateByInstrumentId.size())
                .log();
    }

    @Override
    public Instrument resolveInstrument(String instrumentId) {
        checkArgument(!Strings.isNullOrEmpty(instrumentId));

        return instrumentByInstrumentId.get(instrumentId);
    }

    @Override
    public Market resolveMarket(String marketId) {
        checkArgument(!Strings.isNullOrEmpty(marketId));

        return marketByMarketId.get(marketId);
    }

    @Override
    public Portfolio resolvePortfolio(String portfolioId) {
        checkArgument(!Strings.isNullOrEmpty(portfolioId));

        return portfolioByPortfolioId.get(portfolioId);
    }

    @Override
    public TradingState resolveTradingState(String instrumentId) {
        checkArgument(!Strings.isNullOrEmpty(instrumentId));

        return tradingStateByInstrumentId.get(instrumentId);
    }

    @Override
    public Algo resolveAlgo(String id) {
        checkArgument(!Strings.isNullOrEmpty(id));

        return algoByAlgoId.get(id);
    }

    public void add(Portfolio portfolio) {
        portfolioByPortfolioId.put(portfolio.getId(), portfolio);
    }

    public void add(Market market) {
        marketByMarketId.put(market.getId(), market);
    }

    public void add(Instrument instrument) {
        instrumentByInstrumentId.put(instrument.getId(), instrument);
    }

    public void add(TradingState state) {
        tradingStateByInstrumentId.put(state.getInstrument().getId(), state);
    }

    public void add(Algo algo) {
        algoByAlgoId.put(algo.getId(), algo);
    }
}
