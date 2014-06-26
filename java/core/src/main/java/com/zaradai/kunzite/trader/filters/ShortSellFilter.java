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
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class ShortSellFilter implements Filter {
    private final ContextLogger logger;
    private final TradingStateResolver stateResolver;
    private final FilterParameterManager filterParameterManager;

    @Inject
    ShortSellFilter(ContextLogger logger, TradingStateResolver stateResolver,
                    FilterParameterManager filterParameterManager) {
        this.logger = logger;
        this.stateResolver = stateResolver;
        this.filterParameterManager = filterParameterManager;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, Constants.INVALID_ORDER_REQUEST);

        if (orderRequest.isSell() && (!getAllowShort(orderRequest))) {
            // get the trading state for this instrument
            TradingState state = checkNotNull(stateResolver.resolveTradingState(orderRequest.getInstrumentId()),
                    Constants.INVALID_INSTRUMENT_REQUEST);
            // get possible short position, i.e. ignore outstanding buy as all the sells may be executed first
            // leaving us short.
            long possibleShortestPosition =
                            state.getPositionBook().getTotalNetPosition() -
                            state.getOrderBook().getOutstandingSellQuantity() -
                            orderRequest.getQuantity();

            if (possibleShortestPosition < 0) {
                logFail(orderRequest.getQuantity(), possibleShortestPosition);
                orderRequest.reject(OrderRejectReason.ShortSell);
                return false;
            }
        }

        return true;
    }

    private void logFail(long quantity, long position) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Quantity", quantity)
                .add("Possible Short Position", position)
                .log();
    }

    private boolean getAllowShort(OrderRequest request) {
        FilterRequest filterRequest = FilterRequest.newInstance(request.getInstrumentId(),request.getPortfolioId());
        return filterParameterManager.allowShort(filterRequest);
    }

    @Override
    public String getName() {
        return Constants.SHORT_SELL_FILTER_NAME;
    }
}
