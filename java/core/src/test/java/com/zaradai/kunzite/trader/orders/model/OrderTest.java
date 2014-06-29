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
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderTest {
    @Test
    public void shouldGetRefData() throws Exception {
        OrderRefData test = new OrderRefData();
        Order uut = Order.newInstance(test);

        assertThat(uut.getRefData(), is(test));
    }

    @Test
    public void shouldGetState() throws Exception {
        Order uut = Order.builder().build();

        assertThat(uut.getState(), not(nullValue()));
        assertThat(uut.getState().getOrder(), is(uut));
    }

    @Test
    public void shouldGetPrice() throws Exception {
        final OrderState state = mock(OrderState.class);
        Order uut = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state;
            }
        };

        uut.getPrice();

        verify(state).getPrice();
    }

    @Test
    public void shouldGetPendingOrOnMarket() throws Exception {
        final OrderState state = mock(OrderState.class);
        Order uut = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state;
            }
        };

        uut.getPendingOrOnMarket();

        verify(state).getPendingOrOnMarket();
    }

    @Test
    public void shouldIsBuy() throws Exception {
        final OrderState state = mock(OrderState.class);
        Order uut = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state;
            }
        };

        uut.isBuy();

        verify(state).isBuy();
    }

    @Test
    public void shouldIsSell() throws Exception {
        final OrderState state = mock(OrderState.class);
        Order uut = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state;
            }
        };

        uut.isSell();

        verify(state).isSell();
    }

    @Test
    public void shouldIsMarketOrder() throws Exception {
        final OrderState state = mock(OrderState.class);
        Order uut = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state;
            }
        };

        uut.isMarketOrder();

        verify(state).isMarketOrder();
    }

    @Test
    public void shouldCompareUsingCreatedTime() throws Exception {
        DateTime before = DateTime.now();
        DateTime after = before.plus(1000);
        final OrderState state1 = mock(OrderState.class);
        when(state1.getCreated()).thenReturn(before);
        final OrderState state2 = mock(OrderState.class);
        when(state2.getCreated()).thenReturn(after);

        Order order1 = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state1;
            }
        };
        Order order2 = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state2;
            }
        };

        assertThat(Order.TimeComparator.compare(order1, order2), lessThan(0));
    }

    @Test
    public void shouldCompareUsingPrice() throws Exception {
        double less = 42.42;
        double more = less + 1.0;
        final OrderState state1 = mock(OrderState.class);
        when(state1.getPrice()).thenReturn(less);
        final OrderState state2 = mock(OrderState.class);
        when(state2.getPrice()).thenReturn(more);

        Order order1 = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state1;
            }
        };
        Order order2 = new Order(null) {
            @Override
            protected OrderState createOrderState() {
                return state2;
            }
        };

        assertThat(Order.LimitPriceComparator.compare(order1, order2), lessThan(0));
    }

    @Test
    public void shouldBuildWithBuilder() throws Exception {
        String TEST_BROKER_ID = "broker";
        String TEST_CLIENT_ID = "client";
        String TEST_ID = "id";
        String TEST_INS_ID = "ins";
        String TEST_MARKET_ID = "market";
        String TEST_PTF_ID = "ptf";
        String TEST_KEY = "key";
        String TEST_VALUE = "value";

        Order uut = Order.builder()
                .broker(TEST_BROKER_ID)
                .client(TEST_CLIENT_ID)
                .id(TEST_ID)
                .instrument(TEST_INS_ID)
                .market(TEST_MARKET_ID)
                .portfolio(TEST_PTF_ID)
                .field(TEST_KEY, TEST_VALUE)
                .build();

        assertThat(uut.getRefData().getBrokerId(), is(TEST_BROKER_ID));
        assertThat(uut.getRefData().getClientOrderId(), is(TEST_CLIENT_ID));
        assertThat(uut.getRefData().getOrderId(), is(TEST_ID));
        assertThat(uut.getRefData().getInstrumentId(), is(TEST_INS_ID));
        assertThat(uut.getRefData().getMarketId(), is(TEST_MARKET_ID));
        assertThat(uut.getRefData().getPortfolioId(), is(TEST_PTF_ID));
        assertThat(uut.getRefData().getField(TEST_KEY), is(TEST_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidInstrumentId() throws Exception {
        Order.builder().instrument(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidPortfolioId() throws Exception {
        Order.builder().portfolio(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidClientId() throws Exception {
        Order.builder().client(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidBrokerId() throws Exception {
        Order.builder().broker(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidMarketId() throws Exception {
        Order.builder().market(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidFieldKey() throws Exception {
        Order.builder().field(null, "test").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidFieldValue() throws Exception {
        Order.builder().field("test", null).build();
    }
}
