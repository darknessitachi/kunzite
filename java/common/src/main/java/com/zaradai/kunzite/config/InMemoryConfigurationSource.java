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
package com.zaradai.kunzite.config;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class InMemoryConfigurationSource extends AbstractConfigurationSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryConfigurationSource.class);

    private final Map<String, String> configurationData;

    public InMemoryConfigurationSource() {
        configurationData = createConfigMap();
    }

    protected Map<String, String> createConfigMap() {
        return Maps.newConcurrentMap();
    }

    @Override
    public void addProperties(Properties properties) {
        storeValues(properties);
    }

    @Override
    public String get(String key) {
        return configurationData.get(key);
    }

    @Override
    public void set(String key, String value) {
        configurationData.put(key, value);
    }

    protected int getNumEntries() {
        return configurationData.size();
    }

    protected Set<String> getKeys() {
        return ImmutableSet.copyOf(configurationData.keySet());
    }

    private void storeValues(final Properties properties) {
        if (properties.isEmpty()) {
            return;
        }

        for (final String key : properties.stringPropertyNames()) {
            final String value = properties.getProperty(key);
            if (!Strings.isNullOrEmpty(value)) {
                set(key, value);
                LOGGER.debug("{}={}", key, value);
            }
        }
    }
}
