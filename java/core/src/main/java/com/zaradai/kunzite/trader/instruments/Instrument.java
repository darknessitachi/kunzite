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
package com.zaradai.kunzite.trader.instruments;

/**
 * Declares minimum fields required to define an instrument.  A concrete implementation
 * will identify the type of instrument, i.e. Stock, Bond, ...
 * The instrument is used to characterize a traded entity
 */
public abstract class Instrument {
    private String id;
    private String name;
    private double multiplier;
    private int lotSize;
    private String marketId;

    public abstract InstrumentType getType();

    /**
     * A unique identifier for this instrument typically an internationally identified symbol.
     * @return
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Common name for instrument
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * A non leveraged instrument like a stock will have 1 for its multiplier whilst
     * leveraged instruments like derivative products will be greater.
     * @return
     */
    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    /**
     * Minimum traded size
     * @return
     */
    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    /**
     * Determines which stock market this instrument is traded on.
     * @return
     */
    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
}
