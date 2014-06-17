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

import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.data.DataRequestManager;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.InputRowGenerator;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.List;

public class HillClimberTactic extends AbstractTactic {
    static final String OPTIMIZER_NAME = "Hill Climber";

    private long processed;
    private int generation;
    private long toProcess;

    @Inject
    HillClimberTactic(DataRequestManager dataRequestManager) {
        super(dataRequestManager);
        generation = 0;
        processed = 0;
    }

    @Override
    public String getName() {
        return OPTIMIZER_NAME;
    }

    @Override
    protected void initialize() {
        //nop
    }

    @Override
    protected List<InputRow> prepare() {
        // get row to search around
        InputRow centre = (getOptimizedRow() == null) ? getStart() : getOptimizedRow().getInput();
        // generate neighbours for the start condition and send
        return InputRowGenerator.getNeighbours(centre);
    }

    @Override
    protected boolean process(List<Row> results) {
        boolean finished = true;

        for (Row row : results) {
            if (testValue(row)) {
                // new optima found keep going
                finished = false;
            }
        }

        return finished;
    }
}
