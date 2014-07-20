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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.events.TradeEvent;
import com.zaradai.kunzite.trader.orders.model.*;

public class DefaultOrderStateManager implements OrderStateManager {
    private final ContextLogger logger;
    private final EventAggregator eventAggregator;
    private final OrderManager orderManager;

    @Inject
    DefaultOrderStateManager(ContextLogger logger, EventAggregator eventAggregator, @Assisted OrderManager orderManager) {
        this.logger = logger;
        this.eventAggregator = eventAggregator;
        this.orderManager = orderManager;
    }

    @Override
    public NewOrder newRequest(Order order, OrderRequest request) {
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
        // if this is a new order request then add to order book
        if (request.getOrderRequestType() == OrderRequestType.Create) {
            orderManager.getBook().add(order);
        }
        // create the new order request
        return createNewOrder(order, entry);
    }

    private NewOrder createNewOrder(Order order, OrderEntry entry) {
        NewOrder newOrder = new NewOrder();
        newOrder.setRefData(order.getRefData());
        newOrder.setPrice(entry.getPrice());
        newOrder.setQuantity(entry.getQuantity());
        newOrder.setSide(entry.getSide());
        newOrder.setTimeInForce(entry.getTimeInForce());
        newOrder.setType(entry.getType());
        newOrder.setRequestType(entry.getRequestType());
        return newOrder;
    }

    private OrderEntry createEntry(OrderRequest request) {
        OrderRequestType requestType = request.getOrderRequestType();

        OrderEntry entry = OrderEntry.newInstanceWithCreated(request.getCreated());
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

    @Override
    public void onOrderStatus(Order order, OrderStatusEvent event) {
        OrderState state = order.getState();

        switch (event.getOrderStatus()) {
            case New:   // Outstanding order with no executions
                onNew(state, event);
                break;
            case PartiallyFilled:   // Outstanding order with executions and remaining quantity
                onPartiallyFilled(state, event);
                break;
            case Filled:
                onFilled(state, event);
                break;
            case DoneForDay:
                onDoneForDay(state, event);
                break;
            case Cancelled:
                onCancelled(state, event);
                break;
            case Replaced:
                onReplace(state, event);
                break;
            case PendingCancelReplace:
                onPendingCancelReplace(state, event);
                break;
            case Stopped:
                onStopped(state, event);
                break;
            case Rejected:
                onRejected(state, event);
                break;
            case Suspended:
                onSuspended(state, event);
                break;
            case PendingNew:
                onPendingNew(state, event);
                break;
            case Calculated:
                onCalculated(state, event);
                break;
            case Expired:
                onExpired(state, event);
                break;
        }
    }

    /**
     * Order has been accepted,
     * @param state
     * @param event
     */
    private void onNew(OrderState state, OrderStatusEvent event) {
        // live order now
        state.setPending(false);
        // market is now same as request
        OrderEntry entry = state.getEntry();
        state.setQuantity(entry.getQuantity());
        state.setPrice(entry.getPrice());
        // update the entry exchange Id
        entry.setExchangeId(event.getExchangeId());
    }

    private void onPartiallyFilled(OrderState state, OrderStatusEvent event) {
        // update executed quantity
        state.setExecQty(state.getExecQty() + event.getExecQty());
        // fire a trade event
        fireTradeEvent(state, event);
    }

    private void onFilled(OrderState state, OrderStatusEvent event) {
        // update executed quantity
        state.setExecQty(event.getExecQty());
        // order not alive
        state.setAlive(false);
        // remove from order book
        orderManager.getBook().remove(state.getOrder());
        // fire the trade event
        fireTradeEvent(state, event);
    }

    private void onDoneForDay(OrderState state, OrderStatusEvent event) {
        // order not alive
        state.setAlive(false);
        // remove from order book
        orderManager.getBook().remove(state.getOrder());
    }

    private void onCancelled(OrderState state, OrderStatusEvent event) {
        // order not alive
        state.setAlive(false);
        // remove from order book
        orderManager.getBook().remove(state.getOrder());
    }

    private void onReplace(OrderState state, OrderStatusEvent event) {
        if (state.isPending()) {
            state.setPending(false);
        }
        // market is now same as request
        OrderEntry entry = state.getEntry();
        state.setQuantity(entry.getQuantity());
        state.setPrice(entry.getPrice());
        // update the entry exchange Id
        entry.setExchangeId(event.getExchangeId());
    }

    private void onPendingCancelReplace(OrderState state, OrderStatusEvent event) {
        // Do nothing
    }

    private void onStopped(OrderState state, OrderStatusEvent event) {
        LogHelper.warn(logger)
                .addContext("Unhandled Order Status")
                .add("Status", "Stopped")
                .log();
    }

    private void onRejected(OrderState state, OrderStatusEvent event) {
        // order not alive
        state.setAlive(false);
        // remove from order book
        orderManager.getBook().remove(state.getOrder());
    }

    private void onSuspended(OrderState state, OrderStatusEvent event) {
        LogHelper.warn(logger)
                .addContext("Unhandled Order Status")
                .add("Status", "Stopped")
                .log();
    }

    private void onPendingNew(OrderState state, OrderStatusEvent event) {
        // Do nothing
    }

    private void onCalculated(OrderState state, OrderStatusEvent event) {
        LogHelper.warn(logger)
                .addContext("Unhandled Order Status")
                .add("Status", "Stopped")
                .log();
    }

    private void onExpired(OrderState state, OrderStatusEvent event) {
        // order not alive
        state.setAlive(false);
        // remove from order book
        orderManager.getBook().remove(state.getOrder());
    }


    private void fireTradeEvent(OrderState state, OrderStatusEvent event) {
        TradeEvent trade = TradeEvent.newTrade(
                state.getOrder().getRefData().getPortfolioId(),
                state.getOrder().getRefData().getInstrumentId(),
                event.getExecQty(),
                event.getLastPx(),
                event.getTimestamp()
                );

        eventAggregator.publish(trade);
    }
}
