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

import com.zaradai.kunzite.trader.config.statics.InstrumentConfig;
import com.zaradai.kunzite.trader.config.statics.MarketConfig;
import com.zaradai.kunzite.trader.config.statics.PortfolioConfig;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.instruments.TickDefinition;
import org.apache.commons.digester3.binder.AbstractRulesModule;

public class StaticModule  extends AbstractRulesModule {
    @Override
    protected void configure() {
        forPattern("statics").createObject().ofType(StaticConfiguration.class);
        // Markets
        forPattern("statics/markets/market").createObject().ofType(MarketConfig.class)
                .then().setProperties()
                .then().setNext("add");
        // ticks
        forPattern("statics/markets/market/ticks/tick").createObject().ofType(TickDefinition.class)
                .then().setProperties()
                .then().setNext("add");
        // Portfolios
        forPattern("statics/portfolios/portfolio").createObject().ofType(PortfolioConfig.class)
                .then().setProperties()
                .then().setNext("add");
        // instruments
        forPattern("statics/instruments/instrument").createObject().ofType(InstrumentConfig.class)
                .then().setProperties()
                .then().setNext("add");
        // handle instrument partof
        forPattern("statics/instruments/instrument/partof/security")
                .callMethod("addPartOf").usingElementBodyAsArgument();
        // handle instrument partof
        forPattern("statics/instruments/instrument/members/security")
                .callMethod("addMember").usingElementBodyAsArgument();
        // handle instrument baskets
        forPattern("statics/instruments/instrument/basket/security")
                .callMethod("addBasketConstituent").usingElementBodyAsArgument();
    }
}
