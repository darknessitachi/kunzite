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
import com.zaradai.kunzite.trader.config.orders.GatewayConfig;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfiguration;
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
    private final OrderGatewayFactory orderGatewayFactory;
    private final Map<String, OrderGateway> orderGatewayByMarket;
    private final ContextLogger logger;

    @Inject
    DefaultOrderGatewayService(ContextLogger logger, EventAggregator eventAggregator, TraderService traderService,
                               OrderGatewayFactory orderGatewayFactory) {
        super(logger);
        this.logger = logger;
        this.eventAggregator = eventAggregator;
        this.traderService = traderService;
        this.orderGatewayFactory = orderGatewayFactory;

        eventAggregator.subscribe(this);
        orderGatewayByMarket = createGatewayMap();
    }

    protected Map<String, OrderGateway> createGatewayMap() {
        return Maps.newHashMap();
    }

    @Override
    protected void startUp() throws Exception {
        // start all order gateways
        for (Map.Entry<String, OrderGateway> gatewayEntry : orderGatewayByMarket.entrySet()) {
            gatewayEntry.getValue().startAsync().awaitRunning();
            logGatewayState(gatewayEntry.getValue().getName(), gatewayEntry.getKey(), "running");
        }
    }

    private void logGatewayState(String gatewayName, String marketId, String state) {
        LogHelper.info(logger)
                .add("Gateway", gatewayName)
                .add("Market", marketId)
                .add("Is", state)
                .log();
    }

    @Override
    protected void shutDown() throws Exception {
        // stop all order gateways
        for (Map.Entry<String, OrderGateway> gatewayEntry : orderGatewayByMarket.entrySet()) {
            gatewayEntry.getValue().stopAsync().awaitTerminated();
            logGatewayState(gatewayEntry.getValue().getName(), gatewayEntry.getKey(), "stopped");
        }
    }

    @Override
    public void handleEvent(Object event) {
        if (event instanceof OrderSendEvent) {
            processSendOrder((OrderSendEvent) event);
        } else if (event instanceof OrderStatusEvent) {
            processStatusEvent((OrderStatusEvent) event);
        }
    }

    /**
     * Order status received from gateway needs to be sent to the trader
     * @param event
     */
    private void processStatusEvent(OrderStatusEvent event) {
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

    @Override
    public void load(OrderGatewayConfiguration configuration) {
        for (GatewayConfig gatewayConfig : configuration.getGateways()) {
            try {
                OrderGateway gateway = orderGatewayFactory.create(gatewayConfig.getGatewayClass());
                this.orderGatewayByMarket.put(gatewayConfig.getMarketId(), gateway);
            } catch (GatewayException e) {
                LogHelper.error(logger)
                        .addContext("OrderGatewayService-build gateway")
                        .addReason(e.getMessage())
                        .add("Gateway", gatewayConfig.getGatewayClass())
                        .add("Market", gatewayConfig.getMarketId())
                        .log();
            }
        }
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