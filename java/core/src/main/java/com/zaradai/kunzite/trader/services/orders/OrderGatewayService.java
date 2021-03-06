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
package com.zaradai.kunzite.trader.services.orders;

import com.google.common.util.concurrent.Service;
import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfiguration;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;

public interface OrderGatewayService extends Service {
    void build(OrderGatewayConfiguration configuration) throws ConfigException;
    void onOrderSend(OrderSendEvent event);
    void onOrderStatus(OrderStatusEvent event);
}
