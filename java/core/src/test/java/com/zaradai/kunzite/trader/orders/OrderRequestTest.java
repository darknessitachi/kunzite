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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderRequestTest {
    private static final OrderRequestType TEST_REQUEST_TYPE = OrderRequestType.Amend;
    private static final String TEST_STRING = "test";
    private static final long TEST_LONG = 12345L;
    private static final double TEST_DOUBLE = 12.34;
    private static final OrderSide TEST_SIDE = OrderSide.Cover_Short;
    private static final OrderType TEST_TYPE = OrderType.Stop_Limit;
    private static final OrderRejectReason TEST_REJECT_REASON = OrderRejectReason.MaxLong;
    private static final boolean TEST_VALID = false;

    private OrderRequest uut;

    @Before
    public void setUp() throws Exception {
        uut = new OrderRequest();
    }

    @Test
    public void shouldGetOrderRequestType() throws Exception {
        uut.setOrderRequestType(TEST_REQUEST_TYPE);

        assertThat(uut.getOrderRequestType(), is(TEST_REQUEST_TYPE));
    }

    @Test
    public void shouldGetPortfolioId() throws Exception {
        uut.setPortfolioId(TEST_STRING);

        assertThat(uut.getPortfolioId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetInstrumentId() throws Exception {
        uut.setInstrumentId(TEST_STRING);

        assertThat(uut.getInstrumentId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetClientOrderId() throws Exception {
        uut.setClientOrderId(TEST_STRING);

        assertThat(uut.getClientOrderId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetStrategyId() throws Exception {
        uut.setStrategyId(TEST_STRING);

        assertThat(uut.getStrategyId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetQuantity() throws Exception {
        uut.setQuantity(TEST_LONG);

        assertThat(uut.getQuantity(), is(TEST_LONG));
    }

    @Test
    public void shouldGetPrice() throws Exception {
        uut.setPrice(TEST_DOUBLE);

        assertThat(uut.getPrice(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetSide() throws Exception {
        uut.setSide(TEST_SIDE);

        assertThat(uut.getSide(), is(TEST_SIDE));
    }

    @Test
    public void shouldGetType() throws Exception {
        uut.setType(TEST_TYPE);

        assertThat(uut.getType(), is(TEST_TYPE));
    }

    @Test
    public void shouldGetSizeRequestIsOnIfBuy() throws Exception {
        uut.setSide(OrderSide.Buy);

        assertThat(uut.isBuy(), is(true));
        assertThat(uut.isSell(), is(false));
    }

    @Test
    public void shouldGetSizeRequestIsOnIfSell() throws Exception {
        uut.setSide(OrderSide.Sell);

        assertThat(uut.isBuy(), is(false));
        assertThat(uut.isSell(), is(true));
    }

    @Test
    public void shouldGetRejectReason() throws Exception {
        uut.reject(TEST_REJECT_REASON);

        assertThat(uut.getRejectReason(), is(TEST_REJECT_REASON));
        assertThat(uut.isValid(), is(false));
    }

    @Test
    public void shouldBeValidAfterConstruction() throws Exception {
        assertThat(uut.isValid(), is(true));
    }

    @Test
    public void shouldGetValid() throws Exception {
        uut.setValid(TEST_VALID);

        assertThat(uut.isValid(), is(TEST_VALID));
    }
}
