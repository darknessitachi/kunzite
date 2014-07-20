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

import com.google.common.collect.Lists;
import com.zaradai.kunzite.trader.orders.model.NewOrder;

import java.util.List;

public class OrderSendEvent {
    private final List<NewOrder> orders;

    private OrderSendEvent() {
        orders = Lists.newArrayList();
    }

    public static OrderSendEvent newInstance() {
        return new OrderSendEvent();
    }

    public boolean hasOrders() {
        return orders.size() > 0;
    }

    public void add(NewOrder order) {
        orders.add(order);
    }

    public Iterable<NewOrder> getOrders() {
        return orders;
    }
}
