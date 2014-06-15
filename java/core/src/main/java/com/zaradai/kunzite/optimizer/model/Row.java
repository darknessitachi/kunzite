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


public final class Row implements Cloneable {
    private InputRow input;
    private OutputRow output;

    private Row() {

    }

    private Row(InputRow input, OutputRow output) {
        this.input = input;
        this.output = output;
    }

    public static Row newInstance() {
        return new Row();
    }
    public static Row fromSchema(RowSchema schema) {
        Preconditions.checkNotNull(schema, "Invalid schema specified");

        return new Row(
                InputRow.fromSchema(schema.getInputRowSchema()),
                OutputRow.fromSchema(schema.getOutputRowSchema()));
    }

    public static Row fromRow(Row row) {
        Preconditions.checkNotNull(row, "Invalid row specified");

        return new Row((InputRow) row.getInput().clone(), (OutputRow) row.getOutput().clone());
    }

    @Override
    public Object clone() {
        return Row.fromRow(this);
    }

    public InputRow getInput() {
        return input;
    }

    public OutputRow getOutput() {
        return output;
    }

    public void setInput(InputRow input) {
        this.input = input;
    }

    public void setOutput(OutputRow output) {
        this.output = output;
    }
}
