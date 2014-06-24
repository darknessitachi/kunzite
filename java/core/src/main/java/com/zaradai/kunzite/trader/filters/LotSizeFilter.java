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

public class LotSizeFilter implements Filter {
    static final String FILTER_NAME = "Lot Size";

    private final ContextLogger logger;
    private final InstrumentResolver instrumentResolver;

    @Inject
    LotSizeFilter(ContextLogger logger, InstrumentResolver instrumentResolver) {
        this.logger = logger;
        this.instrumentResolver = instrumentResolver;
    }

    @Override
    public boolean check(OrderRequest orderRequest) {
        checkNotNull(orderRequest, "Invalid Order request");
        Instrument instrument = checkNotNull(instrumentResolver.resolveInstrument(orderRequest.getInstrumentId()),
                "Invalid Instrument in request");

        long lotSize = instrument.getLotSize();
        if (orderRequest.getQuantity() % lotSize != 0) {
            logFailed(orderRequest, lotSize);
            orderRequest.reject(OrderRejectReason.LotSize);
            return false;
        }

        return true;
    }

    private void logFailed(OrderRequest orderRequest, long lotSize) {
        LogHelper.warn(logger)
                .addContext("Filter: " + getName())
                .add("Quantity", orderRequest.getQuantity())
                .add("Lot Size", lotSize)
                .log();
    }

    @Override
    public String getName() {
        return FILTER_NAME;
    }
}
