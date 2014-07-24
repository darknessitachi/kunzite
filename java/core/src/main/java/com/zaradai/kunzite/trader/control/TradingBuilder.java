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

import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.algo.*;
import com.zaradai.kunzite.trader.config.statics.*;
import com.zaradai.kunzite.trader.instruments.*;
import com.zaradai.kunzite.trader.positions.PortfolioFactory;

public class TradingBuilder {
    private final ContextLogger logger;
    private final PortfolioFactory portfolioFactory;
    private final MarketFactory marketFactory;
    private final TradingStateFactory tradingStateFactory;
    private final InstrumentFactory instrumentFactory;
    private final AlgoFactory algoFactory;

    @Inject
    TradingBuilder(ContextLogger logger, PortfolioFactory portfolioFactory, MarketFactory marketFactory,
                          TradingStateFactory tradingStateFactory, InstrumentFactory instrumentFactory,
                          AlgoFactory algoFactory) {
        this.logger = logger;
        this.portfolioFactory = portfolioFactory;
        this.marketFactory = marketFactory;
        this.tradingStateFactory = tradingStateFactory;
        this.instrumentFactory = instrumentFactory;
        this.algoFactory = algoFactory;
    }

    public void build(TradingManager manager, StaticConfiguration configuration) {
        //1. build portfolios
        buildPortfolios(manager, configuration.getPortfolios());
        //2. build the markets
        buildMarkets(manager, configuration.getMarkets());
        //3. build the instruments
        buildInstruments(manager, configuration.getInstruments());
        //4. build states
        buildTradingStates(manager, configuration.getInstruments());
        //5. build the algos
        buildAlgos(manager, configuration.getAlgos());
    }

    private void buildPortfolios(TradingManager manager, Iterable<PortfolioConfig> portfolios) {
        for (PortfolioConfig config : portfolios) {
            String id = config.getId();
            manager.add(portfolioFactory.create(id));
        }
    }

    private void buildMarkets(TradingManager manager, Iterable<MarketConfig> markets) {
        for (MarketConfig config : markets) {
            String id = config.getId();
            Market market = marketFactory.create(id);
            // config the market ticks
            for (TickDefinition tickDefinition : config.getTickDefinitions()) {
                market.addTickDefinition(tickDefinition);
            }
            manager.add(market);
        }
    }

    private void buildInstruments(TradingManager manager, Iterable<InstrumentConfig> instruments) {
        // Need 2 pass construction due to membership resolving
        // 1. Build and fill with basic information
        for (InstrumentConfig config : instruments) {
            Instrument instrument = createInstrument(config);
            manager.add(instrument);
        }
        // 2. Fixup any membership references
        for (InstrumentConfig config : instruments) {
            Instrument instrument = manager.resolveInstrument(config.getId());
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

    private void buildTradingStates(TradingManager manager, Iterable<InstrumentConfig> instruments) {
        for (InstrumentConfig config : instruments) {
            Instrument instrument = manager.resolveInstrument(config.getId());
            TradingState state = tradingStateFactory.create(instrument);

            manager.add(state);
        }
    }

    private void buildAlgos(TradingManager manager, Iterable<AlgoConfig> algos) {
        for (AlgoConfig config : algos) {
            for (String instrumentId : config.getInstruments()) {
                Algo algo = buildAlgoForInstrument(manager, config, instrumentId);

                if (algo != null) {
                    manager.add(algo);
                }
            }
        }
    }

    private Algo buildAlgoForInstrument(TradingManager manager, AlgoConfig config, String instrumentId) {
        Algo res = null;
        String algoId = config.getName() + "-" + instrumentId;
        // get the instrument trading state
        TradingState tradingState = manager.resolveTradingState(instrumentId);

        try {
            res = algoFactory.create(config.getAlgo());
            res.setId(algoId);
            res.setState(tradingState);
        } catch (AlgoException e) {
            logFailedToBuildAlgo(algoId);
        }

        return res;
    }

    private void logFailedToBuildAlgo(String algoId) {
        LogHelper.error(logger)
                .addContext("Create Algo")
                .add("Algo", algoId)
                .log();
    }
}
