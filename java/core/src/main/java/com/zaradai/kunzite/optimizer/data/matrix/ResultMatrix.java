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
package com.zaradai.kunzite.optimizer.data.matrix;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zaradai.kunzite.optimizer.model.InputRowSchema;
import com.zaradai.kunzite.optimizer.model.Row;

public final class ResultMatrix {
    private final SparseMatrix<Row> data;
    private final int xIndex;
    private final int yIndex;
    private final String target;
    private int targetIndex;
    private final int columnXSize;
    private final int columnYSize;
    private double minValue;
    private double maxValue;
    private final String columnXName;
    private final String columnYName;
    private final InputRowSchema schema;

    private ResultMatrix(String columnXName, String columnYName, String target, InputRowSchema schema) {

        this.columnXName = columnXName;
        this.columnYName = columnYName;
        this.schema = schema;
        targetIndex = -1;
        this.target = target;
        this.xIndex = schema.getIndex(columnXName);
        columnXSize = schema.getSeries(xIndex).getSteps();
        this.yIndex = schema.getIndex(columnYName);
        this.columnYSize = schema.getSeries(yIndex).getSteps();
        data = createSparseMatrix();
        minValue = Double.NaN;
        maxValue = Double.NaN;
    }

    protected SparseMatrix<Row> createSparseMatrix() {
        // default is not thread safe, override and provide SafeSparseMatrix if thread safety required
        return new SparseMatrix<Row>();
    }

    public static ResultMatrix newMatrix(String columnXName, String columnYName, String target, InputRowSchema schema) {
        Preconditions.checkNotNull(schema, "Invalid Schema");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(target), "Invalid target");
        Preconditions.checkArgument(schema.hasColumn(columnXName), "Name for columnXName is unknown");
        Preconditions.checkArgument(schema.hasColumn(columnYName), "Name for columnXName is unknown");

        return new ResultMatrix(columnXName, columnYName, target, schema);
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public String getX() {
        return columnXName;
    }

    public String getY() {
        return columnYName;
    }

    public int getColumnXSize() {
        return columnXSize;
    }

    public int getColumnYSize() {
        return columnYSize;
    }

    public InputRowSchema getSchema() {
        return schema;
    }

    public double getYMin() {
        return schema.getSeries(yIndex).getMin();
    }

    public double getYMax() {
        return schema.getSeries(yIndex).getMax();
    }

    public double getXMin() {
        return schema.getSeries(xIndex).getMin();
    }

    public double getXMax() {
        return schema.getSeries(xIndex).getMax();
    }

    public void update(Row row) {
        // need to guard against unknowns
        // get the position of this update
        int x = row.getInput().getValue(xIndex);
        int y = row.getInput().getValue(yIndex);
        double targetValue = getValue(row);

        Row current = get(x, y);

        if (current == null) {
            update(x, y, row);
        } else {
            double currentValue = getValue(current);

            if (targetValue > currentValue) {
                update(x, y, row);
            }
        }
    }

    private void update(int x, int y, Row row) {
        data.set(x, y, row);
        // check value against min/max
        double value = getValue(row);
        // update min
        if (Double.isNaN(minValue) || value < minValue) {
            minValue = value;
        }
        if (Double.isNaN(maxValue) || value > maxValue) {
            maxValue = value;
        }
    }


    public Row get(int x, int y) {
        if (x >= 0 && x < columnXSize && y >= 0 && y < columnYSize) {
            return data.get(x, y);
        }

        return null;
    }

    public double getValue(int x, int y) {
        Row row = get(x, y);

        if (row != null) {
            return getValue(row);
        }
        return Double.NaN;
    }

    private double getValue(Row row) {
        return row.getOutput().getValue(getTargetIndex(row));
    }


    private int getTargetIndex(Row row) {
        if (targetIndex == -1) {
            targetIndex = row.getOutput().getSchema().getIndex(target);
        }

        return targetIndex;
    }

    public String getTarget() {
        return target;
    }

    public ResultMatrix reverse() {
        ResultMatrix res = new ResultMatrix(getY(), getX(), getTarget(), getSchema());

        for (int x = 0; x < columnXSize; ++x) {
            for (int y = 0; y < columnYSize; ++y) {
                // reverse data
                res.data.set(y, x, data.get(x, y));
            }
        }

        return res;
    }
}
