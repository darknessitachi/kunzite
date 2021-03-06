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
package com.zaradai.kunzite.trader.orders.execution;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.events.OrderRequestRejectEvent;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.filters.Filter;
import com.zaradai.kunzite.trader.filters.FilterManager;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.book.OrderBookFactory;
import com.zaradai.kunzite.trader.orders.model.*;
import com.zaradai.kunzite.trader.orders.utils.OrderIdGenerator;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultOrderManager implements OrderManager {
    private final OrderStateManager orderStateManager;
    private final OrderBook orderBook;
    private final ContextLogger logger;
    private final EventAggregator eventAggregator;
    private final OrderIdGenerator idGenerator;

    private final List<OrderRequest> pending;
    private final Filter orderFilter;
    private final String instrumentId;
    private final String marketId;

    @Inject
    DefaultOrderManager(ContextLogger logger, EventAggregator eventAggregator, OrderIdGenerator idGenerator,
                               OrderStateManagerFactory orderStateManagerFactory, OrderBookFactory orderBookFactory,
                               FilterManager filterManager, @Assisted Instrument instrument) {
        this.logger = logger;
        this.eventAggregator = eventAggregator;
        this.idGenerator = idGenerator;
        orderStateManager = orderStateManagerFactory.create(this);
        orderBook = orderBookFactory.create();

        pending = createPendingList();
        instrumentId = instrument.getId();
        marketId = instrument.getMarketId();
        orderFilter = filterManager.createFor(instrument);
    }

    private List<OrderRequest> createPendingList() {
        return Lists.newArrayList();
    }

    @Override
    public void add(OrderRequest orderRequest) {
        checkNotNull(orderRequest, "Invalid Order Request");
        pending.add(orderRequest);
    }

    @Override
    public void process() {
        processPending();
        processRejects();
        clear();
    }

    @Override
    public void clear() {
        pending.clear();
    }

    @Override
    public OrderBook getBook() {
        return orderBook;
    }

    private void processRejects() {
        OrderRequestRejectEvent event = OrderRequestRejectEvent.newInstance();

        for (OrderRequest orderRequest : pending) {
            if (!orderRequest.isValid()) {
                event.add(orderRequest);
            }
        }
        // only publish if we have rejects
        if (event.hasRequests()) {
            eventAggregator.publish(event);
        }
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
        NewOrder order = createOrder(request);

        if (order != null) {
            // add to the send event if a valid order was created
            orderSendEvent.add(order);
        }
    }

    private NewOrder createOrder(OrderRequest request) {
        Order order;
        boolean newOrder = request.getOrderRequestType() == OrderRequestType.Create;

        if (newOrder) {
            OrderRefData refData = OrderRefData.builder()
                    // set a new unique id
                    .id(idGenerator.generate())
                    .instrument(request.getInstrumentId())
                    .market(request.getMarketId())
                    .portfolio(request.getPortfolioId())
                    .client(request.getClientOrderId())
                    .broker(request.getBrokerId())
                    .build();
            order = new Order(refData);
        } else {
            // get from the book
            order = orderBook.get(request.getDependentOrderId());
            // if the order is not found reject the request
            if (order == null) {
                failedToGetDependentOrder(request);
                return null;
            }
        }
        // ask the state manager to update the order based on the request
        // and return a new order to send to the market
        return orderStateManager.newRequest(order, request);
    }

    private void failedToGetDependentOrder(OrderRequest request) {
        request.reject(OrderRejectReason.DependantOrder);
        LogHelper.warn(logger)
                .addContext("Filter: Dependant Order")
                .add("Order ID", request.getDependentOrderId())
                .log();
    }
}
