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
package com.zaradai.kunzite.trader.orders;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultOrderBookTest {
    private static final String TEST_ORDER_ID = "test";
    private static final Double TEST_PRICE = 42.42;
    private static final DateTime TEST_CREATED = DateTime.now();
    private static final long TEST_PENDING_OR_ON_MARKET = 1500;

    @Test
    public void shouldAddAndRememberByOrderId() throws Exception {
        Order order = createOrder();
        order.setOrderId(TEST_ORDER_ID);
        DefaultOrderBook uut = new DefaultOrderBook();

        uut.add(order);

        assertThat(uut.get(TEST_ORDER_ID), not(nullValue()));

        uut.remove(order);

        assertThat(uut.get(TEST_ORDER_ID), is(nullValue()));
    }

    @Test
    public void shouldAddAndRememberByExchangeOrderId() throws Exception {
        Order order = createOrder();
        order.setExchangeOrderId(TEST_ORDER_ID);
        DefaultOrderBook uut = new DefaultOrderBook();

        uut.add(order);

        assertThat(uut.getByExchangeId(TEST_ORDER_ID), not(nullValue()));

        uut.remove(order);

        assertThat(uut.getByExchangeId(TEST_ORDER_ID), is(nullValue()));
    }

    @Test
    public void shouldAddAndRememberByClientOrderId() throws Exception {
        Order order = createOrder();
        order.setClientOrderId(TEST_ORDER_ID);
        DefaultOrderBook uut = new DefaultOrderBook();

        uut.add(order);

        assertThat(uut.getByClientId(TEST_ORDER_ID), not(nullValue()));

        uut.remove(order);

        assertThat(uut.getByClientId(TEST_ORDER_ID), is(nullValue()));
    }

    @Test
    public void shouldAddAndRemoveMarketOrder() throws Exception {
        Order order = createOrder();
        order.setType(OrderType.Market);
        DefaultOrderBook uut = new DefaultOrderBook();

        uut.add(order);
        assertThat(uut.get(TEST_ORDER_ID), not(nullValue()));

        uut.remove(order);

        assertThat(uut.get(TEST_ORDER_ID), is(nullValue()));
    }

    private Order createOrder() {
        final OrderState orderState = mock(OrderState.class);
        when(orderState.getCreated()).thenReturn(TEST_CREATED);
        when(orderState.getPrice()).thenReturn(TEST_PRICE);
        when(orderState.getPendingOrOnMarket()).thenReturn(TEST_PENDING_OR_ON_MARKET);

        Order order = new Order() {
            @Override
            protected OrderState createOrderState() {
                return orderState;
            }
        };
        order.setOrderId(TEST_ORDER_ID);
        order.setType(OrderType.Limit);

        return order;
    }

    @Test
    public void shouldGetOutstandingBuyQuantity() throws Exception {
        DefaultOrderBook uut = new DefaultOrderBook();
        // add market order
        Order order = createOrder();
        order.setType(OrderType.Market);
        order.setSide(OrderSide.Buy);
        uut.add(order);
        // add limit order
        order = createOrder();
        order.setSide(OrderSide.Buy);
        uut.add(order);

        assertThat(uut.getOutstandingBuyQuantity(), is(TEST_PENDING_OR_ON_MARKET * 2));
    }
}
