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
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.instruments.InstrumentResolver;
import com.zaradai.kunzite.trader.instruments.Market;
import com.zaradai.kunzite.trader.instruments.MarketResolver;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioResolver;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class TradingManager implements InstrumentResolver, TradingStateResolver, MarketResolver, PortfolioResolver {
    private final Map<String, Portfolio> portfolioByPortfolioId;
    private final Map<String, Market> marketByMarketId;
    private final Map<String, Instrument> instrumentByInstrumentId;
    private final Map<String, TradingState> tradingStateByInstrumentId;

    public TradingManager() {
        tradingStateByInstrumentId = createStateMap();
        instrumentByInstrumentId = createInstrumentsMap();
        marketByMarketId = createMarketsMap();
        portfolioByPortfolioId = createPortfoliosMap();
    }

    private Map<String, Portfolio> createPortfoliosMap() {
        return Maps.newHashMap();
    }

    private Map<String, Market> createMarketsMap() {
        return Maps.newHashMap();
    }

    private Map<String, Instrument> createInstrumentsMap() {
        return Maps.newHashMap();
    }

    private Map<String, TradingState> createStateMap() {
        return Maps.newHashMap();
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
}
