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

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetUpdateListener;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.Map;

public class CachingMatrixManager implements MatrixManager, DataSetUpdateListener {
    private final DataSet dataSet;
    private final Map<String, ResultMatrix> matrices;

    @Inject
    CachingMatrixManager(@Assisted DataSet dataSet) {
        this.dataSet = dataSet;
        this.dataSet.registerUpdateListener(this);
        matrices = createMatrixMap();
    }

    protected Map<String, ResultMatrix> createMatrixMap() {
        return Maps.newHashMap();
    }

    @Override
    public ResultMatrix get(String columnX, String columnY, String target) {
        ResultMatrix res = matrices.get(getInputKey(columnX, columnY, target));

        if (res == null) {
            // try reversed
            res = matrices.get(getInputKey(columnY, columnX, target));

            if (res != null) {
                // return a reversed matrix
                return reverseMatrix(res);
            }
            res = new ResultMatrix(columnX, columnY, target, dataSet.getContext().getInputSchema());
            // add to active map
            matrices.put(getInputKey(columnX, columnY, target), res);
            // for now do a painful scan to update
            rescan(res);
        }

        return res;
    }

    private String getInputKey(String columnX, String columnY, String target) {
        return columnX + "-" + columnY + "-" + target;
    }

    private void rescan(ResultMatrix matrix) {
        for (Row row : dataSet) {
            matrix.update(row);
        }
    }

    private ResultMatrix reverseMatrix(ResultMatrix matrix) {
        ResultMatrix res = matrix.reverse();
        // add to map
        matrices.put(getInputKey(res.getX(), res.getY(), res.getTarget()), res);
        // and return
        return res;
    }

    @Override
    public void onUpdate(Row row) {
        for (ResultMatrix matrix : matrices.values()) {
            matrix.update(row);
        }
    }
}
