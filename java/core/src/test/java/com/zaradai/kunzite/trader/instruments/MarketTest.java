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
package com.zaradai.kunzite.trader.instruments;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MarketTest {
    private static final String MARKET_ID = "test";

    @Test
    public void shouldValidateTick() {
        Market uut = createMarket();
        uut.addTickDefinition(new TickDefinition(0.0, 10.0, 0.1));

        boolean valid = uut.validTick(5.1);
        boolean invalid = uut.validTick(5.01);

        assertThat(valid, is(true));
        assertThat(invalid, is(false));
    }

    @Test
    public void shouldHaveId() throws Exception {
        Market uut = createMarket();

        String id = uut.getId();

        assertThat(id, is(MARKET_ID));
    }

    private Market createMarket() {
        return new Market(MARKET_ID);
    }
}
