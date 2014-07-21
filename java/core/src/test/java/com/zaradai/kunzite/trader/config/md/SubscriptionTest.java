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
package com.zaradai.kunzite.trader.config.md;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SubscriptionTest {
    private static final String TEST_STRING = "test";

    @Test
    public void shouldGetId() throws Exception {
        Subscription uut = new Subscription();
        uut.setId(TEST_STRING);

        assertThat(uut.getId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetMap() throws Exception {
        Subscription uut = new Subscription();
        uut.setMap(TEST_STRING);

        assertThat(uut.getMap(), is(TEST_STRING));
    }

    @Test
    public void shouldGetChannel() throws Exception {
        Subscription uut = new Subscription();
        uut.setChannel(TEST_STRING);

        assertThat(uut.getChannel(), is(TEST_STRING));
    }
}
