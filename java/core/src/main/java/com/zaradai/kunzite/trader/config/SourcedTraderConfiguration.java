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

import com.google.inject.Inject;
import com.zaradai.kunzite.config.ConfigurationSource;

public class SourcedTraderConfiguration implements TraderConfiguration {
    private static final String PRE = "trader";

    public static final String STATIC_CONFIG_URI = PRE + ".static.config.uri";
    public static final String MD_CONFIG_URI = PRE + ".md.config.uri";
    public static final String OG_URI = PRE + ".og.config.uri";

    public static final String DEFAULT_STATIC_CONFIG_URI = "static.xml";
    public static final String DEFAULT_MD_CONFIG_URI = "md.xml";
    public static final String DEFAULT_OG_CONFIG_URI = "og.xml";

    private final ConfigurationSource source;

    @Inject
    SourcedTraderConfiguration(ConfigurationSource source) {
        this.source = source;
    }


    @Override
    public String getStaticConfigUri() {
        return source.get(STATIC_CONFIG_URI, DEFAULT_STATIC_CONFIG_URI);
    }

    @Override
    public String getMarketDataConfigUri() {
        return source.get(MD_CONFIG_URI, DEFAULT_MD_CONFIG_URI);
    }

    @Override
    public String getOrderGatewayConfigUri() {
        return source.get(OG_URI, DEFAULT_OG_CONFIG_URI);
    }
}
