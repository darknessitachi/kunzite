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
package com.zaradai.kunzite.logging;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class LogEntryTest {
    @Test
    public void shouldNewInstance() throws Exception {
        LogEntry res = LogEntry.newInstance();

        assertThat(res, is(notNullValue()));
    }

    @Test
    public void shouldCreateWithLevel() throws Exception {
        LogEntry uut = LogEntry.newInstanceWithLevel(LogLevel.Debug);

        assertThat(uut.getLogLevel(), is(LogLevel.Debug));
    }

    @Test
    public void shouldGetLogLevel() throws Exception {
        final LogLevel TEST_LOG_LEVEL = LogLevel.Debug;
        LogEntry uut = LogEntry.newInstance();

        uut.setLogLevel(TEST_LOG_LEVEL);

        assertThat(uut.getLogLevel(), is(TEST_LOG_LEVEL));
    }

    @Test
    public void shouldFormatStringBasedOnSingleEntries() throws Exception {
        final String KEY = "key";
        final String VALUE = "value";
        final String TEST_STRING = KEY + "=" + VALUE;

        LogEntry uut = LogEntry.newInstance();

        uut.addEntry(KEY, VALUE);

        assertThat(uut.toString(), is(TEST_STRING));
    }

    @Test
    public void shouldFormatStringBasedOnMultipleEntries() throws Exception {
        final String KEY1 = "key1";
        final String KEY2 = "key2";
        final String VALUE1 = "value1";
        final String VALUE2 = "value2";
        final String TEST_STRING = KEY1 + "=" + VALUE1 + ", " + KEY2 + "=" + VALUE2;

        LogEntry uut = LogEntry.newInstance();

        uut.addEntry(KEY1, VALUE1);
        uut.addEntry(KEY2, VALUE2);

        assertThat(uut.toString(), is(TEST_STRING));
    }
}
