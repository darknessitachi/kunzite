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

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.zaradai.kunzite.config.ConfigurationSource;
import org.junit.Before;
import org.slf4j.LoggerFactory;

public class BaseTraderTest {
    private Injector injector;
    private ConfigurationSource source;

    @Before
    public void setUp() throws Exception {
        printLogbackSettings();

        injector = Guice.createInjector(getTraderModule());
        // get the config source
        source = injector.getInstance(ConfigurationSource.class);

    }

    private void printLogbackSettings() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        // print logback's internal status
        StatusPrinter.print(lc);
    }

    protected Module getTraderModule() {
        return new TraderModule();
    }

    protected Injector getInjector() {
        return injector;
    }

    protected ConfigurationSource getSource() {
        return source;
    }
}
