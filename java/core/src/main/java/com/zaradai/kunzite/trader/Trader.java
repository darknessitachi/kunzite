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

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.TraderConfiguration;
import com.zaradai.kunzite.trader.config.md.MarketDataConfigLoader;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfigLoader;
import com.zaradai.kunzite.trader.config.statics.StaticDataLoader;
import com.zaradai.kunzite.trader.services.md.MarketDataService;
import com.zaradai.kunzite.trader.services.orders.OrderGatewayService;
import com.zaradai.kunzite.trader.services.timer.TimerService;
import com.zaradai.kunzite.trader.services.trader.TraderService;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Trader extends AbstractIdleService {
    private final ContextLogger logger;
    private final TraderConfiguration configuration;
    private final StaticDataLoader staticDataLoader;
    private final OrderGatewayConfigLoader orderGatewayConfigLoader;
    private final MarketDataConfigLoader marketDataConfigLoader;
    private final TraderService traderService;
    private final OrderGatewayService orderGatewayService;
    private final MarketDataService marketDataService;
    private final TimerService timerService;
    private final Slf4jReporter reporter;

    @Inject
    Trader(ContextLogger logger, TraderConfiguration configuration, StaticDataLoader staticDataLoader,
           OrderGatewayConfigLoader orderGatewayConfigLoader, MarketDataConfigLoader marketDataConfigLoader,
           TraderService traderService, OrderGatewayService orderGatewayService, MarketDataService marketDataService,
           TimerService timerService, MetricRegistry metricRegistry) {
        this.logger = logger;
        this.configuration = configuration;
        this.staticDataLoader = staticDataLoader;
        this.orderGatewayConfigLoader = orderGatewayConfigLoader;
        this.marketDataConfigLoader = marketDataConfigLoader;
        this.traderService = traderService;
        this.orderGatewayService = orderGatewayService;
        this.marketDataService = marketDataService;
        this.timerService = timerService;

        reporter = Slf4jReporter.forRegistry(metricRegistry)
                .outputTo(LoggerFactory.getLogger("com.zaradai.kunzite.trader"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
    }

    private void build() throws ConfigException {
        traderService.build(staticDataLoader.load(configuration.getStaticConfigUri()));
        marketDataService.build(marketDataConfigLoader.load(configuration.getMarketDataConfigUri()));
        orderGatewayService.build(orderGatewayConfigLoader.load(configuration.getOrderGatewayConfigUri()));
    }

    @Override
    protected void startUp() throws Exception {
        // start the metric reporter
        reporter.start(1, TimeUnit.MINUTES);
        // build the services from loaded config
        build();
        // now start each service and wait for them to be started
        traderService.startAsync().awaitRunning();
        orderGatewayService.startAsync().awaitRunning();
        marketDataService.startAsync().awaitRunning();
        timerService.startAsync().awaitRunning();
        // log it
        LogHelper.info(logger).add("Status", "Trader Engine is running");
    }

    @Override
    protected void shutDown() throws Exception {
        timerService.stopAsync().awaitTerminated();
        marketDataService.stopAsync().awaitTerminated();
        orderGatewayService.stopAsync().awaitTerminated();
        traderService.stopAsync().awaitTerminated();
        // log it
        LogHelper.info(logger).add("Status", "Trader Engine has stopped");
    }
}
