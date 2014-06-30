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
import com.zaradai.kunzite.trader.events.OrderStatus;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.events.TradeEvent;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.model.*;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultOrderStateManagerTest {
    private static final long TEST_QTY = 2400L;
    private static final double TEST_PRC = 23.56;
    private static final String TEST_ID = "test";
    private static final String PTF_ID = "ptf_id";
    private static final String INS_ID = "ins_id";

    private DefaultOrderStateManager uut;
    private ContextLogger logger;
    private EventAggregator eventAggregator;
    private OrderBook orderBook;
    private OrderState state;
    private OrderEntry entry;
    private Order order;

    @Captor
    ArgumentCaptor<TradeEvent> tradeEventArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        OrderManager orderManager = mock(OrderManager.class);
        orderBook = mock(OrderBook.class);
        when(orderManager.getBook()).thenReturn(orderBook);
        // setup the status event mocks
        OrderRefData refData = mock(OrderRefData.class);
        when(refData.getPortfolioId()).thenReturn(PTF_ID);
        when(refData.getInstrumentId()).thenReturn(INS_ID);
        state = mock(OrderState.class);
        entry = mock(OrderEntry.class);
        when(state.getEntry()).thenReturn(entry);
        order = mock(Order.class);
        when(order.getState()).thenReturn(state);
        when(order.getRefData()).thenReturn(refData);
        when(state.getOrder()).thenReturn(order);

        uut = new DefaultOrderStateManager(logger, eventAggregator, orderManager);
    }

    @Test
    public void shouldGenerateNewRequestForCreate() throws Exception {
        double prc = 23.45;
        long qty = 2500;
        DateTime created = DateTime.now();
        OrderSide side = OrderSide.Buy;
        OrderType type = OrderType.Limit;
        OrderTimeInForce tif = OrderTimeInForce.AtTheOpen;
        Order order = Order.newInstance(new OrderRefData());
        OrderRequest request = new OrderRequest();
        request.setPrice(prc);
        request.setQuantity(qty);
        request.setSide(side);
        request.setType(type);
        request.setTimeInForce(tif);
        request.setOrderRequestType(OrderRequestType.Create);
        request.setCreated(created);

        uut.newRequest(order, request);

        OrderState state = order.getState();
        assertThat(state.getQuantity(), is(qty));
        assertThat(state.getPrice(), is(prc));
        assertThat(state.isAlive(), is(true));
        assertThat(state.isPending(), is(true));
        OrderEntry entry = state.getEntry();
        assertThat(entry.getPrice(), is(prc));
        assertThat(entry.getQuantity(), is(qty));
        assertThat(entry.getExchangeId(), is(nullValue()));
        assertThat(entry.getRequestType(), is(OrderRequestType.Create));
        assertThat(entry.getSide(), is(side));
        assertThat(entry.getTimeInForce(), is(tif));
        assertThat(entry.getType(), is(type));
        assertThat(state.getCreated(), is(created));
        verify(orderBook).add(order);
    }

    @Test
    public void shouldGenerateNewRequestForAmend() throws Exception {
        double prc = 23.45;
        long qty = 2500;
        DateTime created = DateTime.now();
        OrderSide side = OrderSide.Buy;
        OrderType type = OrderType.Limit;
        OrderTimeInForce tif = OrderTimeInForce.AtTheOpen;
        Order order = Order.newInstance(new OrderRefData());
        OrderRequest request = new OrderRequest();
        request.setPrice(prc);
        request.setQuantity(qty);
        request.setSide(side);
        request.setType(type);
        request.setTimeInForce(tif);
        request.setOrderRequestType(OrderRequestType.Amend);
        request.setCreated(created);

        uut.newRequest(order, request);

        OrderState state = order.getState();
        assertThat(state.getQuantity(), is(qty));
        assertThat(state.getPrice(), is(prc));
        assertThat(state.isAlive(), is(true));
        assertThat(state.isPending(), is(true));
        OrderEntry entry = state.getEntry();
        assertThat(entry.getPrice(), is(prc));
        assertThat(entry.getQuantity(), is(qty));
        assertThat(entry.getExchangeId(), is(nullValue()));
        assertThat(entry.getRequestType(), is(OrderRequestType.Amend));
        assertThat(entry.getSide(), is(side));
        assertThat(entry.getTimeInForce(), is(tif));
        assertThat(entry.getType(), is(type));
        assertThat(state.getCreated(), is(created));
        verify(orderBook, never()).add(order);  // amended orders already in the book
    }

    @Test
    public void shouldHandleNewStatusEvent() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.New);
        event.setExchangeId(TEST_ID);
        when(entry.getQuantity()).thenReturn(TEST_QTY);
        when(entry.getPrice()).thenReturn(TEST_PRC);

        uut.onOrderStatus(order, event);

        verify(state).setPending(false);
        verify(state).setQuantity(TEST_QTY);
        verify(state).setPrice(TEST_PRC);
        verify(entry).setExchangeId(TEST_ID);
    }

    @Test
    public void shouldHandlePartialFillEvent() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.PartiallyFilled);
        event.setExecQty(TEST_QTY);
        event.setLastPx(TEST_PRC);
        event.setTimestamp(DateTime.now());

        uut.onOrderStatus(order, event);

        verify(state).setExecQty(TEST_QTY);
        verify(eventAggregator).publish(tradeEventArgumentCaptor.capture());
        TradeEvent tradeEvent = tradeEventArgumentCaptor.getValue();

        assertThat(tradeEvent.getQuantity(), is(TEST_QTY));
        assertThat(tradeEvent.getPortfolioId(), is(PTF_ID));
        assertThat(tradeEvent.getPrice(), is(TEST_PRC));
        assertThat(tradeEvent.getInstrumentId(), is(INS_ID));
    }

    @Test
    public void shouldHandleFilledEvent() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Filled);
        event.setExecQty(TEST_QTY);
        event.setLastPx(TEST_PRC);
        event.setTimestamp(DateTime.now());

        uut.onOrderStatus(order, event);

        verify(state).setExecQty(TEST_QTY);
        verify(state).setAlive(false);
        verify(orderBook).remove(order);
        verify(eventAggregator).publish(tradeEventArgumentCaptor.capture());
        TradeEvent tradeEvent = tradeEventArgumentCaptor.getValue();

        assertThat(tradeEvent.getQuantity(), is(TEST_QTY));
        assertThat(tradeEvent.getPortfolioId(), is(PTF_ID));
        assertThat(tradeEvent.getPrice(), is(TEST_PRC));
        assertThat(tradeEvent.getInstrumentId(), is(INS_ID));
    }

    @Test
    public void shouldHandleDoneForDay() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.DoneForDay);

        uut.onOrderStatus(order, event);

        verify(state).setAlive(false);
        verify(orderBook).remove(order);
    }

    @Test
    public void shouldHandleCancelled() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Cancelled);

        uut.onOrderStatus(order, event);

        verify(state).setAlive(false);
        verify(orderBook).remove(order);
    }

    @Test
    public void shouldHandleReplaced() throws Exception {
        when(state.isPending()).thenReturn(true);
        when(entry.getQuantity()).thenReturn(TEST_QTY);
        when(entry.getPrice()).thenReturn(TEST_PRC);
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Replaced);
        event.setExchangeId(TEST_ID);

        uut.onOrderStatus(order, event);

        verify(state).setQuantity(TEST_QTY);
        verify(state).setPrice(TEST_PRC);
        verify(entry).setExchangeId(TEST_ID);
    }

    @Test
    public void shouldNotErrorForPendingCancelReplace() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.PendingCancelReplace);

        uut.onOrderStatus(order, event);
    }

    @Test
    public void shouldNotErrorForPendingNew() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.PendingNew);

        uut.onOrderStatus(order, event);
    }

    @Test
    public void shouldNotHandleStoppedEvent() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Stopped);

        uut.onOrderStatus(order, event);

        verify(logger).warn();
    }

    @Test
    public void shouldNotHandleSuspendedEvent() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Suspended);

        uut.onOrderStatus(order, event);

        verify(logger).warn();
    }

    @Test
    public void shouldNotHandleCalculatedEvent() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Calculated);

        uut.onOrderStatus(order, event);

        verify(logger).warn();
    }

    @Test
    public void shouldHandleRejected() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Rejected);

        uut.onOrderStatus(order, event);

        verify(state).setAlive(false);
        verify(orderBook).remove(order);
    }

    @Test
    public void shouldHandleExpired() throws Exception {
        OrderStatusEvent event = new OrderStatusEvent();
        event.setOrderStatus(OrderStatus.Expired);

        uut.onOrderStatus(order, event);

        verify(state).setAlive(false);
        verify(orderBook).remove(order);
    }
}
