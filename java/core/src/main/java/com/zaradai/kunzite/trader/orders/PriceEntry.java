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

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Will support Order Book capability to view an orders time priority
 */
public final class PriceEntry {
    private static final int ORDER_QUEUE_INITIAL_CAPACITY = 100;

    private final double price;
    private final Queue<Order> buySide;
    private final Queue<Order> sellSide;

    private PriceEntry(double price) {
        this.price = price;
        buySide = createOrderQueue();
        sellSide = createOrderQueue();
    }

    private Queue<Order> createOrderQueue() {
        return new PriorityQueue<Order>(ORDER_QUEUE_INITIAL_CAPACITY, Order.TimeComparator);
    }

    public static PriceEntry newEntry(double entry) {
        checkArgument(!Double.isNaN(entry), "Invalid entry specified");

        return new PriceEntry(entry);
    }

    void add(Order order) {
        if (order.isBuy()) {
            this.buySide.offer(order);
        } else {
            this.sellSide.offer(order);
        }
    }

    void remove(Order order) {
        if (order.isBuy()) {
            this.buySide.remove(order);
        } else {
            this.sellSide.remove(order);
        }
    }

    boolean hasBuyOrders() {
        return this.buySide.size() > 0;
    }

    boolean hasSellOrders() {
        return this.sellSide.size() > 0;
    }

    List<Order> getBuyOrders() {
        return ImmutableList.copyOf(buySide);
    }

    List<Order> getSellOrders() {
        return ImmutableList.copyOf(sellSide);
    }

    public double getPrice() {
        return price;
    }
}
