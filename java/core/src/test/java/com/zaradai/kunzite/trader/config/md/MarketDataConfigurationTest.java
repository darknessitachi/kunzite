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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MarketDataConfigurationTest {
    private static final ChannelConfig CHANNEL_1 = mock(ChannelConfig.class);
    private static final ChannelConfig CHANNEL_2 = mock(ChannelConfig.class);
    private static final MappingConfig MAPPING_1 = mock(MappingConfig.class);
    private static final MappingConfig MAPPING_2 = mock(MappingConfig.class);
    private static final Subscription SUB_1 = mock(Subscription.class);
    private static final Subscription SUB_2 = mock(Subscription.class);

    @Test
    public void shouldHaveNoChannelsOnConstruction() throws Exception {
        MarketDataConfiguration uut = new MarketDataConfiguration();

        assertThat(uut.getChannels(), emptyIterable());
    }

    @Test
    public void shouldHaveNoMappingsOnConstruction() throws Exception {
        MarketDataConfiguration uut = new MarketDataConfiguration();

        assertThat(uut.getMappings(), emptyIterable());
    }

    @Test
    public void shouldHaveNoSubscriptionsOnConstruction() throws Exception {
        MarketDataConfiguration uut = new MarketDataConfiguration();

        assertThat(uut.getSubscriptions(), emptyIterable());
    }

    @Test
    public void shouldHaveChannelsAfterAdding() throws Exception {
        MarketDataConfiguration uut = new MarketDataConfiguration();

        uut.add(CHANNEL_1);
        uut.add(CHANNEL_2);

        assertThat(uut.getChannels(), containsInAnyOrder(CHANNEL_1, CHANNEL_2));
    }

    @Test
    public void shouldHaveMappingsAfterAdding() throws Exception {
        MarketDataConfiguration uut = new MarketDataConfiguration();

        uut.add(MAPPING_1);
        uut.add(MAPPING_2);

        assertThat(uut.getMappings(), containsInAnyOrder(MAPPING_1, MAPPING_2));
    }

    @Test
    public void shouldHaveSubscriptionsAfterAdding() throws Exception {
        MarketDataConfiguration uut = new MarketDataConfiguration();

        uut.add(SUB_1);
        uut.add(SUB_2);

        assertThat(uut.getSubscriptions(), containsInAnyOrder(SUB_1, SUB_2));
    }
}
