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
package com.zaradai.kunzite.trader.algo;

import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.events.MarketBookUpdateEventHandler;
import com.zaradai.kunzite.trader.events.OrderRejectHandler;
import com.zaradai.kunzite.trader.events.PositionChangeHandler;
import com.zaradai.kunzite.trader.events.TradeEventHandler;

public interface Algo extends MarketBookUpdateEventHandler, OrderRejectHandler, TradeEventHandler, PositionChangeHandler {
    String getId();
    TradingState getState();

    void initialize() throws AlgoException;
    void start() throws AlgoException;
    void stop() throws AlgoException;


    boolean isRunning();
}
