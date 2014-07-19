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
package com.zaradai.kunzite.trader;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.events.eventbus.EventBusAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.disruptor.DisruptorLogger;
import com.zaradai.kunzite.trader.algo.AlgoFactory;
import com.zaradai.kunzite.trader.algo.AlgoResolver;
import com.zaradai.kunzite.trader.algo.InjectedAlgoFactory;
import com.zaradai.kunzite.trader.config.statics.StaticLoader;
import com.zaradai.kunzite.trader.config.statics.digester.DigesterStaticLoader;
import com.zaradai.kunzite.trader.control.*;
import com.zaradai.kunzite.trader.filters.DefaultFilterManager;
import com.zaradai.kunzite.trader.filters.FilterManager;
import com.zaradai.kunzite.trader.instruments.*;
import com.zaradai.kunzite.trader.marketdata.DefaultMarketBook;
import com.zaradai.kunzite.trader.marketdata.MarketBook;
import com.zaradai.kunzite.trader.marketdata.MarketBookFactory;
import com.zaradai.kunzite.trader.orders.book.DefaultOrderBook;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.book.OrderBookFactory;
import com.zaradai.kunzite.trader.orders.execution.*;
import com.zaradai.kunzite.trader.orders.utils.KunziteOrderIdGenerator;
import com.zaradai.kunzite.trader.orders.utils.OrderIdGenerator;
import com.zaradai.kunzite.trader.positions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraderModule extends AbstractModule {
    private Logger logger;
    private EventBus eventBus;
    private KunziteOrderIdGenerator orderIdGenerator;

    public TraderModule() {
        logger = LoggerFactory.getLogger("trader");
        eventBus = new EventBus("Trader");
        orderIdGenerator = KunziteOrderIdGenerator.newInstance();
    }

    @Override
    protected void configure() {
        bind(Logger.class).toInstance(logger);
        bind(ContextLogger.class).to(DisruptorLogger.class).in(Singleton.class);
        bind(EventBus.class).toInstance(eventBus);
        bind(EventAggregator.class).to(EventBusAggregator.class).in(Singleton.class);
        bind(StaticLoader.class).to(DigesterStaticLoader.class);
        bind(TradingManager.class).in(Singleton.class);
        bind(TradingBuilder.class);
        bind(AlgoFactory.class).to(InjectedAlgoFactory.class);
        bind(InstrumentFactory.class).to(DefaultInstrumentFactory.class);

        bind(InstrumentResolver.class).to(TradingManager.class);
        bind(TradingStateResolver.class).to(TradingManager.class);
        bind(MarketResolver.class).to(TradingManager.class);
        bind(PortfolioResolver.class).to(TradingManager.class);
        bind(AlgoResolver.class).to(TradingManager.class);

        bind(FilterManager.class).to(DefaultFilterManager.class);

        bind(OrderIdGenerator.class).toInstance(orderIdGenerator);
        bind(PositionUpdater.class).to(DefaultPositionUpdater.class);

        install(new FactoryModuleBuilder().implement(TradingState.class, InstrumentTradingState.class).build(TradingStateFactory.class));
        install(new FactoryModuleBuilder().build(BasketFactory.class));
        install(new FactoryModuleBuilder().build(BondFactory.class));
        install(new FactoryModuleBuilder().build(ForwardFactory.class));
        install(new FactoryModuleBuilder().build(FutureFactory.class));
        install(new FactoryModuleBuilder().build(IndexFactory.class));
        install(new FactoryModuleBuilder().build(OptionFactory.class));
        install(new FactoryModuleBuilder().build(StockFactory.class));
        install(new FactoryModuleBuilder().build(WarrantFactory.class));

        install(new FactoryModuleBuilder().build(MarketFactory.class));
        install(new FactoryModuleBuilder().build(PortfolioFactory.class));

        install(new FactoryModuleBuilder().implement(MarketBook.class, DefaultMarketBook.class).build(MarketBookFactory.class));
        install(new FactoryModuleBuilder().implement(PositionBook.class, DefaultPositionBook.class).build(PositionBookFactory.class));
        install(new FactoryModuleBuilder().implement(OrderBook.class, DefaultOrderBook.class).build(OrderBookFactory.class));
        install(new FactoryModuleBuilder().implement(OrderManager.class, DefaultOrderManager.class).build(OrderManagerFactory.class));
        install(new FactoryModuleBuilder().implement(OrderStateManager.class, DefaultOrderStateManager.class).build(OrderStateManagerFactory.class));
        install(new FactoryModuleBuilder().build(PositionFactory.class));


    }
}
