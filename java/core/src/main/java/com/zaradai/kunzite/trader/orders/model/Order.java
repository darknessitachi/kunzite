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

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class Order {
    private final OrderRefData refData;
    private final OrderState state;

    private Order(OrderRefData refData) {
        this.refData = refData;
        state = new OrderState();
    }

    public static Order newInstance(OrderRefData refData) {
        checkNotNull(refData, "Invalid ref data");
        return new Order(refData);
    }

    public OrderRefData getRefData() {
        return refData;
    }

    public OrderState getState() {
        return state;
    }

    public double getPrice() {
        return state.getPrice();
    }

    public long getPendingOrOnMarket() {
        return state.getQuantity();
    }

    public boolean isBuy() {
        return state.isBuy();
    }

    public boolean isSell() {
        return state.isSell();
    }

    public boolean isMarketOrder() {
        return state.isMarketOrder();
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
        return (int) (state.getCreated().getMillis() - o2.state.getCreated().getMillis());
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        final OrderRefData refData = new OrderRefData();

        public OrderBuilder id(String id) {
            refData.setOrderId(id);
            return this;
        }

        public OrderBuilder instrument(String instrumentId) {
            refData.setInstrumentId(instrumentId);
            return this;
        }

        public OrderBuilder portfolio(String portfolioId) {
            refData.setPortfolioId(portfolioId);
            return this;
        }
        public OrderBuilder client(String id) {
            refData.setClientOrderId(id);
            return this;
        }
        public OrderBuilder broker(String id) {
            refData.setBrokerId(id);
            return this;
        }
        public OrderBuilder market(String id) {
            refData.setMarketId(id);
            return this;
        }
        public OrderBuilder field(String key, String value) {
            refData.addField(key, value);
            return this;
        }

        public Order build() {
            return Order.newInstance(refData);
        }
    }
}
