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
package com.zaradai.kunzite.trader.config.statics.digester;

import com.google.common.collect.Maps;
import com.zaradai.kunzite.trader.config.statics.*;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DigesterStaticLoaderTest {
    public static final String STATIC_TEST_CONFIG_XML = "StaticTestConfig.xml";

    @Test
    public void shouldLoad() throws Exception {
        DigesterStaticLoader uut = new DigesterStaticLoader();

        StaticConfiguration res = uut.load(STATIC_TEST_CONFIG_XML);

        assertMarkets(res);
        assertPortfolios(res);
        assertInstruments(res);
        assertAlgos(res);
    }

    private void assertInstruments(StaticConfiguration res) {
        Map<String, InstrumentConfig> instrumentById = Maps.newHashMap();

        for (InstrumentConfig config : res.getInstruments()) {
            instrumentById.put(config.getId(), config);
        }

        assertThat(instrumentById.size(), is(8));

    }

    private void assertPortfolios(StaticConfiguration res) {
        Map<String, PortfolioConfig> portfolioById = Maps.newHashMap();

        for (PortfolioConfig config : res.getPortfolios()) {
            portfolioById.put(config.getId(), config);
        }

        assertThat(portfolioById.size(), is(2));
    }

    private void assertMarkets(StaticConfiguration res) {
        Map<String, MarketConfig> marketsById = Maps.newHashMap();

        for (MarketConfig config : res.getMarkets()) {
            marketsById.put(config.getId(), config);
        }

        assertThat(marketsById.size(), is(2));
    }


    private void assertAlgos(StaticConfiguration res) {
        Map<String, AlgoConfig> algoBy = Maps.newHashMap();

        for (AlgoConfig config : res.getAlgos()) {
            algoBy.put(config.getName(), config);
        }

        assertThat(algoBy.size(), is(2));
    }
}
