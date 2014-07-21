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
package com.zaradai.kunzite.trader.config.orders;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GatewayConfigTest {
    private static final String TEST_STRING = "test";

    @Test
    public void shouldGetMarketId() throws Exception {
        GatewayConfig uut = new GatewayConfig();
        uut.setMarketId(TEST_STRING);

        assertThat(uut.getMarketId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetGatewayClass() throws Exception {
        GatewayConfig uut = new GatewayConfig();
        uut.setGatewayClass(TEST_STRING);

        assertThat(uut.getGatewayClass(), is(TEST_STRING));
    }
}
