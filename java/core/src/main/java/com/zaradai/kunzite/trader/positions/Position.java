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
package com.zaradai.kunzite.trader.positions;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.trader.instruments.Instrument;
import org.joda.time.DateTime;

public class Position {
    private final Portfolio portfolio;
    private final Instrument instrument;

    private long volume;
    private double entryPrice;
    private DateTime opened;

    @Inject
    Position(@Assisted Portfolio portfolio, @Assisted Instrument instrument) {
        this.portfolio = portfolio;
        this.instrument = instrument;
        reset();
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public String getPortfolioId() {
        return portfolio.getId();
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public String getInstrumentId() {
        return instrument.getId();
    }

    public long getNet() {
        return volume;
    }

    public void add(long position) {
        volume += position;
    }

    public double getEntryPrice() {
        return entryPrice;
    }

    public void setEntryPrice(double entryPrice) {
        this.entryPrice = entryPrice;
    }

    public boolean isLong() {
        return getNet() >= 0;
    }

    public boolean isActive() {
        return getNet() != 0;
    }

    public DateTime getOpened() {
        return opened;
    }

    public void setOpened(DateTime opened) {
        this.opened = opened;
    }

    public void reset() {
        volume = 0L;
        entryPrice = 0.0;
        opened = null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Instrument", getInstrumentId())
                .add("Portfolio", getPortfolioId())
                .add("Net", getNet())
                .add("Opened", getOpened().toString("YYYY-MM-dd HH:mm:ss.SSS"))
                .add("Entry", getEntryPrice())
                .toString();
    }

    /**
     * A position is unique per portfolio per instrument
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(instrument, portfolio);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Position other = (Position) obj;

        return Objects.equal(instrument, other.instrument) && Objects.equal(portfolio, other.portfolio);
    }
}
