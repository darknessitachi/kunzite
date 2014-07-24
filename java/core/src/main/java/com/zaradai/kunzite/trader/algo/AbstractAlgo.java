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
import com.zaradai.kunzite.trader.events.*;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;

public abstract class AbstractAlgo implements Algo {
    private String id;
    private TradingState tradingState;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String algoId) {
        id = algoId;
    }

    @Override
    public TradingState getState() {
        return tradingState;
    }

    @Override
    public void setState(TradingState tradingState) {
        this.tradingState = tradingState;
    }

    @Override
    public void onMarketBookUpdate(MarketBookUpdateEvent marketBookUpdateEvent) {

    }

    @Override
    public void onReject(OrderRequest request) {

    }

    @Override
    public void onPositionInitiated(PositionInitiatedEvent event) {

    }

    @Override
    public void onPositionLiquidated(PositionLiquidatedEvent event) {

    }

    @Override
    public void onPositionChanged(PositionChangedEvent event) {

    }

    @Override
    public void onTrade(TradeEvent event) {

    }
}
