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
import com.zaradai.kunzite.trader.config.statics.InstrumentConfig;
import com.zaradai.kunzite.trader.config.statics.MarketConfig;
import com.zaradai.kunzite.trader.config.statics.PortfolioConfig;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.instruments.*;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioFactory;
import com.zaradai.kunzite.trader.positions.PortfolioResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class TradingManager implements InstrumentResolver, TradingStateResolver, MarketResolver, PortfolioResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradingManager.class);

    private final Map<String, Portfolio> portfolioByPortfolioId;
    private final Map<String, Market> marketByMarketId;
    private final Map<String, Instrument> instrumentByInstrumentId;
    private final Map<String, TradingState> tradingStateByInstrumentId;
    private final StaticConfiguration configuration;
    private final PortfolioFactory portfolioFactory;
    private final MarketFactory marketFactory;
    private final TradingStateFactory tradingStateFactory;
    private final InstrumentFactory instrumentFactory;

    @Inject
    TradingManager(StaticConfiguration configuration, PortfolioFactory portfolioFactory, MarketFactory marketFactory,
                   TradingStateFactory tradingStateFactory, InstrumentFactory instrumentFactory) {
        this.configuration = configuration;
        this.portfolioFactory = portfolioFactory;
        this.marketFactory = marketFactory;
        this.tradingStateFactory = tradingStateFactory;
        this.instrumentFactory = instrumentFactory;
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

    public void build() throws TradingException {

        try {
            //1. build portfolios
            buildPortfolios();
            //2. build the markets
            buildMarkets();
            //3. build the instruments
            buildInstruments();
            //4. Build states
            buildTradingStates();
            //5. log done
            logBuilt();
        } catch (Exception e) {
            throw new TradingException("Unable to build Trading manager", e);
        }
    }

    private void logBuilt() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void buildPortfolios() {
        for (PortfolioConfig config : configuration.getPortfolios()) {
            String id = config.getId();
            portfolioByPortfolioId.put(id, portfolioFactory.create(id));
        }
    }

    private void buildMarkets() {
        for (MarketConfig config : configuration.getMarkets()) {
            String id = config.getId();
            Market market = marketFactory.create(id);
            // config the market ticks
            for (TickDefinition tickDefinition : config.getTickDefinitions()) {
                market.addTickDefinition(tickDefinition);
            }
            marketByMarketId.put(id, market);
        }
    }

    private void buildInstruments() {
        // Need 2 pass construction due to membership resolving
        // 1. Build and fill with basic information
        for (InstrumentConfig config : configuration.getInstruments()) {
            Instrument instrument = createInstrument(config);
            instrumentByInstrumentId.put(instrument.getId(), instrument);
        }
        // 2. Fixup any membership references
        for (InstrumentConfig config : configuration.getInstruments()) {
            Instrument instrument = resolveInstrument(config.getId());
            fixupReferences((MembershipInstrument) instrument, config);
        }
    }

    private Instrument createInstrument(InstrumentConfig config) {

        switch (config.getType()) {
            case Basket:
                return createBasket(config);
            case Bond:
                return createBond(config);
            case Forward:
                return createForward(config);
            case Future:
                return createFuture(config);
            case Index:
                return createIndex(config);
            case Option:
                return createOption(config);
            case Stock:
                return createStock(config);
            case Warrant:
                return createWarrant(config);
        }

        return null;
    }

    private Instrument createBasket(InstrumentConfig config) {
        Basket res = instrumentFactory.createBasket();
        setMembershipMembers(res, config);

        return res;
    }

    private Instrument createBond(InstrumentConfig config) {
        Bond res = instrumentFactory.createBond();
        setMembershipMembers(res, config);
        // set bond specific
        res.setCoupon(config.getBondCoupon());
        res.setFirstCoupon(config.getBondFirstCoupon());
        res.setIssue(config.getBondIssue());
        res.setMaturity(config.getBondMaturity());
        res.setNotional(config.getBondNotional());

        return res;
    }

    private Instrument createForward(InstrumentConfig config) {
        Forward res = instrumentFactory.createForward();
        setMembershipMembers(res, config);

        return res;
    }

    private Instrument createFuture(InstrumentConfig config) {
        Future res = instrumentFactory.createFuture();
        setMembershipMembers(res, config);

        return res;
    }

    private Instrument createIndex(InstrumentConfig config) {
        Index res = instrumentFactory.createIndex();
        setMembershipMembers(res, config);

        return res;
    }

    private Instrument createOption(InstrumentConfig config) {
        Option res = instrumentFactory.createOption();
        setMembershipMembers(res, config);
        // set option specific
        res.setStrike(config.getStrike());
        res.setOptionType(config.getOptionType());

        return res;
    }

    private Instrument createStock(InstrumentConfig config) {
        Stock res = instrumentFactory.createStock();
        setMembershipMembers(res, config);

        return res;
    }

    private Instrument createWarrant(InstrumentConfig config) {
        Warrant res = instrumentFactory.createWarrant();
        setMembershipMembers(res, config);
        // set option specific
        res.setIssueDate(config.getIssueDate());
        res.setIssuer(config.getIssuer());
        res.setIssueSize(config.getIssueSize());
        res.setConversionRatio(config.getConversionRatio());

        return res;
    }

    private void setMembershipMembers(MembershipInstrument res, InstrumentConfig config) {
        res.setId(config.getId());
        res.setMarketId(config.getMarketId());
        res.setLotSize(config.getLotSize());
        res.setMultiplier(config.getMultiplier());
        res.setName(config.getName());
    }

    private void fixupReferences(MembershipInstrument instrument, InstrumentConfig config) {
        for (String id : config.getMembers()) {
            instrument.add(id);
        }
        for (String id : config.getPartOf()) {
            instrument.addTo(id);
        }
        if (instrument instanceof Basket) {
            Basket basket = (Basket) instrument;

            for (String id : config.getBasketConstituents()) {
                basket.addToBasket(id);
            }
        }
    }

    private void buildTradingStates() {
        for (Map.Entry<String, Instrument> entry : instrumentByInstrumentId.entrySet()) {
            Instrument instrument = entry.getValue();
            TradingState state = tradingStateFactory.create(instrument);

            tradingStateByInstrumentId.put(instrument.getId(), state);
        }
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
