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
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class MaxQuantityFilter implements Filter {
    static final String FILTER_NAME = "Max Quantity";

    private final ContextLogger logger;
    private final FilterParameterManager filterParameterManager;

    @Inject
    MaxQuantityFilter(ContextLogger logger, FilterParameterManager filterParameterManager) {
        this.logger = logger;
        this.filterParameterManager = filterParameterManager;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, "Invalid Order request");

        long quantity = orderRequest.getQuantity();
        long limit = getLimit(orderRequest);

        if (quantity > limit) {
            logFail(quantity, limit);
            orderRequest.reject(OrderRejectReason.MaxQuantity);
            return false;
        }

        return true;
    }

    private long getLimit(OrderRequest request) {
        FilterRequest filterRequest = FilterRequest.newInstance(request.getInstrumentId(),request.getPortfolioId());
        return filterParameterManager.getMaxQuantity(filterRequest);
    }

    private void logFail(long quantity, long limit) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Quantity", quantity)
                .add("Limit", limit)
                .log();
    }

    @Override
    public String getName() {
        return FILTER_NAME;
    }
}
