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
package com.zaradai.kunzite.trader.services.md;

import com.google.common.base.Strings;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import static com.google.common.base.Preconditions.checkArgument;

public class InjectedMarketDataChannelFactory implements MarketDataChannelFactory {
    private final Injector injector;

    @Inject
    InjectedMarketDataChannelFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public MarketDataChannel create(String clazz) throws MarketDataException {
        checkArgument(!Strings.isNullOrEmpty(clazz), "A valid market data channel class name needs to be supplied");

        try {
            return  (MarketDataChannel) injector.getInstance(Class.forName(clazz));
        } catch (ConfigurationException e) {
            throw new MarketDataException("Config issue", e);
        } catch (ProvisionException e) {
            throw new MarketDataException("Guice provision error", e);
        } catch (ClassNotFoundException e) {
            throw new MarketDataException("Channel class not found", e);
        }
    }
}
