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

    private long startOfDay;
    private long intradayLong;
    private long intradayShort;

    private double startOfDayCashFlow;
    private double intradayLongCashFlow;
    private double intradayShortCashFlow;

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
        return getStartOfDay() + getIntradayLong() - getIntradayShort();
    }

    public double getNetCashFlow() {
        return getStartOfDayCashFlow() + getIntradayLongCashFlow() - getIntradayShortCashFlow();
    }

    public long getStartOfDay() {
        return startOfDay;
    }

    public void setStartOfDay(long startOfDay) {
        this.startOfDay = startOfDay;
    }

    public long getIntradayLong() {
        return intradayLong;
    }

    public void addLong(long position) {
        intradayLong += position;
    }

    public long getIntradayShort() {
        return intradayShort;
    }

    public void addShort(long position) {
        intradayShort += position;
    }

    public double getStartOfDayCashFlow() {
        return startOfDayCashFlow;
    }

    public void setStartOfDayCashFlow(double startOfDayCashFlow) {
        this.startOfDayCashFlow = startOfDayCashFlow;
    }

    public double getIntradayLongCashFlow() {
        return intradayLongCashFlow;
    }

    public void addLongCashFlow(double cash) {
        intradayLongCashFlow += cash;
    }

    public double getIntradayShortCashFlow() {
        return intradayShortCashFlow;
    }

    public void addShortCashFlow(double cash) {
        intradayShortCashFlow += cash;
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
        startOfDay = intradayLong = intradayShort = 0L;
        startOfDayCashFlow = intradayLongCashFlow = intradayShortCashFlow = 0.0;
        entryPrice = 0.0;
        opened = null;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Instrument", getInstrumentId())
                .add("Portfolio", getPortfolioId())
                .add("Net", getNet())
                .add("Net Cash", getNetCashFlow())
                .add("Day Long", intradayLong)
                .add("Day Long Cash", intradayLongCashFlow)
                .add("Day Short", intradayShort)
                .add("Day Short Cash", intradayShortCashFlow)
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
