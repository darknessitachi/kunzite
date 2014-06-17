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
package com.zaradai.kunzite.optimizer.data.dataset.driver.memory;

import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import com.zaradai.kunzite.optimizer.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InMemoryDataSetDriverTest {
    private InMemoryDataSetDriver uut;

    @Before
    public void setUp() throws Exception {
        DataSetContext context = DataSetContext.builder().name("test").build();
        uut = InMemoryDataSetDriver.createWithContext(context);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToBuildWithInvalidContext() throws Exception {
        InMemoryDataSetDriver.createWithContext(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToAddWithInvalidRow() throws Exception {
        uut.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToAddWithInvalidRowInput() throws Exception {
        Row row = Row.newInstance(null, OutputRow.fromSchema(OutputRowSchema.newBuilder().build()));
        uut.add(row);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToAddWithInvalidRowOutput() throws Exception {
        Row row = Row.newInstance(InputRow.fromSchema(InputRowSchema.newBuilder().build()), null);
        uut.add(row);
    }

    @Test
    public void shouldAddValidRowAndUpdateCount() throws Exception {
        Row row = Row.newInstance(
                InputRow.fromSchema(InputRowSchema.newBuilder().build()),
                OutputRow.fromSchema(OutputRowSchema.newBuilder().build())
        );

        uut.add(row);

        assertThat(uut.getRowCount(), is(1L));
    }

    @Test
    public void shouldGetInsertedRowByInputKey() throws Exception {
        Row row = Row.newInstance(
                InputRow.fromSchema(InputRowSchema.newBuilder().build()),
                OutputRow.fromSchema(OutputRowSchema.newBuilder().build())
        );
        uut.add(row);

        Row res = uut.get(row.getInput());

        assertThat(res, is(row));
    }

    @Test
    public void shouldClearAllDataOnClose() throws Exception {
        Row row = Row.newInstance(
                InputRow.fromSchema(InputRowSchema.newBuilder().build()),
                OutputRow.fromSchema(OutputRowSchema.newBuilder().build())
        );
        uut.add(row);

        uut.close();

        assertThat(uut.getRowCount(), is(0L));
    }

    @Test
    public void shouldReturnAnIteratorOnTheData() throws Exception {
        Iterator<Row> res = uut.iterator();

        assertThat(res, not(nullValue()));
    }
}
