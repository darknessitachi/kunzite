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
package com.zaradai.kunzite.trader.filters;

import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.control.TradingStateResolver;
import com.zaradai.kunzite.trader.orders.model.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class MaxSpreadFilter implements Filter {
    private final ContextLogger logger;
    private final TradingStateResolver stateResolver;
    private final FilterParameterManager filterParameterManager;

    @Inject
    MaxSpreadFilter(ContextLogger logger, TradingStateResolver stateResolver,
                           FilterParameterManager filterParameterManager) {
        this.logger = logger;
        this.stateResolver = stateResolver;
        this.filterParameterManager = filterParameterManager;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, Constants.INVALID_ORDER_REQUEST);
        // get the trading state for this instrument
        TradingState state = checkNotNull(stateResolver.resolveTradingState(orderRequest.getInstrumentId()),
                Constants.INVALID_INSTRUMENT_REQUEST);
        double lastTradedPrice = state.getMarketBook().getLastTradedPrice();
        double spread = Math.abs(lastTradedPrice - orderRequest.getPrice());
        // get the limit
        double limit = getLimit(orderRequest);

        if (spread > limit) {
            logFail(lastTradedPrice, spread, limit);
            orderRequest.reject(OrderRejectReason.MaxSpread);
            return false;
        }

        return true;
    }

    private void logFail(double lastTradedPrice, double spread, double limit) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Last Traded", lastTradedPrice)
                .add("Spread", spread)
                .add("Limit", limit)
                .log();
    }

    private double getLimit(OrderRequest request) {
        FilterRequest filterRequest = FilterRequest.newInstance(request.getInstrumentId(),request.getPortfolioId());
        return filterParameterManager.getMaxSpread(filterRequest);
    }

    @Override
    public String getName() {
        return Constants.MAX_SPREAD_FILTER_NAME;
    }
}
