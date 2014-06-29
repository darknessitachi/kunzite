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
package com.zaradai.kunzite.trader.events;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class OrderStatusEventTest {
    public static final OrderStatus TEST_STATUS = OrderStatus.Calculated;
    private static final String TEST_STRING = "test";
    private static final long TEST_QTY = 1234L;
    private static final double TEST_DOUBLE = 12.34;

    @Test
    public void shouldGetOrderStatus() throws Exception {
        OrderStatusEvent uut = new OrderStatusEvent();
        uut.setOrderStatus(TEST_STATUS);

        assertThat(uut.getOrderStatus(), is(TEST_STATUS));
    }

    @Test
    public void shouldGetExchangeId() throws Exception {
        OrderStatusEvent uut = new OrderStatusEvent();
        uut.setExchangeId(TEST_STRING);

        assertThat(uut.getExchangeId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetExecQty() throws Exception {
        OrderStatusEvent uut = new OrderStatusEvent();
        uut.setExecQty(TEST_QTY);

        assertThat(uut.getExecQty(), is(TEST_QTY));
    }

    @Test
    public void shouldGetLastPx() throws Exception {
        OrderStatusEvent uut = new OrderStatusEvent();
        uut.setLastPx(TEST_DOUBLE);

        assertThat(uut.getLastPx(), is(TEST_DOUBLE));
    }
}
