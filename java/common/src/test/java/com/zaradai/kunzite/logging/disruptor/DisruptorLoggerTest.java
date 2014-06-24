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
package com.zaradai.kunzite.logging.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.zaradai.kunzite.logging.LogEntry;
import com.zaradai.kunzite.logging.LogLevel;
import com.zaradai.kunzite.mocks.LogEntryMocker;
import com.zaradai.kunzite.mocks.LoggerMocker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

public class DisruptorLoggerTest {
    @Mock
    Disruptor<LogEntry> disruptorMock;
    @Mock
    ExecutorService executorServiceMock;

    private Logger logger;
    private DisruptorLogger uut;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        logger = LoggerMocker.create();
        uut = new DisruptorLogger(logger) {
            @Override
            protected Disruptor<LogEntry> createDisruptor() {
                return disruptorMock;
            }

            @Override
            protected ExecutorService createExecutorService() {
                return executorServiceMock;
            }
        };
    }

    @Test
    public void shouldConstructProperly() throws Exception {
        verify(disruptorMock).handleEventsWith(uut);
        verify(disruptorMock).start();
    }

    @Test
    public void shouldReturnErrorEntry() throws Exception {
        LogEntry entry = uut.error();

        assertThat(entry.getLogLevel(), is(LogLevel.Error));
    }

    @Test
    public void shouldReturnWarnEntry() throws Exception {
        LogEntry entry = uut.warn();

        assertThat(entry.getLogLevel(), is(LogLevel.Warn));
    }

    @Test
    public void shouldReturnInfoEntry() throws Exception {
        LogEntry entry = uut.info();

        assertThat(entry.getLogLevel(), is(LogLevel.Info));
    }

    @Test
    public void shouldReturnDebugEntry() throws Exception {
        LogEntry entry = uut.debug();

        assertThat(entry.getLogLevel(), is(LogLevel.Debug));
    }

    @Test
    public void shouldLog() throws Exception {
        LogEntry entry = LogEntry.newInstance();

        uut.log(entry);

        verify(disruptorMock).publishEvent(uut, entry);
    }

    @Test
    public void shouldShutdown() throws Exception {
        uut.shutdown();

        verify(disruptorMock).shutdown();
        verify(executorServiceMock).shutdown();
    }

    @Test
    public void shouldLogDebug() throws Exception {
        LogEntry entry = LogEntry.newInstanceWithLevel(LogLevel.Debug);

        uut.onEvent(entry, 0, false);

        verify(logger).debug(anyString());
    }

    @Test
    public void shouldLogInfo() throws Exception {
        LogEntry entry = LogEntry.newInstanceWithLevel(LogLevel.Info);

        uut.onEvent(entry, 0, false);

        verify(logger).info(anyString());
    }

    @Test
    public void shouldLogWarn() throws Exception {
        LogEntry entry = LogEntry.newInstanceWithLevel(LogLevel.Warn);

        uut.onEvent(entry, 0, false);

        verify(logger).warn(anyString());
    }

    @Test
    public void shouldLogError() throws Exception {
        LogEntry entry = LogEntry.newInstanceWithLevel(LogLevel.Error);

        uut.onEvent(entry, 0, false);

        verify(logger).error(anyString());
    }

    @Test
    public void shouldTranslateCorrectly() throws Exception {
        LogEntry from = LogEntryMocker.create();
        LogEntry to = LogEntryMocker.create();

        uut.translateTo(to, 0, from);

        verify(to).copyFrom(from);
    }

    @Test
    public void shouldStartupThenShutdownWithoutErrors() throws Exception {
        uut = new DisruptorLogger(logger);

        uut.shutdown();
    }
}
