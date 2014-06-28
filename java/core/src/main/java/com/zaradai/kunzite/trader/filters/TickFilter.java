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

import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.instruments.Market;
import com.zaradai.kunzite.trader.orders.model.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;

import static com.google.common.base.Preconditions.checkNotNull;

public class TickFilter implements Filter {
    private final ContextLogger logger;
    private final Market market;

    public TickFilter(ContextLogger logger, @Assisted Market market) {
        this.logger = logger;
        this.market = market;
    }

    @Override
    public boolean check(OrderRequest request) {
        checkNotNull(request, Constants.INVALID_ORDER_REQUEST);

        double price = request.getPrice();

        if (!market.validTick(price)) {
            logFail(market.getId(), price);
            request.reject(OrderRejectReason.TICK_SIZE);
            return false;
        }

        return true;
    }

    private void logFail(String id, double price) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Market", id)
                .add("Price", price)
                .log();
    }

    @Override
    public String getName() {
        return Constants.TICK_FILTER_NAME;
    }
}
