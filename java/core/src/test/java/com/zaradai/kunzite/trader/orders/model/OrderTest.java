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
        OrderRefData test = OrderRefData.builder().build();
        Order uut = new Order(test);

        assertThat(uut.getRefData(), is(test));
    }

    @Test
    public void shouldGetState() throws Exception {
        Order uut = new Order(OrderRefData.builder().build());

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
}
