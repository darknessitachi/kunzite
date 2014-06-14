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

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class RowSchemaTest {
    @Test
    public void shouldCreateNewInstance() throws Exception {
        RowSchema rowSchema = RowSchema.newInstance(
                InputRowSchema.newBuilder().build(),
                OutputRowSchema.newBuilder().build()
        );

        assertThat(rowSchema, not(nullValue()));
    }

    @Test(expected = NullPointerException.class)
    public void shouldCatchInvalidInputSchemaOnCreate() throws Exception {
        RowSchema rowSchema = RowSchema.newInstance(
                null,
                OutputRowSchema.newBuilder().build()
        );
    }

    @Test(expected = NullPointerException.class)
    public void shouldCatchInvalidOutputSchemaOnCreate() throws Exception {
        RowSchema rowSchema = RowSchema.newInstance(
                InputRowSchema.newBuilder().build(),
                null
        );
    }
}
