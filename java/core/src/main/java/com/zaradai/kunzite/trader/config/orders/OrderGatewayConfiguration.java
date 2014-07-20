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

import com.google.common.collect.Lists;

import java.util.List;

public class OrderGatewayConfiguration {
    private final List<GatewayConfig> gateways;

    public OrderGatewayConfiguration() {
        this.gateways = Lists.newArrayList();
    }

    public Iterable<GatewayConfig> getGateways() {
        return gateways;
    }

    public void add(GatewayConfig config) {
        gateways.add(config);
    }
}
