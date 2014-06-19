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

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class TickCalculatorTest {
    private TickCalculator uut;

    @Before
    public void setUp() throws Exception {
        // will follow HKEX for stocks rules
        uut = new TickCalculator();
        uut.addDefinition(new TickDefinition(0.01, 0.25, 0.001));
        uut.addDefinition(new TickDefinition(0.25, 0.50, 0.005));
        uut.addDefinition(new TickDefinition(0.505, 10.00, 0.01));
        uut.addDefinition(new TickDefinition(10.01, 20.00, 0.02));
        uut.addDefinition(new TickDefinition(20.01, 100.00, 0.05));
        uut.addDefinition(new TickDefinition(100.01, 200.00, 0.10));
        uut.addDefinition(new TickDefinition(200.01, 500.00, 0.20));
        uut.addDefinition(new TickDefinition(500.01, 1000.00, 0.50));
        uut.addDefinition(new TickDefinition(1000.01, 2000.00, 1.00));
        uut.addDefinition(new TickDefinition(2000.01, 5000.00, 2.00));
        uut.addDefinition(new TickDefinition(5000.01, 9995.00, 5.00));
    }

    @Test
    public void shouldFindDefinition() throws Exception {
        assertThat(uut.getTickDefinitionFor(200.0).getValue(), is(0.10));
        assertThat(uut.getTickDefinitionFor(200.01).getValue(), is(0.20));
        assertThat(uut.getTickDefinitionFor(0.249).getValue(), is(0.001));
        assertThat(uut.getTickDefinitionFor(0.25).getValue(), is(0.005));
        assertThat(uut.getTickDefinitionFor(10000.0), is(nullValue()));
    }

    @Test
    public void shouldIndicateTickValidity() {
        assertThat(uut.isValidTick(0.251), is(false));
        assertThat(uut.isValidTick(0.255), is(true));
        assertThat(uut.isValidTick(10000.0), is(false));
    }

    @Test
    public void shouldWorkWithOneDefinition() {
        uut = new TickCalculator();
        uut.addDefinition(new TickDefinition(0.01, 10.00, 0.01));

        assertThat(uut.isValidTick(5.25), is(true));
        assertThat(uut.isValidTick(5.251), is(false));
    }

    @Test
    public void shouldHandleReset() throws Exception {
        uut.reset();

        assertThat(uut.getTickDefinitionFor(1), is(nullValue()));
    }
}
