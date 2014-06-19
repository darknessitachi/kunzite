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
import com.zaradai.kunzite.trader.marketdata.MarketBook;
import org.joda.time.DateTime;

public final class MarketBookUpdate {
    private final String instrumentId;
    private final MarketBook marketBook;
    private final DateTime timestamp;
    private final boolean bestPriceUpdated;
    private final boolean bestSizeUpdated;
    private final boolean depthUpdated;
    private final boolean lastTradeUpdated;
    private final boolean ohlcUpdated;

    private MarketBookUpdate(String instrumentId, MarketBook marketBook, DateTime timestamp, boolean bestPriceUpdated,
                             boolean bestSizeUpdated, boolean depthUpdated, boolean lastTradeUpdated,
                             boolean ohlcUpdated) {
        this.instrumentId = instrumentId;
        this.marketBook = marketBook;
        this.timestamp = timestamp;
        this.bestPriceUpdated = bestPriceUpdated;
        this.bestSizeUpdated = bestSizeUpdated;
        this.depthUpdated = depthUpdated;
        this.lastTradeUpdated = lastTradeUpdated;
        this.ohlcUpdated = ohlcUpdated;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public MarketBook getMarketBook() {
        return marketBook;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public boolean isBestPriceUpdated() {
        return bestPriceUpdated;
    }

    public boolean isBestSizeUpdated() {
        return bestSizeUpdated;
    }

    public boolean isDepthUpdated() {
        return depthUpdated;
    }

    public boolean isLastTradeUpdated() {
        return lastTradeUpdated;
    }

    public boolean isOhlcUpdated() {
        return ohlcUpdated;
    }

    public static MarketBookUpdateBuilder builder() {
        return new MarketBookUpdateBuilder();
    }

    public static class MarketBookUpdateBuilder {
        private String instrumentId;
        private MarketBook marketBook;
        private DateTime timestamp;
        private boolean bestPriceUpdated;
        private boolean bestSizeUpdated;
        private boolean depthUpdated;
        private boolean lastTradeUpdated;
        private boolean ohlcUpdated;

        public MarketBookUpdateBuilder instrument(String instrumentId) {
            this.instrumentId = instrumentId;
            return this;
        }

        public MarketBookUpdateBuilder book(MarketBook marketBook) {
            this.marketBook = marketBook;
            return this;
        }

        public MarketBookUpdateBuilder timestamp(DateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MarketBookUpdateBuilder bestPrice(boolean bestPriceChanged) {
            this.bestPriceUpdated = bestPriceChanged;
            return this;
        }

        public MarketBookUpdateBuilder bestSize(boolean bestSizeChanged) {
            this.bestSizeUpdated = bestSizeChanged;
            return this;
        }

        public MarketBookUpdateBuilder depth(boolean depthChanged) {
            this.depthUpdated = depthChanged;
            return this;
        }

        public MarketBookUpdateBuilder ohlc(boolean ohlcChanged) {
            this.ohlcUpdated = ohlcChanged;
            return this;
        }

        public MarketBookUpdateBuilder lastTrade(boolean lastTradeUpdated) {
            this.lastTradeUpdated = lastTradeUpdated;
            return this;
        }

        public MarketBookUpdate build() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(instrumentId), "Invalid Instrument ID");
            if (timestamp == null) {
                timestamp = DateTime.now();
            }

            return new MarketBookUpdate(instrumentId, marketBook, timestamp, bestPriceUpdated, bestSizeUpdated,
                    depthUpdated, lastTradeUpdated, ohlcUpdated);
        }
    }
}
