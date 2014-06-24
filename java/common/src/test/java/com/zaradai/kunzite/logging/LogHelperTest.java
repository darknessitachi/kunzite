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

import com.zaradai.kunzite.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.mocks.LogEntryMocker;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class LogHelperTest {
    @Test
    public void shouldLogToDebug() throws Exception {
        ContextLogger logger = ContextLoggerMocker.create();
        LogHelper.LogAssistant uut = LogHelper.debug(logger);

        verify(logger).debug();
    }

    @Test
    public void shouldLogToInfo() throws Exception {
        ContextLogger logger = ContextLoggerMocker.create();
        LogHelper.LogAssistant uut = LogHelper.info(logger);

        verify(logger).info();
    }

    @Test
    public void shouldLogToWarn() throws Exception {
        ContextLogger logger = ContextLoggerMocker.create();
        LogHelper.LogAssistant uut = LogHelper.warn(logger);

        verify(logger).warn();
    }

    @Test
    public void shouldLogToError() throws Exception {
        ContextLogger logger = ContextLoggerMocker.create();
        LogHelper.LogAssistant uut = LogHelper.error(logger);

        verify(logger).error();
    }

    @Test
    public void shouldLogLog() throws Exception {
        ContextLogger logger = ContextLoggerMocker.create();
        LogHelper.error(logger).log();

        verify(logger).error();
        verify(logger).log(any(LogEntry.class));
    }

    @Test
    public void shouldAddEntries() throws Exception {
        final String TEST = "Test";
        LogEntry logEntry = LogEntryMocker.create();
        ContextLogger logger = ContextLoggerMocker.create(logEntry);
        LogHelper.error(logger)
                .add(TEST, null)
                .add(TEST, false)
                .add(TEST, 0)
                .add(TEST, 0L)
                .add(TEST, 1.0)
                .add(TEST, (float)1.0)
                .add(TEST, 'c')
                .add(TEST, TEST)
                .log();

        verify(logger).error();
        verify(logger).log(any(LogEntry.class));
        verify(logEntry, times(8)).addEntry(any(String.class), any(String.class));
    }

    @Test
    public void shouldAddReason() throws Exception {
        final String TEST = "Test";
        LogEntry logEntry = LogEntryMocker.create();
        ContextLogger logger = ContextLoggerMocker.create(logEntry);
        LogHelper.error(logger)
                .addReason(null)
                .addReason(false)
                .addReason(0)
                .addReason(0L)
                .addReason(1.0)
                .addReason((float)1.0)
                .addReason('c')
                .addReason(TEST)
                .log();

        verify(logger).error();
        verify(logger).log(any(LogEntry.class));
        verify(logEntry, times(8)).addEntry(any(String.class), any(String.class));
    }

    @Test
    public void shouldAddContext() throws Exception {
        final String TEST = "Test";
        LogEntry logEntry = LogEntryMocker.create();
        ContextLogger logger = ContextLoggerMocker.create(logEntry);
        LogHelper.error(logger)
                .addContext(null)
                .addContext(false)
                .addContext(0)
                .addContext(0L)
                .addContext(1.0)
                .addContext((float)1.0)
                .addContext('c')
                .addContext(TEST)
                .log();

        verify(logger).error();
        verify(logger).log(any(LogEntry.class));
        verify(logEntry, times(8)).addEntry(any(String.class), any(String.class));
    }
}
