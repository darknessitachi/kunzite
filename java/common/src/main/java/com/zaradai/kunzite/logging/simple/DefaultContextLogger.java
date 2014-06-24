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

import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogEntry;
import com.zaradai.kunzite.logging.LogLevel;
import org.slf4j.Logger;

public class DefaultContextLogger implements ContextLogger {
    private final Logger logger;

    @Inject
    public DefaultContextLogger(Logger logger) {
        this.logger = logger;
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

    @Override
    public void shutdown() {
        //nop
    }
}
