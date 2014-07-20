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
package com.zaradai.kunzite.trader.config.orders.digester;

import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.orders.ConfigLoader;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfiguration;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class GatewayLoader implements ConfigLoader {
    @Override
    public OrderGatewayConfiguration load(String sourceUri) throws ConfigException {
        InputStream inputStream = getStream(sourceUri);
        Digester digester = getDigester();

        try {
            return digester.parse(inputStream);
        } catch (SAXException e) {
            throw new ConfigException("Unable to load config file", e);
        } catch (IOException e) {
            throw new ConfigException("Unable to parse config file", e);
        }
    }

    protected InputStream getStream(String sourceUri) {
        return GatewayModule.class.getClassLoader().getResourceAsStream(sourceUri);
    }

    protected Digester getDigester() {
        DigesterLoader loader = DigesterLoader.newLoader(new GatewayModule());

        return loader.newDigester();
    }
}
