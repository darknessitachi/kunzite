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

import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.events.OrderRequestRejectEvent;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.filters.Filter;
import com.zaradai.kunzite.trader.filters.FilterManager;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.book.OrderBookFactory;
import com.zaradai.kunzite.trader.orders.model.*;
import com.zaradai.kunzite.trader.orders.utils.OrderIdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DefaultOrderManagerTest {
    private static final String INST_ID = "inst_id";
    private static final String MARKET_ID = "mkt_id";
    private static final String PTF_ID = "ptf_id";
    private static final String CLIENT_ID = "client_id";
    private static final String BROKER_ID = "broker_id";
    private static final String ORDER_ID = "order_id";
    private static final String DEP_ID = "dep_id";


    private DefaultOrderManager uut;
    private Filter orderFilter;
    private OrderStateManager orderStateManager;
    private EventAggregator eventAggregator;
    private OrderBook orderBook;

    @Captor
    ArgumentCaptor<Order> orderArgumentCaptor;
    @Captor
    ArgumentCaptor<OrderRequest> orderRequestArgumentCaptor;
    @Captor
    ArgumentCaptor<OrderSendEvent> orderSendEventArgumentCaptor;
    @Captor
    ArgumentCaptor<OrderRequestRejectEvent> orderRequestRejectEventArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ContextLogger logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        OrderIdGenerator orderIdGenerator = mock(OrderIdGenerator.class);
        when(orderIdGenerator.generate()).thenReturn(ORDER_ID);
        OrderStateManagerFactory orderStateManagerFactory = mock(OrderStateManagerFactory.class);
        orderStateManager = mock(OrderStateManager.class);
        OrderBookFactory orderBookFactory = mock(OrderBookFactory.class);
        orderBook = mock(OrderBook.class);
        when(orderBookFactory.create()).thenReturn(orderBook);
        FilterManager filterManager = mock(FilterManager.class);
        Instrument instrument = mock(Instrument.class);
        when(instrument.getId()).thenReturn(INST_ID);
        when(instrument.getMarketId()).thenReturn(MARKET_ID);
        TradingState tradingState = mock(TradingState.class);
        when(tradingState.getInstrument()).thenReturn(instrument);
        orderFilter = mock(Filter.class);
        when(filterManager.createFor(instrument)).thenReturn(orderFilter);
        when(orderStateManagerFactory.create(any(OrderManager.class))).thenReturn(orderStateManager);
        uut = new DefaultOrderManager(logger, eventAggregator, orderIdGenerator, orderStateManagerFactory,
                orderBookFactory, filterManager, instrument);
    }

    @Test
    public void shouldProcessValidCreateOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderRequestType(OrderRequestType.Create);
        request.setPortfolioId(PTF_ID);
        request.setClientOrderId(CLIENT_ID);
        request.setBrokerId(BROKER_ID);
        when(orderFilter.check(request)).thenReturn(true);
        when(orderStateManager.newRequest(any(Order.class), any(OrderRequest.class))).thenReturn(mock(NewOrder.class));

        uut.add(request);
        uut.process();

        verify(orderStateManager).newRequest(orderArgumentCaptor.capture(), orderRequestArgumentCaptor.capture());

        assertThat(orderRequestArgumentCaptor.getValue(), is(request));
        // make sure that the order was created with correct id's
        Order order = orderArgumentCaptor.getValue();

        assertThat(order.getRefData().getMarketId(), is(MARKET_ID));
        assertThat(order.getRefData().getPortfolioId(), is(PTF_ID));
        assertThat(order.getRefData().getInstrumentId(), is(INST_ID));
        assertThat(order.getRefData().getBrokerId(), is(BROKER_ID));
        assertThat(order.getRefData().getOrderId(), is(ORDER_ID));

        // verify sent order
        verify(eventAggregator).publish(orderSendEventArgumentCaptor.capture());
        OrderSendEvent orderSendEvent = orderSendEventArgumentCaptor.getValue();

        assertThat(orderSendEvent.hasOrders(), is(true));
    }

    @Test
    public void shouldProcessValidAmendOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderRequestType(OrderRequestType.Amend);
        request.setDependentOrderId(DEP_ID);
        when(orderFilter.check(request)).thenReturn(true);
        Order amendedOrder = mock(Order.class);
        when(orderBook.get(DEP_ID)).thenReturn(amendedOrder);
        when(orderStateManager.newRequest(amendedOrder, request)).thenReturn(mock(NewOrder.class));

        uut.add(request);
        uut.process();

        verify(orderStateManager).newRequest(orderArgumentCaptor.capture(), orderRequestArgumentCaptor.capture());

        assertThat(orderRequestArgumentCaptor.getValue(), is(request));
        assertThat(orderArgumentCaptor.getValue(), is(amendedOrder));
        // verify sent order
        verify(eventAggregator).publish(orderSendEventArgumentCaptor.capture());
        OrderSendEvent orderSendEvent = orderSendEventArgumentCaptor.getValue();

        assertThat(orderSendEvent.hasOrders(), is(true));
    }

    @Test
    public void shouldNotValidateCancelledOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderRequestType(OrderRequestType.Cancel);

        uut.add(request);
        uut.process();

        verify(orderFilter, never()).check(request);
    }

    @Test
    public void shouldNotCreateOrderRequestIfDependantOrderNotFound() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderRequestType(OrderRequestType.Amend);
        request.setDependentOrderId(DEP_ID);
        when(orderBook.get(DEP_ID)).thenReturn(null);

        uut.add(request);
        uut.process();

        verify(eventAggregator, never()).publish(any(OrderSendEvent.class));
    }

    @Test
    public void shouldClearPendingAfterExecute() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderRequestType(OrderRequestType.Amend);
        request.setDependentOrderId(DEP_ID);
        when(orderFilter.check(request)).thenReturn(true);
        Order amendedOrder = mock(Order.class);
        when(orderBook.get(DEP_ID)).thenReturn(amendedOrder);
        when(orderStateManager.newRequest(amendedOrder, request)).thenReturn(mock(NewOrder.class));

        uut.add(request);
        uut.process();
        uut.process();  // should not create any additional requests

        verify(eventAggregator).publish(any(OrderSendEvent.class));
    }

    @Test
    public void shouldProcessRejectedOrders() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setOrderRequestType(OrderRequestType.Create);
        when(orderFilter.check(request)).thenReturn(false);
        request.reject(OrderRejectReason.MaxNotional);

        uut.add(request);
        uut.process();


        verify(eventAggregator).publish(orderRequestRejectEventArgumentCaptor.capture());
        OrderRequestRejectEvent event = orderRequestRejectEventArgumentCaptor.getValue();

        assertThat(event.hasRequests(), is(true));
        assertThat(event.getRejects().get(0), is(request));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToAddInvalidOrderRequest() throws Exception {
        uut.add(null);
    }

    @Test
    public void shouldGetOrderBook() throws Exception {
        assertThat(uut.getBook(), not(nullValue()));
    }
}
