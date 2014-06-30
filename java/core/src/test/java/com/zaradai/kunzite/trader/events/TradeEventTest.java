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
package com.zaradai.kunzite.trader.events;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class TradeEventTest {
    private static final String TEST_PTF_ID = "ptf_id";
    private static final String TEST_INST_ID = "inst_id";
    private static final long TEST_QTY = 4500;
    private static final double TEST_PRC = 45.25;
    private static final DateTime TEST_TIMESTAMP = DateTime.now();

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfCreatedWithInvalidPortfolio() throws Exception {
        TradeEvent.newTrade(null, TEST_INST_ID, TEST_QTY, TEST_PRC, TEST_TIMESTAMP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfCreatedWithInvalidInstrument() throws Exception {
        TradeEvent.newTrade(TEST_PTF_ID, null, TEST_QTY, TEST_PRC, TEST_TIMESTAMP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfCreatedWithInvalidPrice() throws Exception {
        TradeEvent.newTrade(TEST_PTF_ID, TEST_INST_ID, TEST_QTY, Double.NaN, TEST_TIMESTAMP);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailIfCreatedWithInvalidTimestamp() throws Exception {
        TradeEvent.newTrade(TEST_PTF_ID, TEST_INST_ID, TEST_QTY, TEST_PRC, null);
    }

    @Test
    public void shouldConstructProperly() throws Exception {
        TradeEvent uut = TradeEvent.newTrade(TEST_PTF_ID, TEST_INST_ID, TEST_QTY, TEST_PRC, TEST_TIMESTAMP);

        assertThat(uut.getQuantity(), is(TEST_QTY));
        assertThat(uut.getPrice(), is(TEST_PRC));
        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getPortfolioId(), is(TEST_PTF_ID));
        assertThat(uut.getTimestamp(), is(TEST_TIMESTAMP));
    }
}
