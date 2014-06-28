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

import com.google.common.base.Strings;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.orders.model.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioResolver;

import static com.google.common.base.Preconditions.checkNotNull;

public class PortfolioFilter implements Filter {
    private final ContextLogger logger;
    private final PortfolioResolver portfolioResolver;

    public PortfolioFilter(ContextLogger logger, PortfolioResolver portfolioResolver) {
        this.logger = logger;
        this.portfolioResolver = portfolioResolver;
    }

    @Override
    public boolean check(OrderRequest request) {
        checkNotNull(request, Constants.INVALID_ORDER_REQUEST);
        // valid portfolioId ?
        String portfolioId = request.getPortfolioId();

        if (getPortfolio(portfolioId) == null) {
            logFail(request);
            request.reject(OrderRejectReason.InvalidPortfolio);
            return false;
        }

        return true;
    }

    private void logFail(OrderRequest request) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Portfolio", request.getPortfolioId())
                .add("Instrument", request.getInstrumentId())
                .log();
    }

    private Portfolio getPortfolio(String id) {
        if (!Strings.isNullOrEmpty(id)) {
            return portfolioResolver.resolvePortfolio(id);
        }

        return null;
    }

    @Override
    public String getName() {
        return Constants.PORTFOLIO_FILTER_NAME;
    }
}
