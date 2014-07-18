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
package com.zaradai.kunzite.trader.algo;

import com.google.common.base.Strings;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.zaradai.kunzite.trader.control.TradingState;

import static com.google.common.base.Preconditions.checkArgument;

public class InjectedAlgoFactory implements AlgoFactory {
    private final Injector injector;

    @Inject
    InjectedAlgoFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Algo create(String name, TradingState tradingState) throws AlgoException {
        checkArgument(!Strings.isNullOrEmpty(name), "A valid algo name needs to be supplied");

        Algo res = null;

        try {
            res = (Algo) injector.getInstance(Class.forName(name));
            res.setState(tradingState);
        } catch (ConfigurationException e) {
            throw new AlgoException("Creating Algo", e);
        } catch (ProvisionException e) {
            throw new AlgoException("Creating Algo", e);
        } catch (ClassNotFoundException e) {
            throw new AlgoException("Creating Algo", e);
        }

        return res;
    }
}
