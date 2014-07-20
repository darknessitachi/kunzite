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
package com.zaradai.kunzite.trader.services.orders;

import com.google.common.base.Strings;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

import static com.google.common.base.Preconditions.checkArgument;

public class InjectedGatewayFactory implements OrderGatewayFactory {
    private final Injector injector;

    @Inject
    InjectedGatewayFactory(Injector injector) {
        this.injector = injector;
    }

    @Override
    public OrderGateway create(String name) throws GatewayException {
        checkArgument(!Strings.isNullOrEmpty(name), "A valid gateway class name needs to be supplied");

        try {
            return  (OrderGateway) injector.getInstance(Class.forName(name));
        } catch (ConfigurationException e) {
            throw new GatewayException("Config issue", e);
        } catch (ProvisionException e) {
            throw new GatewayException("Guice provision error", e);
        } catch (ClassNotFoundException e) {
            throw new GatewayException("Gateway class not found", e);
        }
    }
}
