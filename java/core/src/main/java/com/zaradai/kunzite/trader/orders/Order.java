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
package com.zaradai.kunzite.trader.orders;

import org.joda.time.DateTime;

import java.util.Comparator;

public class Order {
    private String exchangeOrderId;
    private String orderId;
    private String clientOrderId;
    private String instrumentId;
    private String portfolioId;
    private String strategyId;
    private String marketId;
    private OrderSide side;
    private OrderType type;
    private OrderTimeInForce timeInForce;
    private DateTime created;
    private final OrderState state;

    public Order() {
        state = createOrderState();
    }

    protected OrderState createOrderState() {
        return new OrderState();
    }

    public String getExchangeOrderId() {
        return exchangeOrderId;
    }

    public void setExchangeOrderId(String exchangeOrderId) {
        this.exchangeOrderId = exchangeOrderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(String strategyId) {
        this.strategyId = strategyId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
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

    public DateTime getCreated() {
        return state.getCreated();
    }

    public double getPrice() {
        return state.getPrice();
    }

    public boolean isBuy() {
        return (side == OrderSide.Buy || side == OrderSide.Cover_Short);
    }

    public boolean isSell() {
        return (side == OrderSide.Sell || side == OrderSide.Sell_Short);
    }

    //PriceComparator
    public static Comparator<? super Order> LimitPriceComparator = new Comparator<Order>() {
        public int compare(Order o1, Order o2) {
            return (int) (o1.getPrice() - o2.getPrice());
        }
    };

    //TimeComparator
    public static final Comparator<? super Order> TimeComparator = new Comparator<Order>() {
        public int compare(Order o1, Order o2) {
            return o1.compareCreateTimeTo(o2);
        }
    };

    private int compareCreateTimeTo(Order o2) {
        return (int) (getCreated().getMillis() - o2.getCreated().getMillis());
    }
}
