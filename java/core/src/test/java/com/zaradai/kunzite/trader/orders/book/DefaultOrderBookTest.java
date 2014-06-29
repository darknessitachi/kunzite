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
import com.zaradai.kunzite.trader.orders.model.OrderRefData;
import com.zaradai.kunzite.trader.orders.model.OrderState;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultOrderBookTest {
    private static final String ORDER_ID_1 = "ord_1";
    private static final String ORDER_ID_2 = "ord_2";
    private static final String ORDER_ID_3 = "ord_3";
    private static final long QTY_1 = 2500;
    private static final long QTY_2 = 4500;
    private static final long QTY_3 = 1500;

    private DefaultOrderBook uut;
    private Order order1;
    private Order order2;
    private Order order3;

    @Before
    public void setUp() throws Exception {
        uut = new DefaultOrderBook();
        // setup mock orders common info
        order1 = mock(Order.class);
        OrderRefData refData1 = mock(OrderRefData.class);
        when(refData1.getOrderId()).thenReturn(ORDER_ID_1);
        when(order1.getRefData()).thenReturn(refData1);
        when(order1.getPendingOrOnMarket()).thenReturn(QTY_1);
        when(order1.isMarketOrder()).thenReturn(true);
        OrderState orderState1 = mock(OrderState.class);
        when(order1.getState()).thenReturn(orderState1);
        when(orderState1.getCreated()).thenReturn(DateTime.now());
        order2 = mock(Order.class);
        OrderRefData refData2 = mock(OrderRefData.class);
        when(refData2.getOrderId()).thenReturn(ORDER_ID_2);
        when(order2.getRefData()).thenReturn(refData2);
        when(order2.getPendingOrOnMarket()).thenReturn(QTY_2);
        when(order2.isMarketOrder()).thenReturn(false);
        OrderState orderState2 = mock(OrderState.class);
        when(order2.getState()).thenReturn(orderState2);
        when(orderState2.getCreated()).thenReturn(DateTime.now());
        order3 = mock(Order.class);
        OrderRefData refData3 = mock(OrderRefData.class);
        when(refData3.getOrderId()).thenReturn(ORDER_ID_3);
        when(order3.getRefData()).thenReturn(refData3);
        when(order3.getPendingOrOnMarket()).thenReturn(QTY_3);
        when(order3.isMarketOrder()).thenReturn(false);
        OrderState orderState3 = mock(OrderState.class);
        when(order3.getState()).thenReturn(orderState3);
        when(orderState3.getCreated()).thenReturn(DateTime.now());
    }

    @Test
    public void shouldAddToBuySideAndGetOutstandingBuyQuantity() throws Exception {
        when(order1.isBuy()).thenReturn(true);
        when(order2.isBuy()).thenReturn(true);
        when(order3.isBuy()).thenReturn(true);

        uut.add(order1);
        uut.add(order2);
        uut.add(order3);

        assertThat(uut.getOutstandingBuyQuantity(), is(QTY_1+QTY_2+QTY_3));
        assertThat(uut.get(ORDER_ID_1), is(order1));
        assertThat(uut.get(ORDER_ID_2), is(order2));
        assertThat(uut.get(ORDER_ID_3), is(order3));
    }

    @Test
    public void shouldAddToSellSideAndGetOutstandingSellQuantity() throws Exception {
        when(order1.isBuy()).thenReturn(false);
        when(order2.isBuy()).thenReturn(false);
        when(order3.isBuy()).thenReturn(false);

        uut.add(order1);
        uut.add(order2);
        uut.add(order3);

        assertThat(uut.getOutstandingSellQuantity(), is(QTY_1+QTY_2+QTY_3));
        assertThat(uut.get(ORDER_ID_1), is(order1));
        assertThat(uut.get(ORDER_ID_2), is(order2));
        assertThat(uut.get(ORDER_ID_3), is(order3));
    }

    @Test
    public void shouldRemoveBuyOrder() throws Exception {
        when(order1.isBuy()).thenReturn(true);
        when(order2.isBuy()).thenReturn(true);
        when(order3.isBuy()).thenReturn(true);
        uut.add(order1);
        uut.add(order2);
        uut.add(order3);

        assertThat(uut.getOutstandingBuyQuantity(), is(QTY_1+QTY_2+QTY_3));

        uut.remove(order2);

        assertThat(uut.getOutstandingBuyQuantity(), is(QTY_1+QTY_3));
        assertThat(uut.get(ORDER_ID_2), is(nullValue()));
    }

    @Test
    public void shouldRemoveSellOrder() throws Exception {
        when(order1.isBuy()).thenReturn(false);
        when(order2.isBuy()).thenReturn(false);
        when(order3.isBuy()).thenReturn(false);
        uut.add(order1);
        uut.add(order2);
        uut.add(order3);

        assertThat(uut.getOutstandingSellQuantity(), is(QTY_1+QTY_2+QTY_3));

        uut.remove(order2);

        assertThat(uut.getOutstandingSellQuantity(), is(QTY_1+QTY_3));
        assertThat(uut.get(ORDER_ID_2), is(nullValue()));
    }
}
