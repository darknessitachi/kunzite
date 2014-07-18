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

import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.config.statics.StaticLoader;
import com.zaradai.kunzite.trader.instruments.InstrumentType;
import com.zaradai.kunzite.trader.instruments.OptionType;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.joda.time.DateTime;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

public class DigesterStaticLoader implements StaticLoader {
    @Override
    public StaticConfiguration load(String sourceUri) throws ConfigException {
        StaticConfiguration res = new StaticConfiguration();
        // configure date time converter
        configureConverters();
        InputStream inputStream = getStream(sourceUri);
        Digester digester = getDigester();

        try {
            res = digester.parse(inputStream);
        } catch (SAXException e) {
            throw new ConfigException("Unable to load config file", e);
        } catch (IOException e) {
            throw new ConfigException("Unable to parse config file", e);
        }

        return res;
    }

    private void configureConverters() {
        ConvertUtils.register(new DateTimeConverter("dd-MMM-yyyy"), DateTime.class);
        ConvertUtils.register(new OptionTypeConverter(), OptionType.class);
        ConvertUtils.register(new InstrumentTypeConverter(), InstrumentType.class);
    }

    protected InputStream getStream(String sourceUri) {
        return StaticModule.class.getClassLoader().getResourceAsStream(sourceUri);
    }

    protected Digester getDigester() {
        DigesterLoader loader = DigesterLoader.newLoader(new StaticModule());

        return loader.newDigester();
    }
}
