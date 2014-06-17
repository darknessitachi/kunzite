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
package com.zaradai.kunzite.optimizer.tactic;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.zaradai.kunzite.optimizer.model.Row;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;

public final class OptimizerResult {
    private final String target;
    private final boolean wantMaxima;
    private boolean optimized;
    private Row optimizedRow;
    private double optimizedValue;
    private StopWatch optimizationTimer;
    private long calculations;
    private int generations;

    private OptimizerResult(String target, boolean wantMaxima) {
        this.target = target;
        this.wantMaxima = wantMaxima;
        optimizationTimer = createTimer();
        if (wantMaxima) {
            optimizedValue = Double.NEGATIVE_INFINITY;
        } else {
            optimizedValue = Double.POSITIVE_INFINITY;
        }
    }

    public static OptimizerResult newInstance(String target, boolean wantMaxima) {
        Preconditions.checkNotNull(target, "Target field cannot be null");

        return new OptimizerResult(target, wantMaxima);
    }

    protected StopWatch createTimer() {
        return new Slf4JStopWatch("Optimization");
    }

    public String getTarget() {
        return target;
    }

    public Boolean wantMaxima() {
        return wantMaxima;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public Row getOptimizedRow() {
        return optimizedRow;
    }

    public double getOptimizedValue() {
        return optimizedValue;
    }

    /**
     * Process this row and determine if it is a maxima or minima depending on mode.
     * If a maxima/minima then update the local values
     *
     * @param row the row to test
     */
    public boolean testValue(Row row) {
        Preconditions.checkNotNull(row, "Row should not be null");
        double value = getTargetValue(row);

        boolean isOptimal;

        if (wantMaxima) {
            isOptimal = value > optimizedValue;
        } else {
            isOptimal = value < optimizedValue;
        }

        if (isOptimal) {
            updateOptimized(row, value);
        }

        return isOptimal;
    }

    private void updateOptimized(Row row, double value) {
        optimizedRow = Row.fromRow(row);
        optimizedValue = value;
    }

    public void setOptimized() {
        optimized = true;
        optimizationTimer.stop();
    }

    private double getTargetValue(Row row) {
        return row.getOutput().getValue(getTarget());
    }

    public long getCalculations() {
        return calculations;
    }

    public int getGenerations() {
        return generations;
    }

    public void setCalculations(long calculations) {
        this.calculations = calculations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public long getTimeTaken() {
        return optimizationTimer.getElapsedTime();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Opt Value", getOptimizedValue())
                .add("Opt Input", getOptimizedRow().getInput())
                .add("Opt Output", getOptimizedRow().getOutput())
                .add("Time (ms)", getTimeTaken())
                .add("Gens", getGenerations())
                .add("Calcs", getCalculations())
                .toString();
    }
}
