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
package com.zaradai.kunzite.trader.config.statics;

import com.google.common.collect.Lists;

import java.util.List;

public class StaticConfiguration {
    private final List<InstrumentConfig> instruments;
    private final List<MarketConfig> markets;
    private final List<PortfolioConfig> portfolios;
    private final List<AlgoConfig> algos;

    public StaticConfiguration() {
        instruments = Lists.newArrayList();
        markets = Lists.newArrayList();
        portfolios = Lists.newArrayList();
        algos = Lists.newArrayList();
    }

    public Iterable<InstrumentConfig> getInstruments() {
        return instruments;
    }

    public void add(InstrumentConfig instrument) {
        instruments.add(instrument);
    }

    public Iterable<MarketConfig> getMarkets() {
        return markets;
    }

    public Iterable<AlgoConfig> getAlgos() {
        return algos;
    }

    public void add(MarketConfig market) {
        markets.add(market);
    }

    public Iterable<PortfolioConfig> getPortfolios() {
        return portfolios;
    }

    public void add(PortfolioConfig portfolio) {
        portfolios.add(portfolio);
    }

    public void add(AlgoConfig algo) {
        algos.add(algo);
    }
}
