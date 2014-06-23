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

public final class DefaultOrderBook extends AbstractOrderBook {
    @Override
    protected Map<Double, PriceEntry> createEntryMap() {
        return Maps.newHashMap();
    }

    @Override
    protected Map<String, Order> createOrderMap() {
        return Maps.newHashMap();
    }

    @Override
    protected PriceEntry getOrCreateAtEntryPrice(double entryPrice) {
        Map<Double, PriceEntry> limitOrders = getLimitOrders();
        PriceEntry entry = limitOrders.get(entryPrice);

        if (entry == null) {
            entry = PriceEntry.newEntry(entryPrice);
        }

        return entry;
    }
}
