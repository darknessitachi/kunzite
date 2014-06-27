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
package com.zaradai.kunzite.trader.orders.executors;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KunziteOrderIdGeneratorTest {
    public static final long START_COUNTER = 12345;

    @Test
    public void shouldGenerate() throws Exception {
        KunziteOrderIdGenerator uut = KunziteOrderIdGenerator.newInstance();

        String res = uut.generate();

        assertThat(res.substring(0, res.indexOf("-")), is("Kunzite"));
        assertThat(res.substring(res.lastIndexOf("-")+1), is("1"));
    }

    @Test
    public void shouldGenerateFromKnownStart() throws Exception {
        KunziteOrderIdGenerator uut = KunziteOrderIdGenerator.newInstanceWithStartCounter(START_COUNTER);

        String res = uut.generate();

        assertThat(res.substring(0, res.indexOf("-")), is("Kunzite"));
        assertThat(res.substring(res.lastIndexOf("-")+1), is(String.valueOf(START_COUNTER)));
    }
}
