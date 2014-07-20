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

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.events.OrderStatus;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.orders.model.NewOrder;
import com.zaradai.kunzite.trader.services.AbstractQueueBridge;
import com.zaradai.kunzite.trader.services.trader.TraderService;
import org.joda.time.DateTime;

import java.util.Map;

public class DefaultOrderGatewayService extends AbstractQueueBridge implements OrderGatewayService {
    static final String SERVICE_NAME = "OrderGatewayService";

    private final EventAggregator eventAggregator;
    private final TraderService traderService;
    private final Map<String, OrderGateway> orderGatewayByMarket;
    private final ContextLogger logger;

    @Inject
    DefaultOrderGatewayService(ContextLogger logger, EventAggregator eventAggregator, TraderService traderService) {
        super(logger);
        this.logger = logger;
        this.eventAggregator = eventAggregator;
        this.traderService = traderService;

        eventAggregator.subscribe(this);
        orderGatewayByMarket = createGatewayMap();
    }

    protected Map<String, OrderGateway> createGatewayMap() {
        return Maps.newHashMap();
    }

    @Override
    public void handleEvent(Object event) {
        if (event instanceof OrderSendEvent) {
            processSendOrder((OrderSendEvent) event);
        } else if (event instanceof OrderStatusEvent) {
            processOrderEvent((OrderStatusEvent) event);
        }
    }

    /**
     * Order status received from gateway needs to be sent to the trader
     * @param event
     */
    private void processOrderEvent(OrderStatusEvent event) {
        traderService.onEvent(event);
    }

    /**
     * Order request from trader needs to be sent to handling gateway
     * @param event
     */
    private void processSendOrder(OrderSendEvent event) {
        // iterate the orders
        for (NewOrder order : event.getOrders()) {
            // lookup the order gateway for the intended market
            OrderGateway gateway = getGateway(order.getRefData().getMarketId());

            if (gateway != null) {
                gateway.processOrder(order);
            } else {
                processInvalidGateway(order);
            }
        }
    }

    private void processInvalidGateway(NewOrder order) {
        OrderStatusEvent statusEvent = new OrderStatusEvent();
        statusEvent.setOrderStatus(OrderStatus.NoExchange);
        statusEvent.setTimestamp(DateTime.now());
        statusEvent.setAlgoId(order.getRefData().getAlgoId());
        statusEvent.setOrderId(order.getRefData().getOrderId());
        // send back to the trader
        onEvent(statusEvent);
    }

    private OrderGateway getGateway(String marketId) {
        OrderGateway gateway = orderGatewayByMarket.get(marketId);

        if (gateway == null) {
            LogHelper.error(logger)
                    .addContext("Order Gateway Service")
                    .addReason("No gateway for market")
                    .add("Market", marketId)
                    .log();
        }

        return gateway;
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }

    @Subscribe
    @Override
    public void onOrderSend(OrderSendEvent event) {
        // add to the queue to be processed and return immediately
        onEvent(event);
    }

    @Override
    public void onOrderStatus(OrderStatusEvent event) {
        // add to the queue to be processed and return immediately
        onEvent(event);
    }
}