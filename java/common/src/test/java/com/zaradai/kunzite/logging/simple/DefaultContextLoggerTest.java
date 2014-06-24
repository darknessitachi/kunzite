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
package com.zaradai.kunzite.logging.simple;

import com.zaradai.kunzite.logging.LogEntry;
import com.zaradai.kunzite.logging.LogLevel;
import com.zaradai.kunzite.mocks.LoggerMocker;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

public class DefaultContextLoggerTest {
    @Test
    public void shouldLogError() throws Exception {
        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);

        LogEntry res = uut.error();

        assertThat(res, is(notNullValue()));
        assertThat(res.getLogLevel(), is(LogLevel.Error));
    }

    @Test
    public void testWarn() throws Exception {
        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);

        LogEntry res = uut.warn();

        assertThat(res, is(notNullValue()));
        assertThat(res.getLogLevel(), is(LogLevel.Warn));
    }

    @Test
    public void testInfo() throws Exception {
        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);

        LogEntry res = uut.info();

        assertThat(res, is(notNullValue()));
        assertThat(res.getLogLevel(), is(LogLevel.Info));
    }

    @Test
    public void testDebug() throws Exception {
        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);

        LogEntry res = uut.debug();

        assertThat(res, is(notNullValue()));
        assertThat(res.getLogLevel(), is(LogLevel.Debug));
    }

    @Test
    public void testLogError() throws Exception {
        final String TEST_NAME_1 = "name1";
        final String TEST_VALUE_1 = "value1";
        final String TEST_NAME_2 = "name2";
        final String TEST_VALUE_2 = "value2";
        final String TEST_TXT = TEST_NAME_1+"="+TEST_VALUE_1+", "+TEST_NAME_2+"="+TEST_VALUE_2;

        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);
        LogEntry res = uut.error();
        res.addEntry(TEST_NAME_1, TEST_VALUE_1);
        res.addEntry(TEST_NAME_2, TEST_VALUE_2);

        uut.log(res);

        verify(logger).error(TEST_TXT);
    }

    @Test
    public void testLogDebug() throws Exception {
        final String TEST_NAME_1 = "name1";
        final String TEST_VALUE_1 = "value1";
        final String TEST_NAME_2 = "name2";
        final String TEST_VALUE_2 = "value2";
        final String TEST_TXT = TEST_NAME_1+"="+TEST_VALUE_1+", "+TEST_NAME_2+"="+TEST_VALUE_2;

        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);
        LogEntry res = uut.debug();
        res.addEntry(TEST_NAME_1, TEST_VALUE_1);
        res.addEntry(TEST_NAME_2, TEST_VALUE_2);

        uut.log(res);

        verify(logger).debug(TEST_TXT);
    }

    @Test
    public void testLogWarn() throws Exception {
        final String TEST_NAME_1 = "name1";
        final String TEST_VALUE_1 = "value1";
        final String TEST_NAME_2 = "name2";
        final String TEST_VALUE_2 = "value2";
        final String TEST_TXT = TEST_NAME_1+"="+TEST_VALUE_1+", "+TEST_NAME_2+"="+TEST_VALUE_2;

        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);
        LogEntry res = uut.warn();
        res.addEntry(TEST_NAME_1, TEST_VALUE_1);
        res.addEntry(TEST_NAME_2, TEST_VALUE_2);

        uut.log(res);

        verify(logger).warn(TEST_TXT);
    }

    @Test
    public void testLogInfo() throws Exception {
        final String TEST_NAME_1 = "name1";
        final String TEST_VALUE_1 = "value1";
        final String TEST_NAME_2 = "name2";
        final String TEST_VALUE_2 = "value2";
        final String TEST_TXT = TEST_NAME_1+"="+TEST_VALUE_1+", "+TEST_NAME_2+"="+TEST_VALUE_2;

        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);
        LogEntry res = uut.info();
        res.addEntry(TEST_NAME_1, TEST_VALUE_1);
        res.addEntry(TEST_NAME_2, TEST_VALUE_2);

        uut.log(res);

        verify(logger).info(TEST_TXT);
    }

    @Test
    public void shouldStop() throws Exception {
        //make sure no exception
        Logger logger = LoggerMocker.create();
        DefaultContextLogger uut = new DefaultContextLogger(logger);

        uut.shutdown();
    }
}
