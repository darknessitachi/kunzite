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

import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.marketdata.MarketBook;
import com.zaradai.kunzite.trader.orders.book.OrderBook;
import com.zaradai.kunzite.trader.orders.execution.OrderManager;
import com.zaradai.kunzite.trader.positions.PositionBook;

public interface TradingState {
    Instrument getInstrument();
    MarketBook getMarketBook();
    PositionBook getPositionBook();
    OrderBook getOrderBook();
    OrderManager getOrderManager();
}
