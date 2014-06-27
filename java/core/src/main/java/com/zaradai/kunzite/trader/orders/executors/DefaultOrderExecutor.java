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
package com.zaradai.kunzite.trader.orders.executors;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.filters.Filter;
import com.zaradai.kunzite.trader.filters.FilterManager;
import com.zaradai.kunzite.trader.orders.Order;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import com.zaradai.kunzite.trader.orders.OrderRequestType;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple order executor per tradeable instrument that batch converts a number of requests into orders to be sent to
 * the market.  The requests are validated against limits and the associated trading state
 */
public class DefaultOrderExecutor implements OrderExecutor {
    private final ContextLogger logger;
    private final EventAggregator eventAggregator;
    private final OrderIdGenerator orderIdGenerator;
    private final TradingState tradingState;

    private final List<OrderRequest> pending;
    private final String instrumentId;
    private final String marketId;
    private final Filter orderFilter;

    @Inject
    DefaultOrderExecutor(ContextLogger logger, EventAggregator eventAggregator, OrderIdGenerator orderIdGenerator,
                         FilterManager filterManager, @Assisted TradingState tradingState) {
        this.logger = logger;
        this.eventAggregator = eventAggregator;
        this.orderIdGenerator = orderIdGenerator;
        this.tradingState = tradingState;
        pending = createPendingList();

        instrumentId = tradingState.getInstrument().getId();
        marketId = tradingState.getInstrument().getMarketId();
        orderFilter = filterManager.createFor(tradingState);
    }

    private List<OrderRequest> createPendingList() {
        return Lists.newArrayList();
    }

    @Override
    public void addRequest(OrderRequest orderRequest) {
        checkNotNull(orderRequest, "Invalid Order Request");
        pending.add(orderRequest);
    }

    @Override
    public void execute() {
        processPending();
        processRejects();
        clear();
    }

    private void processRejects() {
        for (OrderRequest orderRequest : pending) {
            if (!orderRequest.isValid()) {
                processReject(orderRequest);
            }
        }
    }

    @Override
    public void clear() {
        pending.clear();
    }

    private void processPending() {
        // Batch all orders to send in one event message
        OrderSendEvent orderSendEvent = OrderSendEvent.newInstance();
        // iterate through all requests
        for (OrderRequest request : pending) {
            if (request.isValid()) {
                processRequest(request, orderSendEvent);
            }
        }

        if (orderSendEvent.hasOrders()) {
            // post the orders to be executed
            eventAggregator.publish(orderSendEvent);
        }
    }

    private void processRequest(OrderRequest request, OrderSendEvent orderSendEvent) {
        // ensure that this trading state is reflected in the request
        request.setInstrumentId(instrumentId);
        request.setMarketId(marketId);
        // for new or modified order requests validate request against configured order filters
        if (request.getOrderRequestType() == OrderRequestType.Create ||
                request.getOrderRequestType() == OrderRequestType.Amend) {
            if (!orderFilter.check(request)) {
                // failed, the failed filter will do the logging
                return;
            }
        }
        // create the order based on a valid request
        Order order = createOrder(request);
        // add to the send event
        orderSendEvent.add(order);
    }

    private Order createOrder(OrderRequest request) {
        // create the order

        // add to the order book

        // To be implemented
        return new Order();
    }

    private void processReject(OrderRequest orderRequest) {
        // To be implemented
    }
}
