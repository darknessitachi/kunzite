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
package com.zaradai.kunzite.trader.control;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.marketdata.MarketBook;
import com.zaradai.kunzite.trader.marketdata.MarketBookFactory;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.execution.OrderManager;
import com.zaradai.kunzite.trader.orders.execution.OrderManagerFactory;
import com.zaradai.kunzite.trader.positions.PositionBook;
import com.zaradai.kunzite.trader.positions.PositionBookFactory;

public class InstrumentTradingState implements TradingState {
    private final Instrument instrument;
    private final MarketBook marketBook;
    private final PositionBook positionBook;
    private final OrderManager orderManager;

    @Inject
    InstrumentTradingState(MarketBookFactory marketBookFactory, PositionBookFactory positionBookFactory,
                           OrderManagerFactory orderManagerFactory, @Assisted Instrument instrument) {
        this.instrument = instrument;
        marketBook = marketBookFactory.create(instrument);
        positionBook = positionBookFactory.create(instrument);
        orderManager = orderManagerFactory.create(instrument);
    }

    @Override
    public Instrument getInstrument() {
        return instrument;
    }

    @Override
    public MarketBook getMarketBook() {
        return marketBook;
    }

    @Override
    public PositionBook getPositionBook() {
        return positionBook;
    }

    @Override
    public OrderBook getOrderBook() {
        return orderManager.getBook();
    }

    @Override
    public OrderManager getOrderManager() {
        return orderManager;
    }
}
