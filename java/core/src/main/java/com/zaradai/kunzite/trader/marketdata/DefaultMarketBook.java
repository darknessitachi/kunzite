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
package com.zaradai.kunzite.trader.marketdata;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.trader.instruments.Instrument;

public class DefaultMarketBook implements MarketBook {
    public static final int MAX_BOOK_DEPTH = 10;
    private final MarketBookItem bids[] = new MarketBookItem[MAX_BOOK_DEPTH];
    private final MarketBookItem asks[] = new MarketBookItem[MAX_BOOK_DEPTH];
    private double prevClose;
    private double open;
    private double high;
    private double low;
    private final MarketBookItem lastTraded;
    private final Instrument instrument;

    @Inject
    DefaultMarketBook(@Assisted Instrument instrument) {
        this.lastTraded = new MarketBookItem(0L, 0.0);
        this.instrument = instrument;
    }

    @Override
    public String getInstrumentId() {
        return instrument.getId();
    }

    @Override
    public MarketBookItem getLastTrade() {
        return lastTraded;
    }

    @Override
    public double getLastTradedPrice() {
        return lastTraded.getPrice();
    }

    @Override
    public long getLastTradedSize() {
        return lastTraded.getSize();
    }

    @Override
    public double bestBid() {
        return getBid(0);
    }

    @Override
    public long bestBidSize() {
        return getBidSize(0);
    }

    @Override
    public double bestAsk() {
        return getAsk(0);
    }

    @Override
    public long bestAskSize() {
        return getAskSize(0);
    }

    @Override
    public double getBid(int level) {
        MarketBookItem bid = getBidItem(level);
        return (bid != null) ? bid.getPrice() : 0.0;
    }

    @Override
    public long getBidSize(int level) {
        MarketBookItem bid = getBidItem(level);
        return (bid != null) ? bid.getSize() : 0;
    }

    @Override
    public double getAsk(int level) {
        MarketBookItem ask = getAskItem(level);
        return (ask != null) ? ask.getPrice() : 0.0;
    }

    @Override
    public long getAskSize(int level) {
        MarketBookItem ask = getAskItem(level);
        return (ask != null) ? ask.getSize() : 0;
    }

    @Override
    public int getAskDepth() {
        int res = 0;

        for (int i = 0; i < MAX_BOOK_DEPTH; ++i) {
            if (getAskItem(i) == null) {
                return res;
            } else {
                res++;
            }
        }

        return res;
    }

    @Override
    public int getBidDepth() {
        int res = 0;

        for (int i = 0; i < MAX_BOOK_DEPTH; ++i) {
            if (getBidItem(i) == null) {
                return res;
            } else {
                res++;
            }
        }

        return res;
    }

    @Override
    public double getPrevClose() {
        return prevClose;
    }

    @Override
    public double getOpen() {
        return open;
    }

    @Override
    public double getHigh() {
        return high;
    }

    @Override
    public double getLow() {
        return low;
    }

    @Override
    public void reset() {
        for (int i = 0; i < MAX_BOOK_DEPTH; ++i) {
            bids[i] = null;
            asks[i] = null;
        }
    }

    @Override
    public void setSize(Side side, int depth, long size) {
        if (!validLevel(depth)) {
            return;
        }

        MarketBookItem item;

        if (side == Side.Bid) {
            item = getBidItem(depth);

            if (item == null) {
                item = new MarketBookItem(0L, 0.0);
                bids[depth] = item;
            }
        } else {
            item = getAskItem(depth);

            if (item == null) {
                item = new MarketBookItem(0L, 0.0);
                asks[depth] = item;
            }
        }

        item.setSize(size);
    }

    @Override
    public void setPrice(Side side, int depth, double price) {
        if (!validLevel(depth)) {
            return;
        }

        MarketBookItem item;

        if (side == Side.Bid) {
            item = getBidItem(depth);

            if (item == null) {
                item = new MarketBookItem(0L, 0.0);
                bids[depth] = item;
            }
        } else {
            item = getAskItem(depth);

            if (item == null) {
                item = new MarketBookItem(0L, 0.0);
                asks[depth] = item;
            }
        }

        item.setPrice(price);
    }

    @Override
    public void setPrevClose(double value) {
        prevClose = value;
    }

    @Override
    public void setOpen(double value) {
        open = value;
    }

    @Override
    public void setHigh(double value) {
        high = value;
    }

    @Override
    public void setLow(double value) {
        low = value;
    }

    private MarketBookItem getBidItem(int level) {
        if (validLevel(level)) {
            return bids[level];
        }

        return null;
    }

    private MarketBookItem getAskItem(int level) {
        if (validLevel(level)) {
            return asks[level];
        }

        return null;
    }

    private boolean validLevel(int level) {
        return (level >= 0 && level < MAX_BOOK_DEPTH);
    }
}
