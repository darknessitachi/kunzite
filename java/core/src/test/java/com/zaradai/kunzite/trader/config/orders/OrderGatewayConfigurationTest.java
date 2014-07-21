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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class OrderGatewayConfigurationTest {
    private static final GatewayConfig GATEWAY_1 = mock(GatewayConfig.class);
    private static final GatewayConfig GATEWAY_2 = mock(GatewayConfig.class);

    @Test
    public void shouldBeEmptyOnConstruction() throws Exception {
        OrderGatewayConfiguration uut = new OrderGatewayConfiguration();

        assertThat(uut.getGateways(), emptyIterable());
    }

    @Test
    public void shouldReturnAllInsertedConfigs() throws Exception {
        OrderGatewayConfiguration uut = new OrderGatewayConfiguration();
        uut.add(GATEWAY_1);
        uut.add(GATEWAY_2);

        assertThat(uut.getGateways(), containsInAnyOrder(GATEWAY_1, GATEWAY_2));
    }
}
