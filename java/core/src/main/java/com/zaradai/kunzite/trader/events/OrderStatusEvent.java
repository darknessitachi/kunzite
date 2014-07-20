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

import org.joda.time.DateTime;

public class OrderStatusEvent {
    private OrderStatus orderStatus;
    private String exchangeId;
    private String orderId;
    private String algoId;
    private long execQty;
    private double lastPx;
    private DateTime timestamp;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public long getExecQty() {
        return execQty;
    }

    public void setExecQty(long execQty) {
        this.execQty = execQty;
    }

    public double getLastPx() {
        return lastPx;
    }

    public void setLastPx(double lastPx) {
        this.lastPx = lastPx;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAlgoId() {
        return algoId;
    }

    public void setAlgoId(String algoId) {
        this.algoId = algoId;
    }
}
