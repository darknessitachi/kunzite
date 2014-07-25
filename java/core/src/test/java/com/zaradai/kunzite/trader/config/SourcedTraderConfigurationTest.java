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
package com.zaradai.kunzite.trader.config;

import com.zaradai.kunzite.config.ConfigurationSource;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SourcedTraderConfigurationTest {
    private ConfigurationSource source;
    private SourcedTraderConfiguration uut;

    @Before
    public void setUp() throws Exception {
        source = mock(ConfigurationSource.class);
        uut = new SourcedTraderConfiguration(source);
    }

    @Test
    public void shouldGetStaticConfigUri() throws Exception {
        uut.getStaticConfigUri();

        verify(source).get(SourcedTraderConfiguration.STATIC_CONFIG_URI,
                SourcedTraderConfiguration.DEFAULT_STATIC_CONFIG_URI);
    }

    @Test
    public void shouldGetMarketDataConfigUri() throws Exception {
        uut.getMarketDataConfigUri();

        verify(source).get(SourcedTraderConfiguration.MD_CONFIG_URI, SourcedTraderConfiguration.DEFAULT_MD_CONFIG_URI);
    }

    @Test
    public void shouldGetOrderGatewayConfigUri() throws Exception {
        uut.getOrderGatewayConfigUri();

        verify(source).get(SourcedTraderConfiguration.OG_URI,SourcedTraderConfiguration.DEFAULT_OG_CONFIG_URI);
    }
}
