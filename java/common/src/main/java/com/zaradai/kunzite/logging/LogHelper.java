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

import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkArgument;

public final class LogHelper {
    private LogHelper() {

    }

    public static LogAssistant debug(ContextLogger logger) {
        return new LogAssistant(logger, logger.debug());
    }

    public static LogAssistant info(ContextLogger logger) {
        return new LogAssistant(logger, logger.info());
    }

    public static LogAssistant warn(ContextLogger logger) {
        return new LogAssistant(logger, logger.warn());
    }

    public static LogAssistant error(ContextLogger logger) {
        return new LogAssistant(logger, logger.error());
    }

    public static void exception(ContextLogger logger, String reason, Exception e) {
        new LogAssistant(logger, logger.error()).addContext("Exception").addReason(reason).add("msg", e.getMessage()).log();
    }

    public static final class LogAssistant {
        private static final String INVALID_NAME = "Invalid name specified";
        private static final String CONTEXT = "Context";
        private static final String REASON = "Reason";

        private final ContextLogger logger;
        private final LogEntry logEntry;

        private LogAssistant(ContextLogger logger, LogEntry logEntry) {
            this.logger = logger;
            this.logEntry = logEntry;
        }

        public LogAssistant addContext(Object value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addContext(boolean value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addContext(char value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addContext(double value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addContext(float value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addContext(int value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addContext(long value) {
            return add(CONTEXT, value);
        }

        public LogAssistant addReason(Object value) {
            return add(REASON, value);
        }

        public LogAssistant addReason(boolean value) {
            return add(REASON, value);
        }

        public LogAssistant addReason(char value) {
            return add(REASON, value);
        }

        public LogAssistant addReason(double value) {
            return add(REASON, value);
        }

        public LogAssistant addReason(float value) {
            return add(REASON, value);
        }

        public LogAssistant addReason(int value) {
            return add(REASON, value);
        }

        public LogAssistant addReason(long value) {
            return add(REASON, value);
        }

        public LogAssistant add(String name, Object value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, boolean value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, char value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, double value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, float value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, int value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, long value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public LogAssistant add(String name, String value) {
            checkArgument(!Strings.isNullOrEmpty(name), INVALID_NAME);
            logEntry.addEntry(name, String.valueOf(value));

            return this;
        }

        public void log() {
            logger.log(logEntry);
        }
    }
}
