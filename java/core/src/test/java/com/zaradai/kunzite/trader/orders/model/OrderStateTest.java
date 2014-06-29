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
package com.zaradai.kunzite.trader.orders.model;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderStateTest {
    private static final double TEST_DOUBLE = 42.42;
    private static final boolean TEST_BOOL = true;
    private static final long TEST_LONG = 42L;
    private static final OrderEntry TEST_ENTRY = mock(OrderEntry.class);
    private static final DateTime TEST_DATE_TIME = DateTime.now();

    private Order order;
    private OrderState uut;

    @Before
    public void setUp() throws Exception {
        order = mock(Order.class);
        uut = OrderState.newInstance(order);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailIfCreatedWithNullOrder() throws Exception {
        OrderState.newInstance(null);
    }

    @Test
    public void shouldGetPrice() throws Exception {
        uut.setPrice(TEST_DOUBLE);

        assertThat(uut.getPrice(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldTestIsPending() throws Exception {
        uut.setPending(TEST_BOOL);

        assertThat(uut.isPending(), is(TEST_BOOL));
    }

    @Test
    public void shouldTestIsAlive() throws Exception {
        uut.setAlive(TEST_BOOL);

        assertThat(uut.isAlive(), is(TEST_BOOL));
    }

    @Test
    public void shouldGetQuantity() throws Exception {
        uut.setQuantity(TEST_LONG);

        assertThat(uut.getQuantity(), is(TEST_LONG));
    }

    @Test
    public void shouldGetExecQty() throws Exception {
        uut.setExecQty(TEST_LONG);

        assertThat(uut.getExecQty(), is(TEST_LONG));
    }

    @Test
    public void shouldGetEntry() throws Exception {
        uut.setEntry(TEST_ENTRY);

        assertThat(uut.getEntry(), is(TEST_ENTRY));
    }

    @Test
    public void shouldGetCreated() throws Exception {
        OrderLatency latency = mock(OrderLatency.class);
        when(latency.getCreated()).thenReturn(TEST_DATE_TIME);
        when(TEST_ENTRY.getLatency()).thenReturn(latency);
        uut.setEntry(TEST_ENTRY);

        assertThat(uut.getCreated(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetIsBuy() throws Exception {
        when(TEST_ENTRY.getSide()).thenReturn(OrderSide.Buy);
        uut.setEntry(TEST_ENTRY);

        assertThat(uut.isBuy(), is(TEST_BOOL));
    }

    @Test
    public void shouldGetIsSell() throws Exception {
        when(TEST_ENTRY.getSide()).thenReturn(OrderSide.Sell);
        uut.setEntry(TEST_ENTRY);

        assertThat(uut.isSell(), is(TEST_BOOL));
    }

    @Test
    public void shouldIsMarketOrder() throws Exception {
        when(TEST_ENTRY.getType()).thenReturn(OrderType.Market);
        uut.setEntry(TEST_ENTRY);

        assertThat(uut.isMarketOrder(), is(true));
    }

    @Test
    public void shouldGetPendingOrOnMarketFromOrderQuantityMinusExecuted() throws Exception {
        long quantity = 4500;
        long executed = 200;
        long expected = quantity - executed;
        uut.setExecQty(executed);
        uut.setQuantity(quantity);
        uut.setAlive(true);

        assertThat(uut.getPendingOrOnMarket(), is(expected));
    }

    @Test
    public void shouldReturnZeroIfOrderDeadForPendingQuantity() throws Exception {
        long quantity = 4500;
        long executed = 200;
        long expected = quantity - executed;
        uut.setExecQty(executed);
        uut.setQuantity(quantity);
        uut.setAlive(false);

        assertThat(uut.getPendingOrOnMarket(), is(0L));
    }

    @Test
    public void shouldGetOrder() throws Exception {
        assertThat(uut.getOrder(), is(order));
    }
}
