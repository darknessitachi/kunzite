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
package com.zaradai.kunzite.optimizer.evaluators;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.OutputRow;
import com.zaradai.kunzite.optimizer.model.OutputRowSchema;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.List;

public class DualMaximaEqEvaluator implements Evaluator {
    private static final String INPUT_X = "x";
    private static final String INPUT_Y = "y";
    private static final String OUTPUT_Z = "z";
    private static final OutputRowSchema outputRowSchema;
    public static final String VERSION = "1.0";
    private static final List<String> validInputs = Lists.newArrayList(INPUT_X, INPUT_Y);
    private static final List<String> validOutputs = Lists.newArrayList(OUTPUT_Z);

    static {
        outputRowSchema = OutputRowSchema.newBuilder().withName(OUTPUT_Z).build();
    }

    @Override
    public List<String> getInputKeys() {
        return validInputs;
    }

    @Override
    public List<String> getOutputKeys() {
        return validOutputs;
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public OutputRowSchema getOutputSchema() {
        return outputRowSchema;
    }

    @Override
    public Row evaluate(InputRow inputRow) {
        // get the input values
        double x = inputRow.getInputValue(INPUT_X);
        double y = inputRow.getInputValue(INPUT_Y);
        // do the calc
        double z = Math.exp(-((Math.pow(x, 2) + Math.pow(y, 2)))) + 2 * Math.exp(-(Math.pow(x - 1.7, 2) + Math.pow(y - 1.7, 2)));

        OutputRow outputRow = OutputRow.fromSchema(outputRowSchema);
        outputRow.setValue(OUTPUT_Z, z);

        return Row.newInstance(inputRow, outputRow);
    }
}
