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
package com.zaradai.kunzite.trader.positions;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.trader.instruments.Instrument;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public final class ConcurrentPositionBook extends AbstractPositionBook {
    @Inject
    ConcurrentPositionBook(PositionFactory positionFactory, PositionUpdater positionUpdater,
                           PortfolioResolver portfolioResolver, @Assisted Instrument instrument) {
        super(positionFactory, positionUpdater, portfolioResolver, instrument);
    }

    @Override
    protected Map<String, Position> createPositionMap() {
        return Maps.newConcurrentMap();
    }

    @Override
    public Position getPositionFor(String portfolioId) {
        ConcurrentMap<String, Position> map = (ConcurrentMap<String, Position>) getPositionByPortfolioMap();

        Position res = map.get(portfolioId);

        if (res == null) {
            // add atomically
            res = createPosition(portfolioId);
            Position previous = map.putIfAbsent(portfolioId, res);

            if (previous != null) {
                // some other thread already beat us to it so return this position.
                return previous;
            }
        }

        return res;
    }
}
