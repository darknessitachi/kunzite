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

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A portfolio will manage positions for a particular account.  One may have different accounts for
 * various reasons and the portfolio will track that accounts position
 */
public class Portfolio {
    private final String id;
    private final Map<String, Position> positionByInstrument;

    @Inject
    Portfolio(@Assisted String id) {
        checkArgument(!Strings.isNullOrEmpty(id), "Portfolio identifier is invalid");
        this.id = id;
        positionByInstrument = createPositionMap();
    }

    protected Map<String, Position> createPositionMap() {
        return Maps.newHashMap();
    }

    public String getId() {
        return id;
    }

    public void addPosition(Position res) {
        this.positionByInstrument.put(res.getInstrumentId(), res);
    }

    public long getNetPosition() {
        long res = 0;

        for (Map.Entry<String, Position> entry : positionByInstrument.entrySet()) {
            res += entry.getValue().getNet();
        }

        return res;
    }

    public double getNetCashFlow() {
        double res = 0;

        for (Map.Entry<String, Position> entry : positionByInstrument.entrySet()) {
            res += entry.getValue().getNetCashFlow();
        }

        return res;
    }

    public boolean hasPosition(String instrumentId) {
        return positionByInstrument.get(instrumentId) != null;
    }

    public long getPositionFor(String instrumentId) {
        Position position = positionByInstrument.get(instrumentId);

        if (position != null) {
            return position.getNet();
        }

        return 0L;
    }
}
