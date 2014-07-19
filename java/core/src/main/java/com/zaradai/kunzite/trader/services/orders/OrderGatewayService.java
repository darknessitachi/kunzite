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
package com.zaradai.kunzite.trader.services.orders;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.services.AbstractQueueBridge;

public class OrderGatewayService extends AbstractQueueBridge {
    static final String SERVICE_NAME = "OrderGatewayService";

    private final EventAggregator eventAggregator;

    @Inject
    OrderGatewayService(ContextLogger contextLogger, EventAggregator eventAggregator) {
        super(contextLogger);
        this.eventAggregator = eventAggregator;

        eventAggregator.subscribe(this);
    }

    @Override
    public void handleEvent(Object event) {
        // decode which market and send to the gateway
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Subscribe
    void onOrderSend(OrderSendEvent event) {
        // add to the queue to be processed and return immediately
        onEvent(event);
    }
}