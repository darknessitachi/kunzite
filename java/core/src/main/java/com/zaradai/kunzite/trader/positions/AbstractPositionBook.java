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

import com.zaradai.kunzite.trader.events.TradeEvent;
import com.zaradai.kunzite.trader.instruments.Instrument;

import java.util.Iterator;
import java.util.Map;


public abstract class AbstractPositionBook implements PositionBook {
    private final PositionFactory positionFactory;
    private final PositionUpdater positionUpdater;
    private final PortfolioResolver portfolioResolver;
    private final Instrument instrument;
    private final Map<String, Position> positionsByPortfolio;

    protected AbstractPositionBook(PositionFactory positionFactory, PositionUpdater positionUpdater, PortfolioResolver portfolioResolver, Instrument instrument) {
        this.positionFactory = positionFactory;
        this.positionUpdater = positionUpdater;
        this.portfolioResolver = portfolioResolver;
        this.instrument = instrument;
        positionsByPortfolio = createPositionMap();
    }

    @Override
    public void onTrade(TradeEvent event) {
        // assume trade is for us and has been filtered by the instrument manager or book container
        Position position = getPositionFor(event.getPortfolioId());
        positionUpdater.update(position, event);
    }

    @Override
    public long getNetPosition(String portfolioId) {
        return getPositionFor(portfolioId).getNet();
    }

    @Override
    public long getTotalNetPosition() {
        long res = 0;
        Iterator<Position> iter = positionsByPortfolio.values().iterator();

        while (iter.hasNext()) {
            res += iter.next().getNet();
        }

        return res;
    }

    protected abstract Map<String,Position> createPositionMap();

    protected Map<String, Position> getPositionByPortfolioMap() {
        return positionsByPortfolio;
    }

    protected Position createPosition(String portfolioId) {
        Portfolio portfolio = portfolioResolver.resolvePortfolio(portfolioId);
        Position res = positionFactory.create(portfolio, instrument);

        return res;
    }
}
