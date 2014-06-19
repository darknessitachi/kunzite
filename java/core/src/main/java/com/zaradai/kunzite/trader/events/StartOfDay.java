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

import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkArgument;

public final class StartOfDay {
    private final String portfolioId;
    private final String instrumentId;
    private final long position;
    private final double cashFlow;

    private StartOfDay(String portfolioId, String instrumentId, long position, double cashFlow) {
        this.portfolioId = portfolioId;
        this.instrumentId = instrumentId;
        this.position = position;
        this.cashFlow = cashFlow;
    }

    public static StartOfDay newStartOfDay(String portfolioId, String instrumentId, long position, double cashFlow) {
        checkArgument(!Strings.isNullOrEmpty(portfolioId), "Invalid portfolio");
        checkArgument(!Strings.isNullOrEmpty(instrumentId), "Invalid instrument");
        checkArgument(!Double.isNaN(cashFlow), "Invalid cash flow");

        return new StartOfDay(portfolioId, instrumentId, position, cashFlow);
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public long getPosition() {
        return position;
    }

    public double getCashFlow() {
        return cashFlow;
    }
}
