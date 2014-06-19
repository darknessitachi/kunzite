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

public final class MarketDataField {
    private final MarketDataFieldType type;
    private final double doubleValue;
    private final long longValue;

    private MarketDataField(MarketDataFieldType type, double doubleValue) {
        this.type = type;
        this.doubleValue = doubleValue;
        this.longValue = 0L;
    }

    private MarketDataField(MarketDataFieldType type, long longValue) {
        this.type = type;
        this.longValue = longValue;
        this.doubleValue = 0.0;
    }

    public static MarketDataField newDoubleValue(MarketDataFieldType type, double doubleValue) {
        return new MarketDataField(type, doubleValue);
    }

    public static MarketDataField newLongValue(MarketDataFieldType type, long longValue) {
        return new MarketDataField(type, longValue);
    }

    public MarketDataFieldType getType() {
        return type;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public long getLongValue() {
        return longValue;
    }
}
