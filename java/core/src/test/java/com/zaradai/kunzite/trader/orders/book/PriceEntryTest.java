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
package com.zaradai.kunzite.trader.orders.book;

import com.zaradai.kunzite.trader.orders.model.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Queue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PriceEntryTest {

    private static final double TEST_ENTRY_PRICE = 42.42;
    @Mock
    Queue<Order> buyMock;
    @Mock
    Queue<Order> sellMock;
    private PriceEntry uut;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        uut = new PriceEntry(TEST_ENTRY_PRICE) {
            @Override
            protected Queue<Order> createBuyOrderQueue() {
                return buyMock;
            }

            @Override
            protected Queue<Order> createSellOrderQueue() {
                return sellMock;
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenConstructedWithInvalidPrice() throws Exception {
        uut = new PriceEntry(Double.NaN);
    }

    @Test
    public void shouldGetPriceForPrceEntry() throws Exception {
        uut = new PriceEntry(TEST_ENTRY_PRICE);

        assertThat(uut.getPrice(), is(TEST_ENTRY_PRICE));
    }

    @Test
    public void shouldAddBuyOrderToBuySide() throws Exception {
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(true);

        uut.add(order);

        verify(buyMock).offer(order);
    }

    @Test
    public void shouldAddSellOrderToSellSide() throws Exception {
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(false);

        uut.add(order);

        verify(sellMock).offer(order);
    }

    @Test
    public void shouldRemoveBuyOrderToBuySide() throws Exception {
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(true);

        uut.remove(order);

        verify(buyMock).remove(order);
    }

    @Test
    public void shouldRemoveSellOrderToSellSide() throws Exception {
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(false);

        uut.remove(order);

        verify(sellMock).remove(order);
    }

    @Test
    public void shouldHaveBuyOrdersIfBuyOrdersAdded() throws Exception {
        uut = new PriceEntry(TEST_ENTRY_PRICE);
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(true);

        uut.add(order);

        assertThat(uut.hasBuyOrders(), is(true));
    }

    @Test
    public void shouldHaveSellOrdersIfSellOrdersAdded() throws Exception {
        uut = new PriceEntry(TEST_ENTRY_PRICE);
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(false);

        uut.add(order);

        assertThat(uut.hasSellOrders(), is(true));
    }

    @Test
    public void shouldGetBuyOrders() throws Exception {
        uut = new PriceEntry(TEST_ENTRY_PRICE);
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(true);

        uut.add(order);

        assertThat(uut.getBuyOrders().size(), is(1));
    }

    @Test
    public void shouldGetSellOrders() throws Exception {
        uut = new PriceEntry(TEST_ENTRY_PRICE);
        Order order = mock(Order.class);
        when(order.isBuy()).thenReturn(false);

        uut.add(order);

        assertThat(uut.getSellOrders().size(), is(1));
    }
}
