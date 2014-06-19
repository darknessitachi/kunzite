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

public final class Trade {
    private final String portfolioId;
    private final String instrumentId;
    private final long quantity;
    private final double price;

    private Trade(String portfolioId, String instrumentId, long quantity, double price) {
        this.portfolioId = portfolioId;
        this.instrumentId = instrumentId;
        this.quantity = quantity;
        this.price = price;
    }

    public static Trade newTrade(String portfolioId, String instrumentId, long quantity, double price) {
        checkArgument(!Strings.isNullOrEmpty(portfolioId), "Invalid portfolio");
        checkArgument(!Strings.isNullOrEmpty(instrumentId), "Invalid instrument");
        checkArgument(!Double.isNaN(price), "Invalid price");

        return new Trade(portfolioId, instrumentId, quantity, price);
    }

    public long getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }
}
