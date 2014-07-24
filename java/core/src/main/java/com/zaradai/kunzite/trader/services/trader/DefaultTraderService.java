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
package com.zaradai.kunzite.trader.services.trader;

import com.google.inject.Inject;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.control.TradingManager;
import com.zaradai.kunzite.trader.services.AbstractQueueBridge;

/**
 * Simple message pump for the trader
 */
public class DefaultTraderService extends AbstractQueueBridge implements TraderService {
    static final String SERVICE_NAME = "TraderService";

    private final EventAggregator eventAggregator;
    private final TradingManager tradingManager;

    @Inject
    DefaultTraderService(ContextLogger contextLogger, EventAggregator eventAggregator, TradingManager tradingManager) {
        super(contextLogger);
        this.eventAggregator = eventAggregator;
        this.tradingManager = tradingManager;
    }

    @Override
    public void handleEvent(Object event) {
        eventAggregator.publish(event);
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Override
    public void build(StaticConfiguration configuration) throws ConfigException {
        tradingManager.build(configuration);
    }

    @Override
    public void onTraderEvent(Object event) {
        onEvent(event);
    }

    @Override
    protected void startUp() throws Exception {
        // initialize the trader
        tradingManager.initialize();
    }
}
