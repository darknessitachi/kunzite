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

import com.google.inject.Inject;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogEntry;
import com.zaradai.kunzite.logging.LogLevel;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorLogger implements ContextLogger, EventTranslatorOneArg<LogEntry, LogEntry>,
        EventHandler<LogEntry> {
    static final int RING_SIZE = 64;    // Logging is not so intensive so can keep ring size to minimum
    static final EventFactory<LogEntry> FACTORY = new EventFactory<LogEntry>()
    {
        public LogEntry newInstance()
        {
            return LogEntry.newInstance();
        }
    };

    private final ExecutorService executorService;
    private final Disruptor<LogEntry> disruptor;
    private final Logger logger;

    @Inject
    DisruptorLogger(Logger logger) {
        this.logger = logger;
        executorService = createExecutorService();
        disruptor = createDisruptor();
        disruptor.handleEventsWith(this);
        disruptor.start();
    }

    protected Disruptor<LogEntry> createDisruptor() {
        return new Disruptor<LogEntry>(FACTORY, RING_SIZE, executorService, ProducerType.MULTI,
                new SleepingWaitStrategy()); // wait strategy does not need to be CPU intensive
    }

    protected ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }

    @Override
    public LogEntry error() {
        return LogEntry.newInstanceWithLevel(LogLevel.Error);
    }

    @Override
    public LogEntry warn() {
        return LogEntry.newInstanceWithLevel(LogLevel.Warn);
    }

    @Override
    public LogEntry info() {
        return LogEntry.newInstanceWithLevel(LogLevel.Info);
    }

    @Override
    public LogEntry debug() {
        return LogEntry.newInstanceWithLevel(LogLevel.Debug);
    }

    @Override
    public void log(LogEntry logEntry) {
        disruptor.publishEvent(this, logEntry);
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
        executorService.shutdown();
    }

    @Override
    public void translateTo(LogEntry logEntry, long sequence, LogEntry arg0) {
        logEntry.copyFrom(arg0);
    }

    @Override
    public void onEvent(LogEntry logEntry, long sequence, boolean endOfBatch) throws Exception {
        switch (logEntry.getLogLevel()) {
            case Debug:
                logger.debug(logEntry.toString());
                break;
            case Info:
                logger.info(logEntry.toString());
                break;
            case Warn:
                logger.warn(logEntry.toString());
                break;
            case Error:
                logger.error(logEntry.toString());
                break;
        }
    }
}
