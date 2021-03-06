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

import com.google.inject.Inject;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.trader.events.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The default updater assumes that a short position is designated in the trade by a negative number.
 */
public class DefaultPositionUpdater implements PositionUpdater {
    private final EventAggregator eventAggregator;

    @Inject
    DefaultPositionUpdater(EventAggregator eventAggregator) {
        this.eventAggregator = eventAggregator;
    }

    /**
     * Assumption is that crossing is taken care of by order command and already split into multiple orders
     * @param position
     * @param event
     */
    @Override
    public void update(Position position, TradeEvent event) {
        checkNotNull(position, "Invalid Position");
        checkNotNull(event, "Invalid Trade");
        checkArgument(event.getInstrumentId().equals(position.getInstrumentId()), "Trade is not for this position");
        checkArgument(event.getPortfolioId().equals(position.getPortfolioId()), "Trade is not for this position");

        double price = event.getPrice();
        long quantity = event.getQuantity();

        if (!position.isActive()) {
            // initiating new position
            position.setEntryPrice(price);
            position.setOpened(event.getTimestamp());

            updatePosition(position, price, quantity);
            // fire initiated event
            eventAggregator.publish(new PositionInitiatedEvent(position));
        } else {
            updatePosition(position, price, quantity);
            // if new position is 0 then fire liquidated event
            if (!position.isActive()) {
                eventAggregator.publish(new PositionLiquidatedEvent(position));
            } else {
                // fire position updated event
                eventAggregator.publish(new PositionChangedEvent(position));
            }
        }
    }

    private void updatePosition(Position position, double price, long quantity) {
        position.add(quantity);
    }
}
