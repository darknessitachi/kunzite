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

import com.zaradai.kunzite.trader.events.PositionUpdatesHandler;

/**
 * Maintains positions across portfolios per instrument.
 */
public interface PositionBook extends PositionUpdatesHandler {
    Position getPositionFor(String portfolioId);    // always succeeds
    long getNetPosition(String portfolioId);
    long getTotalNetPosition();
    double getNetCashFlow(String portfolioId);
    double getTotalNetCashFlow();
}
