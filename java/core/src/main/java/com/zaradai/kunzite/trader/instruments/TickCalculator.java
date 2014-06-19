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
package com.zaradai.kunzite.trader.instruments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.zaradai.kunzite.utils.Utils.areApproxSame;
import static java.lang.Math.round;

public final class TickCalculator {
    private final List<TickDefinition> ticks;
    private final static double EPSILON = 0.00001;

    public TickCalculator() {
        ticks = createTickList();
    }

    private List<TickDefinition> createTickList() {
        return new ArrayList<TickDefinition>();
    }

    public void addDefinition(TickDefinition tickDefinition) {
        ticks.add(tickDefinition);
        sort();
    }

    private void sort() {
        Collections.sort(ticks, TickDefinition.TickComparator);
    }

    public TickDefinition getTickDefinitionFor(double value) {
        // do a binary search
        int from = 0;
        int until = ticks.size();

        while (from < until) {
            int mid = (from + until) / 2;  // Compute mid point.
            TickDefinition tickDefinition = ticks.get(mid);
            // compare
            if (value < tickDefinition.getMin()) {
                until = mid;       // repeat search in bottom half.
            } else if (value > tickDefinition.getMax()) {
                from = mid + 1;  // Repeat search in top half.
            } else {
                return tickDefinition;       // Found it. return
            }
        }

        return null;      // Failed to find
    }

    public boolean isValidTick(double value) {
        TickDefinition tickDefinition = getTickDefinitionFor(value);

        if (tickDefinition != null) {
            double test = round(value / tickDefinition.getValue()) * tickDefinition.getValue();
            return areApproxSame(test, value, EPSILON);
        }

        return false;
    }

    public void reset() {
        ticks.clear();
    }
}
