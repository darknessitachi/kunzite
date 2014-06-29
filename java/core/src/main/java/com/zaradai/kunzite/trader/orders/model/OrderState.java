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

import static com.google.common.base.Preconditions.checkNotNull;

public class OrderState {
    private double price;
    private boolean pending;
    private boolean alive;
    private long quantity;
    private long execQty;
    private OrderEntry entry;
    private Order order;

    private OrderState(Order order) {
        this.order = order;
    }

    public static OrderState newInstance(Order order) {
        checkNotNull(order, "Invalid Order, cannot create state");

        return new OrderState(order);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getExecQty() {
        return execQty;
    }

    public void setExecQty(long execQty) {
        this.execQty = execQty;
    }

    public OrderEntry getEntry() {
        return entry;
    }

    public void setEntry(OrderEntry entry) {
        this.entry = entry;
    }

    public DateTime getCreated() {
        return getEntry().getLatency().getCreated();
    }

    public boolean isBuy() {
        OrderSide side = getEntry().getSide();
        return (side == OrderSide.Buy || side == OrderSide.Cover_Short);
    }

    public boolean isSell() {
        OrderSide side = getEntry().getSide();
        return (side == OrderSide.Sell || side == OrderSide.Sell_Short);
    }

    public boolean isMarketOrder() {
        return getEntry().getType() == OrderType.Market;
    }

    public long getPendingOrOnMarket() {
        if (isAlive()) {
            return getQuantity() - getExecQty();
        }

        return 0L;
    }

    public Order getOrder() {
        return order;
    }
}
