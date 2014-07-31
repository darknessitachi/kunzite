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
package com.zaradai.kunzite.trader.services.md.eod;

import org.joda.time.DateTime;

public class EodData implements Comparable<EodData> {
    public static final EodData START_EOD = new EodData(new DateTime(1800, 1, 1, 0, 0));

    private DateTime date;
    private String symbol;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;

    public EodData(DateTime date) {
        this.date = date;
    }

    public EodData() {

    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public static EodDataBuilder builder() {
        return new EodDataBuilder();
    }

    @Override
    public int compareTo(EodData o) {
        return date.compareTo(o.date);
    }

    public static class EodDataBuilder {
        private final EodData eodData;

        public EodDataBuilder() {
            eodData = new EodData();
        }

        public EodData build() {
            return eodData;
        }

        public EodDataBuilder symbol(String symbol) {
            eodData.setSymbol(symbol);
            return this;
        }

        public EodDataBuilder open(double open) {
            eodData.setOpen(open);
            return this;
        }

        public EodDataBuilder high(double high) {
            eodData.setHigh(high);
            return this;
        }

        public EodDataBuilder low(double low) {
            eodData.setLow(low);
            return this;
        }

        public EodDataBuilder close(double close) {
            eodData.setClose(close);
            return this;
        }

        public EodDataBuilder volumne(long volume) {
            eodData.setVolume(volume);
            return this;
        }

        public EodDataBuilder date(DateTime date) {
            eodData.setDate(date);
            return this;
        }
    }
}
