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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderTest {
    private static final String TEST_STRING = "test";
    private static final OrderSide TEST_SIDE = OrderSide.Cover_Short;
    private static final OrderType TEST_TYPE = OrderType.Stop_Limit;
    private static final OrderTimeInForce TEST_TIME_IN_FORCE = OrderTimeInForce.FillOrKill;

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
}
