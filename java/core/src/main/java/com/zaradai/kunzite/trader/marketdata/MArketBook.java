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

public interface MarketBook {
    String getInstrumentId();
    MarketBookItem getLastTrade();
    double getLastTradedPrice();
    long getLastTradedSize();
    double bestBid();
    long bestBidSize();
    double bestAsk();
    long bestAskSize();
    double getBid(int level);
    long getBidSize(int level);
    double getAsk(int level);
    long getAskSize(int level);
    int getAskDepth();
    int getBidDepth();
    double getPrevClose();
    double getOpen();
    double getHigh();
    double getLow();

    void reset();
    void setSize(Side side, int depth, long size);
    void setPrice(Side side, int depth, double price);
    void setPrevClose(double value);
    void setOpen(double value);
    void setHigh(double value);
    void setLow(double value);
}
