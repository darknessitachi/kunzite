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

import com.google.common.base.Objects;

import java.util.Comparator;

public class TickDefinition {
    private double min;
    private double max;
    private double value;

    public TickDefinition() {

    }

    public TickDefinition(double min, double max, double value) {
        this.min = min;
        this.max = max;
        this.value = value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getValue() {
        return value;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public static final Comparator<TickDefinition> TickComparator = new Comparator<TickDefinition>() {
        public int compare(TickDefinition t1, TickDefinition t2) {
            return Double.compare(t1.getMin(), t2.getMin());
        }
    };

    @Override
    public int hashCode() {
        return Objects.hashCode(min, max, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final TickDefinition other = (TickDefinition) obj;
        // don't use Guava equals as it uses identity equality.
        return Objects.equal(min, other.min)
                && Objects.equal(max, other.max)
                && Objects.equal(value, other.value);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("min", min)
                .add("max", max)
                .add("value", value)
                .toString();
    }
}
