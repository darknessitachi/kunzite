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

/**
 * Represents the latest order entry sent to the exchange.  Given an order may
 * be created, amended, cancel they may be more than one OrderEntry per order.  The
 * last one being the current state.
 */
public class OrderEntry {
    private OrderRequestType requestType;
    private OrderSide side;
    private OrderType type;
    private OrderTimeInForce timeInForce;
    private long quantity;
    private double price;
    private OrderLatency latency;

    public OrderEntry() {
        latency = OrderLatency.newInstance();
    }

    public OrderEntry(DateTime created) {
        latency = OrderLatency.newInstanceFrom(created);
    }

    public OrderRequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(OrderRequestType requestType) {
        this.requestType = requestType;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderTimeInForce getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(OrderTimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public OrderLatency getLatency() {
        return latency;
    }
}
