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
import com.zaradai.kunzite.trader.orders.model.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class RestrictedListFilter implements Filter {
    private final ContextLogger logger;
    private final FilterParameterManager filterParameterManager;

    @Inject
    RestrictedListFilter(ContextLogger logger, FilterParameterManager filterParameterManager) {
        this.logger = logger;
        this.filterParameterManager = filterParameterManager;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, Constants.INVALID_ORDER_REQUEST);

        Set<String> restricted = getRestrictedList(orderRequest);

        if (restricted.contains(orderRequest.getInstrumentId())) {
            logFail(orderRequest.getInstrumentId());
            orderRequest.reject(OrderRejectReason.RestrictedList);
            return false;
        }

        return true;
    }

    private void logFail(String instrumentId) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .addReason("Instrument is restricted")
                .add("Inst", instrumentId)
                .log();
    }

    private Set<String> getRestrictedList(OrderRequest request) {
        FilterRequest filterRequest = FilterRequest.newInstance(request.getInstrumentId(),request.getPortfolioId());
        return filterParameterManager.getRestrictedList(filterRequest);
    }

    @Override
    public String getName() {
        return Constants.RESTRICTED_LIST_FILTER_NAME;
    }
}
