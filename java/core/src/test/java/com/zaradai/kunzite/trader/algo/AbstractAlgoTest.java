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
package com.zaradai.kunzite.trader.algo;

import com.zaradai.kunzite.trader.control.TradingState;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class AbstractAlgoTest {
    private static final String TEST_ID = "test";
    private static final TradingState TEST_STATE = mock(TradingState.class);
    private AbstractAlgo uut;

    @Before
    public void setUp() throws Exception {
        uut = new AbstractAlgo() {
            @Override
            public void initialize() throws AlgoException {

            }
        };
    }

    @Test
    public void shouldGetId() throws Exception {
        uut.setId(TEST_ID);

        assertThat(uut.getId(), is(TEST_ID));
    }

    @Test
    public void shouldGetTradingState() throws Exception {
        uut.setState(TEST_STATE);

        assertThat(uut.getState(), is(TEST_STATE));
    }
}
