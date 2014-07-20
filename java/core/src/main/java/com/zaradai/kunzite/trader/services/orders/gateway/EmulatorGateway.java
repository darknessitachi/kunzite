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
package com.zaradai.kunzite.trader.services.orders.gateway;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.zaradai.kunzite.trader.events.OrderStatus;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.orders.model.NewOrder;
import com.zaradai.kunzite.trader.orders.model.OrderRequestType;
import com.zaradai.kunzite.trader.services.orders.OrderGateway;
import com.zaradai.kunzite.trader.services.orders.OrderGatewayService;
import org.joda.time.DateTime;

public class EmulatorGateway extends AbstractIdleService implements OrderGateway {
    static final String GATEWAY_NAME = "Emulator";

    private final OrderGatewayService orderGatewayService;

    @Inject
    EmulatorGateway(OrderGatewayService orderGatewayService) {
        this.orderGatewayService = orderGatewayService;
    }

    @Override
    protected void startUp() throws Exception {
        //NOP
    }

    @Override
    protected void shutDown() throws Exception {
        //NOP
    }

    @Override
    public void processOrder(NewOrder order) {
        if (order.getRequestType() == OrderRequestType.Create || order.getRequestType() == OrderRequestType.Amend) {
            fillOrder(order);
        } else {
            cancelOrder(order);
        }
    }

    @Override
    public String getName() {
        return GATEWAY_NAME;
    }

    private void cancelOrder(NewOrder order) {
        sendAck(order);
        // now cancel
        OrderStatusEvent event;
        event = new OrderStatusEvent();
        event.setOrderId(order.getRefData().getOrderId());
        event.setAlgoId(order.getRefData().getAlgoId());
        event.setOrderStatus(OrderStatus.Cancelled);
        event.setExchangeId("emulator");
        event.setTimestamp(DateTime.now());
        // send  fill
        orderGatewayService.onOrderStatus(event);
    }

    private void fillOrder(NewOrder order) {
        sendAck(order);
        // now fill
        OrderStatusEvent event;
        event = new OrderStatusEvent();
        event.setOrderId(order.getRefData().getOrderId());
        event.setAlgoId(order.getRefData().getAlgoId());
        event.setOrderStatus(OrderStatus.Filled);
        event.setExchangeId("emulator");
        event.setLastPx(order.getPrice());
        event.setExecQty(order.getQuantity());
        event.setTimestamp(DateTime.now());
        // send  fill
        orderGatewayService.onOrderStatus(event);
    }

    private void sendAck(NewOrder order) {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderId(order.getRefData().getOrderId());
        event.setAlgoId(order.getRefData().getAlgoId());
        event.setOrderStatus(OrderStatus.New);
        event.setExchangeId("emulator");
        event.setTimestamp(DateTime.now());
        // send  ack
        orderGatewayService.onOrderStatus(event);
    }
}
