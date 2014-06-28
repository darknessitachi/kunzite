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
package com.zaradai.kunzite.trader.orders.execution;

import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.orders.model.OrderState;
import com.zaradai.kunzite.trader.orders.model.Order;
import com.zaradai.kunzite.trader.orders.model.OrderEntry;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;
import com.zaradai.kunzite.trader.orders.model.OrderRequestType;

public class DefaultOrderStateManager implements OrderStateManager {
    @Override
    public void newRequest(Order order, OrderRequest request) {
        OrderEntry entry = createEntry(request);
        // update the state
        OrderState state = order.getState();
        state.setAlive(true);
        state.setPending(true);

        if (request.getOrderRequestType() != OrderRequestType.Cancel) {
            // update price and quantity
            state.setPrice(entry.getPrice());
            // if the order is an amend and there is more quantity on the market then don't change
            if (state.getQuantity() < entry.getQuantity()) {
                state.setQuantity(entry.getQuantity());
            }
        }
        // now set the new order entry in the state to be used by this state manager when
        // market statuses return
        state.setEntry(entry);
    }

    @Override
    public void onOrderStatus(Order order, OrderStatusEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private OrderEntry createEntry(OrderRequest request) {
        OrderRequestType requestType = request.getOrderRequestType();

        OrderEntry entry = new OrderEntry(request.getCreated());
        entry.setRequestType(requestType);

        if (requestType != OrderRequestType.Cancel) {
            entry.setPrice(request.getPrice());
            entry.setQuantity(request.getQuantity());
            entry.setSide(request.getSide());
            entry.setType(request.getType());
            entry.setTimeInForce(request.getTimeInForce());
        }

        return entry;
    }
}
