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
package com.zaradai.kunzite.trader;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.inject.Module;
import com.zaradai.kunzite.trader.config.SourcedTraderConfiguration;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class TraderTest extends BaseTraderTest {
    @Test
    public void shouldRun() throws Exception {
        // configure the config sources
        // configure the tactic
        getSource().set(SourcedTraderConfiguration.STATIC_CONFIG_URI, "trader.xml");
        getSource().set(SourcedTraderConfiguration.OG_URI, "ordergateways.xml");
        getSource().set(SourcedTraderConfiguration.MD_CONFIG_URI, "marketdata.xml");
        // get the trader
        Trader trader = getInjector().getInstance(Trader.class);
        //
        trader.startAsync().awaitRunning();
        Uninterruptibles.sleepUninterruptibly(10, TimeUnit.SECONDS);
        trader.stopAsync().awaitTerminated();
    }

    @Override
    protected Module getTraderModule() {
        return new TraderTestModule();
    }
}
