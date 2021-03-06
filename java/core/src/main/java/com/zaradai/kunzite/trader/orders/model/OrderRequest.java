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

public class OrderRequest {
    private OrderRequestType orderRequestType;
    private String portfolioId;
    private String instrumentId;
    private String clientOrderId;
    private String algoId;
    private long quantity;
    private double price;
    private OrderSide side;
    private OrderType type;
    private OrderRejectReason rejectReason;
    private boolean valid;
    private String marketId;
    private String dependentOrderId;    // amend or cancel need an existing order id to work on
    private String brokerId;
    private OrderTimeInForce timeInForce;
    private DateTime created;

    public OrderRequest() {
        valid = true;
    }

    public OrderRequestType getOrderRequestType() {
        return orderRequestType;
    }

    public void setOrderRequestType(OrderRequestType orderRequestType) {
        this.orderRequestType = orderRequestType;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getAlgoId() {
        return algoId;
    }

    public void setAlgoId(String algoId) {
        this.algoId = algoId;
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

    public boolean isBuy() {
        return this.side == OrderSide.Buy || this.side == OrderSide.Cover_Short;
    }

    public boolean isSell() {
        return !isBuy();
    }

    public void reject(OrderRejectReason reason) {
        rejectReason = reason;
        valid = false;
    }

    public OrderRejectReason getRejectReason() {
        return rejectReason;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getDependentOrderId() {
        return dependentOrderId;
    }

    public void setDependentOrderId(String dependentOrderId) {
        this.dependentOrderId = dependentOrderId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    public OrderTimeInForce getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(OrderTimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }
}
