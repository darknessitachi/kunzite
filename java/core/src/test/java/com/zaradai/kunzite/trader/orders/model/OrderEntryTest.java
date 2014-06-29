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
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderEntryTest {
    private OrderEntry uut;

    @Before
    public void setUp() throws Exception {
        uut = OrderEntry.newInstance();
    }

    @Test
    public void shouldGetRequestType() throws Exception {
        OrderRequestType type = OrderRequestType.Amend;
        uut.setRequestType(type);

        assertThat(uut.getRequestType(), is(type));
    }

    @Test
    public void shouldGetSide() throws Exception {
        OrderSide side = OrderSide.Buy;
        uut.setSide(side);

        assertThat(uut.getSide(), is(side));
    }

    @Test
    public void shouldGetType() throws Exception {
        OrderType type = OrderType.Market;
        uut.setType(type);

        assertThat(uut.getType(), is(type));
    }

    @Test
    public void shouldGetTimeInForce() throws Exception {
        OrderTimeInForce tif = OrderTimeInForce.AtTheClose;
        uut.setTimeInForce(tif);

        assertThat(uut.getTimeInForce(), is(tif));
    }

    @Test
    public void shouldGetQuantity() throws Exception {
        long qty = 1000L;
        uut.setQuantity(qty);

        assertThat(uut.getQuantity(), is(qty));
    }

    @Test
    public void shouldGetPrice() throws Exception {
        double prc = 34.56;
        uut.setPrice(prc);

        assertThat(uut.getPrice(), is(prc));
    }

    @Test
    public void shouldGetLatency() throws Exception {
        assertThat(uut.getLatency(), not(nullValue()));
    }

    @Test
    public void shouldCreateLatencyWithCreatedTime() throws Exception {
        DateTime created = DateTime.now();
        OrderEntry uut = OrderEntry.newInstanceWithCreated(created);

        assertThat(uut.getLatency().getCreated(), is(created));
    }

    @Test
    public void shouldGetExchangeId() throws Exception {
        String id = "test";
        uut.setExchangeId(id);

        assertThat(uut.getExchangeId(), is(id));
    }
}
