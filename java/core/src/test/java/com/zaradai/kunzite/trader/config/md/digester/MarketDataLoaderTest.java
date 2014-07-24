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
package com.zaradai.kunzite.trader.config.md.digester;

import com.zaradai.kunzite.trader.config.md.ChannelConfig;
import com.zaradai.kunzite.trader.config.md.MappingConfig;
import com.zaradai.kunzite.trader.config.md.MarketDataConfiguration;
import com.zaradai.kunzite.trader.config.md.Subscription;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class MarketDataLoaderTest {
    public static final String CONFIG_XML = "marketdata.xml";

    @Test
    public void shouldLoad() throws Exception {
        DigesterMarketDataConfigLoader uut = new DigesterMarketDataConfigLoader();
        MarketDataConfiguration res = uut.load(CONFIG_XML);

        assertThat(res.getChannels(), IsIterableWithSize.<ChannelConfig>iterableWithSize(2));
        assertThat(res.getMappings(), IsIterableWithSize.<MappingConfig>iterableWithSize(2));
        assertThat(res.getSubscriptions(), IsIterableWithSize.<Subscription>iterableWithSize(5));
    }
}
