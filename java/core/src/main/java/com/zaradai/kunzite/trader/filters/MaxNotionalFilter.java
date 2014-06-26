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
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.instruments.InstrumentResolver;
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class MaxNotionalFilter implements Filter {
    private final ContextLogger logger;
    private final InstrumentResolver instrumentResolver;
    private final FilterParameterManager filterParameterManager;

    @Inject
    MaxNotionalFilter(ContextLogger logger, InstrumentResolver instrumentResolver,
                      FilterParameterManager filterParameterManager) {
        this.logger = logger;
        this.instrumentResolver = instrumentResolver;
        this.filterParameterManager = filterParameterManager;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, Constants.INVALID_ORDER_REQUEST);
        Instrument instrument = checkNotNull(instrumentResolver.resolveInstrument(orderRequest.getInstrumentId()),
                Constants.INVALID_INSTRUMENT_REQUEST);

        double notional = orderRequest.getPrice() * orderRequest.getQuantity() * instrument.getMultiplier();
        double limit = getLimit(orderRequest);

        if (notional > limit) {
            logFail(notional, limit);
            orderRequest.reject(OrderRejectReason.MaxNotional);
            return false;
        }

        return true;
    }

    private double getLimit(OrderRequest request) {
        FilterRequest filterRequest = FilterRequest.newInstance(request.getInstrumentId(),request.getPortfolioId());
        return filterParameterManager.getMaxNotional(filterRequest);
    }

    private void logFail(double notional, double limit) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Notional", notional)
                .add("Limit", limit)
                .log();
    }

    @Override
    public String getName() {
        return Constants.MAX_NOTIONAL_FILTER_NAME;
    }
}
