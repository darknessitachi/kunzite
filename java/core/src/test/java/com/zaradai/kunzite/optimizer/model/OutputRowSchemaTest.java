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

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class OutputRowSchemaTest {
    private static final String TEST_COL_NAME_1 = "test";
    private static final String TEST_COL_NAME_2 = "test2";
    private static final String TEST_COL_NAME_3 = "test3";

    private OutputRowSchema uut;

    @Before
    public void setUp() throws Exception {
        uut = OutputRowSchema.newBuilder()
                .withName(TEST_COL_NAME_1)
                .withName(TEST_COL_NAME_2)
                .withName(TEST_COL_NAME_3)
                .build();

    }

    @Test
    public void shouldBuildWithBuilder() throws Exception {
        assertThat(uut.getNumColumns(), is(3));
    }

    @Test
    public void shouldGetIndexByName() throws Exception {
        assertThat(uut.getIndex(TEST_COL_NAME_1), is(0));
        assertThat(uut.getIndex(TEST_COL_NAME_2), is(1));
        assertThat(uut.getIndex(TEST_COL_NAME_3), is(2));
    }

    @Test
    public void shouldGetNameByIndex() throws Exception {
        assertThat(uut.getName(0), is(TEST_COL_NAME_1));
        assertThat(uut.getName(1), is(TEST_COL_NAME_2));
        assertThat(uut.getName(2), is(TEST_COL_NAME_3));
    }

    @Test
    public void shouldGetImmutableListOfColumnNames() throws Exception {
        List<String> names = uut.getColumns();

        assertThat(names, not(nullValue()));
        assertThat(names.size(), is(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldCatchInvalidGetName() throws Exception {
        uut.getName(4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidGetIndex() throws Exception {
        uut.getIndex("not there");
    }
}
