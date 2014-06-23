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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceEntryTest {
    private static final double TEST_ENTRY = 42;

    @Test
    public void shouldCreateNewEntryInstance() throws Exception {
        PriceEntry uut = PriceEntry.newEntry(TEST_ENTRY);

        assertThat(uut.getPrice(), is(TEST_ENTRY));
        assertThat(uut.hasBuyOrders(), is(false));
        assertThat(uut.hasSellOrders(), is(false));
    }

    @Test
    public void shouldAddBuyOrder() throws Exception {
        PriceEntry uut = PriceEntry.newEntry(TEST_ENTRY);
        Order order = new Order();
        order.setSide(OrderSide.Buy);

        uut.add(order);

        assertThat(uut.hasBuyOrders(), is(true));
        assertThat(uut.getBuyOrders().size(), is(1));
    }

    @Test
    public void shouldRemoveBuyOrder() throws Exception {
        PriceEntry uut = PriceEntry.newEntry(TEST_ENTRY);
        Order order = new Order();
        order.setSide(OrderSide.Buy);
        uut.add(order);

        uut.remove(order);

        assertThat(uut.hasBuyOrders(), is(false));
        assertThat(uut.getBuyOrders().size(), is(0));
    }

    @Test
    public void shouldAddSellOrder() throws Exception {
        PriceEntry uut = PriceEntry.newEntry(TEST_ENTRY);
        Order order = new Order();
        order.setSide(OrderSide.Sell);

        uut.add(order);

        assertThat(uut.hasSellOrders(), is(true));
        assertThat(uut.getSellOrders().size(), is(1));
    }

    @Test
    public void shouldRemoveSellOrder() throws Exception {
        PriceEntry uut = PriceEntry.newEntry(TEST_ENTRY);
        Order order = new Order();
        order.setSide(OrderSide.Sell);
        uut.add(order);

        uut.remove(order);

        assertThat(uut.hasSellOrders(), is(false));
        assertThat(uut.getSellOrders().size(), is(0));
    }


    private Order createOrder(OrderSide orderSide, DateTime created, double price) {
        final OrderState orderState = mock(OrderState.class);
        when(orderState.getCreated()).thenReturn(created);
        when(orderState.getPrice()).thenReturn(price);

        return new Order() {
            @Override
            protected OrderState createOrderState() {
                return orderState;
            }
        };
    }
}
