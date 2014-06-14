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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class RowTest {
    @Test
    public void shouldCreateFromSchema() throws Exception {
       Row uut = Row.fromSchema(RowSchema.newInstance(
               InputRowSchema.newBuilder().build(),
               OutputRowSchema.newBuilder().build()));

        assertThat(uut, not(nullValue()));
    }

    @Test
    public void shouldCreateFromAnotherRow() throws Exception {
        Row other = createDefaultRow();
        Row uut = Row.fromRow(other);

        assertThat(uut, not(nullValue()));
    }

    @Test
    public void shouldClone() throws Exception {
        Row uut = createDefaultRow();
        Row cloned = (Row) uut.clone();

        assertThat(cloned, not(nullValue()));
        assertThat(cloned == uut, is(false));
        assertThat(cloned.getInput() == uut.getInput(), is(false));
        assertThat(cloned.getOutput() == uut.getOutput(), is(false));
    }

    private Row createDefaultRow() {
        return Row.fromSchema(RowSchema.newInstance(
                InputRowSchema.newBuilder().build(),
                OutputRowSchema.newBuilder().build()));
    }
}
