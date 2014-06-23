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
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderTest {
    private static final String TEST_STRING = "test";
    private static final OrderSide TEST_SIDE = OrderSide.Cover_Short;
    private static final OrderType TEST_TYPE = OrderType.Stop_Limit;
    private static final OrderTimeInForce TEST_TIME_IN_FORCE = OrderTimeInForce.FillOrKill;
    private static final DateTime TEST_DATE_TIME = DateTime.now();
    private static final double TEST_DOUBLE = 42.42;

    @Test
    public void shouldGetExchangeOrderId() throws Exception {
        Order uut = new Order();

        uut.setExchangeOrderId(TEST_STRING);

        assertThat(uut.getExchangeOrderId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetOrderId() throws Exception {
        Order uut = new Order();

        uut.setOrderId(TEST_STRING);

        assertThat(uut.getOrderId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetClientOrderId() throws Exception {
        Order uut = new Order();

        uut.setClientOrderId(TEST_STRING);

        assertThat(uut.getClientOrderId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetInstrumentId() throws Exception {
        Order uut = new Order();

        uut.setInstrumentId(TEST_STRING);

        assertThat(uut.getInstrumentId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetPortfolioId() throws Exception {
        Order uut = new Order();

        uut.setPortfolioId(TEST_STRING);

        assertThat(uut.getPortfolioId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetStrategyId() throws Exception {
        Order uut = new Order();

        uut.setStrategyId(TEST_STRING);

        assertThat(uut.getStrategyId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetMarketId() throws Exception {
        Order uut = new Order();

        uut.setMarketId(TEST_STRING);

        assertThat(uut.getMarketId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetSide() throws Exception {
        Order uut = new Order();

        uut.setSide(TEST_SIDE);

        assertThat(uut.getSide(), is(TEST_SIDE));
    }

    @Test
    public void shouldGetType() throws Exception {
        Order uut = new Order();

        uut.setType(TEST_TYPE);

        assertThat(uut.getType(), is(TEST_TYPE));
    }

    @Test
    public void shouldGetTimeInForce() throws Exception {
        Order uut = new Order();

        uut.setTimeInForce(TEST_TIME_IN_FORCE);

        assertThat(uut.getTimeInForce(), is(TEST_TIME_IN_FORCE));
    }

    @Test
    public void shouldGetCreated() throws Exception {
        Order uut = createOrder(TEST_DATE_TIME, TEST_DOUBLE);

       assertThat(uut.getCreated(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetPrice() throws Exception {
        Order uut = createOrder(TEST_DATE_TIME, TEST_DOUBLE);

        assertThat(uut.getPrice(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldCompareByCreated() throws Exception {
        Order uut = createOrder(TEST_DATE_TIME, TEST_DOUBLE);
        Order same = createOrder(TEST_DATE_TIME, TEST_DOUBLE);
        Order before = createOrder(TEST_DATE_TIME.minusSeconds(1), TEST_DOUBLE);
        Order after = createOrder(TEST_DATE_TIME.plusSeconds(1), TEST_DOUBLE);

        assertThat(Order.TimeComparator.compare(uut, same), is(0));
        assertThat(Order.TimeComparator.compare(uut, before), greaterThan(0));
        assertThat(Order.TimeComparator.compare(uut, after), lessThan(0));
    }

    @Test
    public void shouldCompareByPrice() throws Exception {
        Order uut = createOrder(TEST_DATE_TIME, TEST_DOUBLE);
        Order same = createOrder(TEST_DATE_TIME, TEST_DOUBLE);
        Order less = createOrder(TEST_DATE_TIME, TEST_DOUBLE - 1.0);
        Order more = createOrder(TEST_DATE_TIME.plusSeconds(1), TEST_DOUBLE + 1.0);

        assertThat(Order.LimitPriceComparator.compare(uut, same), is(0));
        assertThat(Order.LimitPriceComparator.compare(uut, less), greaterThan(0));
        assertThat(Order.LimitPriceComparator.compare(uut, more), lessThan(0));
    }

    @Test
    public void shouldBeBuyOrSellSideIfSetAppropriately() throws Exception {
        Order uut = new Order();

        uut.setSide(OrderSide.Buy);
        assertThat(uut.isBuy(), is(true));
        assertThat(uut.isSell(), is(false));

        uut.setSide(OrderSide.Cover_Short);
        assertThat(uut.isBuy(), is(true));
        assertThat(uut.isSell(), is(false));

        uut.setSide(OrderSide.Sell);
        assertThat(uut.isBuy(), is(false));
        assertThat(uut.isSell(), is(true));

        uut.setSide(OrderSide.Sell_Short);
        assertThat(uut.isBuy(), is(false));
        assertThat(uut.isSell(), is(true));
    }

    private Order createOrder(DateTime created, double price) {
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
