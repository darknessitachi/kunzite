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
package com.zaradai.kunzite.optimizer.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zaradai.kunzite.utils.Utils;

import java.util.Iterator;
import java.util.List;

/**
 * Generates a series of double values according to a specified interval.
 */
public final class Series {
    private final List<Double> values;

    private Series() {
        values = createValueList();
    }

    protected List<Double> createValueList() {
        return Lists.newArrayList();
    }

    public static Series newStepSeries(double start, double step, int steps) {
        Preconditions.checkArgument(step > 0, "Step must be positive");

        Series res = new Series();

        double value = start;

        for (int i = 0; i < steps; ++i) {
            res.values.add(value);
            value += step;
        }

        return res;
    }

    public static Series newMinMaxSeries(double min, double max, double step) {
        Preconditions.checkArgument(min < max, "Min value must be less than Max");
        Preconditions.checkArgument(step > 0, "Step must be positive");

        Series res = new Series();

        double value = min;

        while (value <= max) {
            res.values.add(value);
            value += step;
        }

        return res;
    }

    public double getMin() {
        return values.get(0);
    }

    public double getMax() {
        return values.get(getSteps() - 1);
    }

    public int getSteps() {
        return values.size();
    }

    public double getValue(int step) {
        Preconditions.checkArgument(step >= 0 && step < getSteps());

        return values.get(step);
    }

    public double getPreviousValue(double value) {
        double res = value;

        for (double d : values) {
            if (d < value) {
                res = d;
            } else {
                break;
            }
        }

        return res;
    }

    public double getNextValue(double value) {
        double res = value;

        for (int i = getSteps() - 1; i >= 0; --i) {
            double d = values.get(i);

            if (d > value) {
                res = d;
            } else {
                break;
            }
        }

        return res;
    }

    public Iterator<Double> iterator() {
        return values.iterator();
    }


    public int getStepIndex(double value) {
        int res = 0;

        for (double d : values) {
            if (Utils.areApproxSame(d, value)) {
                return res;
            }

            res++;
        }

        return -1;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Series other = (Series) obj;

        return Objects.equal(values, other.values);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("Min", getMin())
                .add("Max", getMax())
                .add("Steps", getSteps())
                .toString();
    }
}
