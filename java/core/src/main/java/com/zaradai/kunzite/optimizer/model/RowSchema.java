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

import com.google.common.base.Preconditions;

public final class RowSchema {
    private final InputRowSchema inputRowSchema;
    private final OutputRowSchema outputRowSchema;

    private RowSchema(InputRowSchema inputRowSchema, OutputRowSchema outputRowSchema) {
        this.inputRowSchema = inputRowSchema;
        this.outputRowSchema = outputRowSchema;
    }

    public static RowSchema newInstance(InputRowSchema inputRowSchema, OutputRowSchema outputRowSchema) {
        Preconditions.checkNotNull(inputRowSchema, "Invalid Input schema specified");
        Preconditions.checkNotNull(outputRowSchema, "Invalid Output schema specified");

        return new RowSchema(inputRowSchema, outputRowSchema);
    }

    public InputRowSchema getInputRowSchema() {
        return inputRowSchema;
    }

    public OutputRowSchema getOutputRowSchema() {
        return outputRowSchema;
    }
}
