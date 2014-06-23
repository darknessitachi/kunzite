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

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentOrderBook extends AbstractOrderBook {
    private final ReadWriteLock updateLock;

    public ConcurrentOrderBook() {
        updateLock = createUpdateLock();
    }

    protected ReadWriteLock createUpdateLock() {
        return new ReentrantReadWriteLock();
    }

    @Override
    protected Map<Double, PriceEntry> createEntryMap() {
        return Maps.newConcurrentMap();
    }

    @Override
    protected Map<String, Order> createOrderMap() {
        return Maps.newConcurrentMap();
    }

    @Override
    protected PriceEntry getOrCreateAtEntryPrice(double entryPrice) {
        ConcurrentMap<Double, PriceEntry> limitOrders = (ConcurrentMap<Double, PriceEntry>) getLimitOrders();
        PriceEntry entry = limitOrders.get(entryPrice);

        if (entry == null) {
            entry = PriceEntry.newEntry(entryPrice);
            PriceEntry previous = limitOrders.putIfAbsent(entryPrice, entry);

            if (previous != null) {
                return previous;
            }
        }

        return entry;
    }

    @Override
    protected void removeEntry(PriceEntry entry, Order order) {
        updateLock.writeLock().lock();

        try {
            entry.remove(order);
        } finally {
            updateLock.writeLock().unlock();
        }
    }

    @Override
    protected void addEntry(PriceEntry entry, Order order) {
        updateLock.writeLock().lock();

        try {
            entry.add(order);
        } finally {
            updateLock.writeLock().unlock();
        }
    }
}
