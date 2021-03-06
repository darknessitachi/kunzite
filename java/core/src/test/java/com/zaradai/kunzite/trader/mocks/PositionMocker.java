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
package com.zaradai.kunzite.trader.mocks;

import com.zaradai.kunzite.trader.positions.Position;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PositionMocker {
    public static Position create(String portfolioId, String instrumentId) {
        Position res = mock(Position.class);
        when(res.getPortfolioId()).thenReturn(portfolioId);
        when(res.getInstrumentId()).thenReturn(instrumentId);

        return res;
    }
}
