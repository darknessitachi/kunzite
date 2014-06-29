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
package com.zaradai.kunzite.trader.orders.book;

import com.google.common.base.Strings;
import com.zaradai.kunzite.trader.orders.model.Order;

import java.util.Map;

public abstract class AbstractOrderBook implements OrderBook {
    private final Map<Double, PriceEntry> limitOrders;
    private final PriceEntry marketOrders;
    private final Map<String, Order> ordersByOrderId;

    public AbstractOrderBook() {
        ordersByOrderId = createOrderMap();
        marketOrders = PriceEntry.newEntry(0);
        limitOrders = createEntryMap();
    }

    protected abstract Map<Double,PriceEntry> createEntryMap();
    protected abstract Map<String,Order> createOrderMap();
    protected abstract PriceEntry getOrCreateAtEntryPrice(double entryPrice);

    @Override
    public void add(Order order) {
        if (order.isMarketOrder()) {
            addEntry(marketOrders, order);
        } else {
            addEntry(getOrCreateAtEntryPrice(order.getPrice()), order);
        }

        rememberOrder(order);
    }

    @Override
    public void remove(Order order) {
        if (order.isMarketOrder()) {
            removeEntry(marketOrders, order);
        } else {
            PriceEntry entry = limitOrders.get(order.getPrice());

            if (entry != null) {
                removeEntry(entry, order);
            }
        }

        forgetOrder(order);
    }

    @Override
    public long getOutstandingBuyQuantity() {
        long res = 0;
        // Process limits
        for (Map.Entry<Double, PriceEntry> entry : this.limitOrders.entrySet()) {
            PriceEntry priceEntry = entry.getValue();

            for (Order order : priceEntry.getBuyOrders()) {
                res += order.getPendingOrOnMarket();
            }
        }
        // process market orders
        for (Order order : this.marketOrders.getBuyOrders()) {
            res += order.getPendingOrOnMarket();
        }

        return res;
    }

    @Override
    public long getOutstandingSellQuantity() {
        long res = 0;
        // Process limits
        for (Map.Entry<Double, PriceEntry> entry : this.limitOrders.entrySet()) {
            PriceEntry priceEntry = entry.getValue();

            for (Order order : priceEntry.getSellOrders()) {
                res += order.getPendingOrOnMarket();
            }
        }
        // process market orders
        for (Order order : this.marketOrders.getSellOrders()) {
            res += order.getPendingOrOnMarket();
        }

        return res;
    }

    protected void removeEntry(PriceEntry entry, Order order) {
        entry.remove(order);
    }

    protected void addEntry(PriceEntry entry, Order order) {
        entry.add(order);
    }

    @Override
    public Order get(String orderId) {
        return ordersByOrderId.get(orderId);
    }

    protected Map<Double, PriceEntry> getLimitOrders() {
        return limitOrders;
    }

    private void rememberOrder(Order order) {
        String orderId = order.getRefData().getOrderId();

        if (!Strings.isNullOrEmpty(orderId)) {
            ordersByOrderId.put(orderId, order);
        }
    }


    private void forgetOrder(Order order) {
        String orderId = order.getRefData().getOrderId();

        if (!Strings.isNullOrEmpty(orderId)) {
            ordersByOrderId.remove(orderId);
        }
    }
}
