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

public abstract class AbstractInputRowBuilder implements InputRowBuilder {
    private final InputRowSchema schema;
    private InputRow current;

    protected AbstractInputRowBuilder(InputRowSchema schema) {
        this.schema = schema;
    }

    @Override
    public InputRow getCurrent() {
        return current;
    }

    protected void setCurrent(InputRow row) {
        current = row;
    }

    protected InputRow createRow() {
        return InputRow.fromSchema(schema);
    }

    protected InputRowSchema getSchema() {
        return schema;
    }

    protected int getStepsInColumn(int index) {
        return schema.getSeries(index).getSteps();
    }
}
