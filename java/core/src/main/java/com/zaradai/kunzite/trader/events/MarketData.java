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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

import java.util.Iterator;
import java.util.List;

public final class MarketData {
    private final String instrumentId;
    private final List<MarketDataField> fields;
    private final DateTime timestamp;

    private MarketData(String instrumentId, DateTime timestamp, List<MarketDataField> fields) {
        this.instrumentId = instrumentId;
        this.timestamp = timestamp;
        this.fields = fields;
    }

    public static MarketData newInstance(String instrumentId, DateTime timestamp, List<MarketDataField> fields) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(instrumentId), "Invalid Instrument");
        Preconditions.checkNotNull(timestamp, "Invalid timestamp");
        Preconditions.checkNotNull(fields, "Invalid fields specified");

        return new MarketData(instrumentId, timestamp, fields);
    }

    public static MarketData newInstance(String instrumentId, List<MarketDataField> fields) {
        return newInstance(instrumentId, DateTime.now(), fields);
    }

    public static MarketData newInstance(String instrumentId, DateTime timestamp, MarketDataField... elements) {
        return newInstance(instrumentId, timestamp, ImmutableList.copyOf(elements));
    }

    public static MarketData newInstance(String instrumentId, MarketDataField... elements) {
        return newInstance(instrumentId, ImmutableList.copyOf(elements));
    }

    public static MarketData newInstance(String instrumentId, DateTime timestamp, Iterable<MarketDataField> elements) {
        return newInstance(instrumentId, timestamp, ImmutableList.copyOf(elements));
    }

    public static MarketData newInstance(String instrumentId, Iterable<MarketDataField> elements) {
        return newInstance(instrumentId, ImmutableList.copyOf(elements));
    }

    public static MarketData newInstance(String instrumentId, DateTime timestamp, Iterator<MarketDataField> elements) {
        return newInstance(instrumentId, timestamp, ImmutableList.copyOf(elements));
    }

    public static MarketData newInstance(String instrumentId, Iterator<MarketDataField> elements) {
        return newInstance(instrumentId, ImmutableList.copyOf(elements));
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public List<MarketDataField> getFields() {
        return fields;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
