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

import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class OrderLatencyTest {
    private final DateTime TEST_CREATED = DateTime.now();
    private final long TEST_LATENCY = 1000;
    private final DateTime TEST_ACK = TEST_CREATED.plus(TEST_LATENCY);
    private OrderLatency uut;

    @Before
    public void setUp() throws Exception {
        uut = OrderLatency.newInstanceFrom(TEST_CREATED);
    }

    @Test
    public void shouldCreateNewInstanceWithNow() throws Exception {
        uut = OrderLatency.newInstance();
        TimeUnit.MILLISECONDS.sleep(10);

        long res = DateTime.now().getMillis() - uut.getCreated().getMillis();

        assertThat(res, greaterThan(0L));
    }

    @Test
    public void shouldGetCreated() throws Exception {
        assertThat(uut.getCreated(), is(TEST_CREATED));
    }

    @Test
    public void shouldGetSending() throws Exception {
        uut.setSending(TEST_CREATED);

        assertThat(uut.getSending(), is(TEST_CREATED));
    }

    @Test
    public void shouldGetSent() throws Exception {
        uut.setSent(TEST_CREATED);

        assertThat(uut.getSent(), is(TEST_CREATED));
    }

    @Test
    public void shouldGetAck() throws Exception {
        uut.setAck(TEST_CREATED);

        assertThat(uut.getAck(), is(TEST_CREATED));
    }

    @Test
    public void shouldGetLatency() throws Exception {
        uut.setAck(TEST_ACK);

        assertThat(uut.getLatency(), is(TEST_LATENCY));
    }
}
