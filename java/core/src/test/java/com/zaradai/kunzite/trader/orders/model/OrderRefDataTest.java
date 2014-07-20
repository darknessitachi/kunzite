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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderRefDataTest {
    private static final String TEST_STRING = "test";

    private OrderRefData uut;

    @Before
    public void setUp() throws Exception {
        uut = OrderRefData.builder().build();
    }

    @Test
    public void shouldGetOrderId() throws Exception {
        uut.setOrderId(TEST_STRING);

        assertThat(uut.getOrderId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetClientOrderId() throws Exception {
        uut.setClientOrderId(TEST_STRING);

        assertThat(uut.getClientOrderId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetInstrumentId() throws Exception {
        uut.setInstrumentId(TEST_STRING);

        assertThat(uut.getInstrumentId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetPortfolioId() throws Exception {
        uut.setPortfolioId(TEST_STRING);

        assertThat(uut.getPortfolioId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetMarketId() throws Exception {
        uut.setMarketId(TEST_STRING);

        assertThat(uut.getMarketId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetBrokerId() throws Exception {
        uut.setBrokerId(TEST_STRING);

        assertThat(uut.getBrokerId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetFieldIfExists() throws Exception {
        uut.addField("key", TEST_STRING);

        assertThat(uut.getField("key"), is(TEST_STRING));
    }

    @Test
    public void shouldReturnNullToGetFieldIfNonExistent() throws Exception {
        assertThat(uut.getField("not there"), is(nullValue()));
    }

    @Test
    public void shouldBuildWithBuilder() throws Exception {
        String TEST_BROKER_ID = "broker";
        String TEST_CLIENT_ID = "client";
        String TEST_ID = "id";
        String TEST_INS_ID = "ins";
        String TEST_MARKET_ID = "market";
        String TEST_PTF_ID = "ptf";

        OrderRefData uut = OrderRefData.builder()
                .broker(TEST_BROKER_ID)
                .client(TEST_CLIENT_ID)
                .id(TEST_ID)
                .instrument(TEST_INS_ID)
                .market(TEST_MARKET_ID)
                .portfolio(TEST_PTF_ID)
                .build();

        assertThat(uut.getBrokerId(), is(TEST_BROKER_ID));
        assertThat(uut.getClientOrderId(), is(TEST_CLIENT_ID));
        assertThat(uut.getOrderId(), is(TEST_ID));
        assertThat(uut.getInstrumentId(), is(TEST_INS_ID));
        assertThat(uut.getMarketId(), is(TEST_MARKET_ID));
        assertThat(uut.getPortfolioId(), is(TEST_PTF_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidInstrumentId() throws Exception {
        OrderRefData.builder().instrument(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidPortfolioId() throws Exception {
        OrderRefData.builder().portfolio(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidClientId() throws Exception {
        OrderRefData.builder().client(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidBrokerId() throws Exception {
        OrderRefData.builder().broker(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToBuildWithInvalidMarketId() throws Exception {
        OrderRefData.builder().market(null).build();
    }
}
