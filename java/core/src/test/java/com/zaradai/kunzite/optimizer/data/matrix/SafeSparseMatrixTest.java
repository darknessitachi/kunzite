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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SafeSparseMatrixTest {
    private static final int TEST_X = 23;
    private static final int TEST_Y = 42;
    private static final Integer TEST = 78;

    @Test
    public void shouldGetIfExists() throws Exception {
        SafeSparseMatrix<Integer> uut = new SafeSparseMatrix<Integer>();

        uut.set(TEST_X, TEST_Y, TEST);

        assertThat(uut.get(TEST_X, TEST_Y), is(TEST));
    }

    @Test
    public void shouldReturnNullIfNotExists() throws Exception {
        SafeSparseMatrix<Integer> uut = new SafeSparseMatrix<Integer>();

        assertThat(uut.get(TEST_X, TEST_Y), is(nullValue()));

    }
}
