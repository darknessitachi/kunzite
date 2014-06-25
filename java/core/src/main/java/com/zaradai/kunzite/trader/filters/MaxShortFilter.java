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

import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.control.TradingStateResolver;
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class MaxShortFilter implements Filter {
    static final String FILTER_NAME = "Max Short";

    private final ContextLogger logger;
    private final TradingStateResolver stateResolver;
    private final FilterParameterManager filterParameterManager;

    public MaxShortFilter(ContextLogger logger, TradingStateResolver stateResolver,
                          FilterParameterManager filterParameterManager) {
        this.logger = logger;
        this.stateResolver = stateResolver;
        this.filterParameterManager = filterParameterManager;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, "Invalid Order request");

        if (orderRequest.isSell()) {
            // get the trading state for this instrument
            TradingState state = stateResolver.resolveTradingState(orderRequest.getInstrumentId());
            // get expected total position which includes outstanding potential sell positions out in the market
            long totalPosition = state.getPositionBook().getTotalNetPosition() -
                    state.getOrderBook().getOutstandingSellQuantity() -
                    orderRequest.getQuantity();
            // get the limit
            long limit = getLimit(orderRequest);
            // check against limits, note negative position convention
            if (totalPosition < limit) {
                logFail(totalPosition, limit);
                orderRequest.reject(OrderRejectReason.MaxShort);
                return false;
            }
        }

        return true;
    }

    private long getLimit(OrderRequest request) {
        FilterRequest filterRequest = FilterRequest.newInstance(request.getInstrumentId(),request.getPortfolioId());
        return filterParameterManager.getMaxShort(filterRequest);
    }

    private void logFail(long totalPosition, long limit) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Position", totalPosition)
                .add("Limit", limit)
                .log();
    }

    @Override
    public String getName() {
        return FILTER_NAME;
    }
}
