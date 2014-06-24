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
package com.zaradai.kunzite.trader.filters;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FilterRequestTest {
    private static final String TEST_INST_ID = "test";
    private static final String TEST_PTF_ID = "ptf";

    @Test
    public void shouldConstructValidNewInstance() throws Exception {
        FilterRequest uut = FilterRequest.newInstance(TEST_INST_ID, TEST_PTF_ID);

        assertThat(uut, not(nullValue()));
        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getPortfolioId(), is(TEST_PTF_ID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithInvalidInstrumentId() throws Exception {
        FilterRequest uut = FilterRequest.newInstance(null, TEST_PTF_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithInvalidPortfolioId() throws Exception {
        FilterRequest uut = FilterRequest.newInstance(TEST_INST_ID, null);
    }
}
