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

import com.zaradai.kunzite.trader.events.StartOfDay;
import com.zaradai.kunzite.trader.events.TradeEvent;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The default updater assumes that a short position is designated in the trade by a negative number.
 */
public class DefaultPositionUpdater implements PositionUpdater {
    @Override
    public void update(Position position, TradeEvent event) {
        checkNotNull(position, "Invalid Position");
        checkNotNull(event, "Invalid Trade");
        checkArgument(event.getInstrumentId().equals(position.getInstrumentId()), "Trade is not for this position");
        checkArgument(event.getPortfolioId().equals(position.getPortfolioId()), "Trade is not for this position");

        long quantity = event.getQuantity();
        double price = event.getPrice();
        double cashFlow = price * quantity * position.getInstrument().getMultiplier();

        if (quantity < 0) {
            // quantity is negative but intraday short is absolute so add the quantity negated.
            position.addShort(-quantity);
            position.addShortCashFlow(-cashFlow);
        } else {
            position.addLong(quantity);
            position.addLongCashFlow(cashFlow);
        }
    }

    @Override
    public void update(Position position, StartOfDay startOfDay) {
        checkNotNull(position, "Invalid Position");
        checkNotNull(startOfDay, "Invalid Start of Day Position");
        checkArgument(startOfDay.getInstrumentId().equals(position.getInstrumentId()), "Start of day position is not for this position");
        checkArgument(startOfDay.getPortfolioId().equals(position.getPortfolioId()), "Start of day position is not for this position");

        position.reset();
        position.setStartOfDay(startOfDay.getPosition());
        position.setStartOfDayCashFlow(startOfDay.getCashFlow());
        position.setOpened(startOfDay.getOpened());
        position.setEntryPrice(startOfDay.getEntryPrice());
    }
}
