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
package com.zaradai.kunzite.optimizer.data.dataset.driver.dummy;

import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DummyDataSetDriverTest {
    private DummyDataSetDriver uut;

    @Before
    public void setUp() throws Exception {
        uut = DummyDataSetDriver.createWithContext(DataSetContext.builder().name("test").build());
    }

    @Test
    public void shouldReturnNullForGet() throws Exception {
        assertThat(uut.get(null), is(nullValue()));
    }

    @Test
    public void shouldGetZeroRowCount() throws Exception {
        assertThat(uut.getRowCount(), is(0L));
    }

    @Test
    public void shouldGetValidIterator() throws Exception {
        assertThat(uut.iterator(), not(nullValue()));
    }
}
