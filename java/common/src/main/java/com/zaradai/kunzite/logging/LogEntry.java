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

import com.google.common.collect.Lists;
import org.javatuples.Pair;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class LogEntry {
    private final List<Pair<String, String>> entries;
    private LogLevel logLevel;

    private LogEntry() {
        entries = createEntryList();
    }

    private List<Pair<String, String>> createEntryList() {
        return Lists.newArrayList();
    }

    public static LogEntry newInstance() {
        return new LogEntry();
    }

    public static LogEntry newInstanceWithLevel(LogLevel logLevel) {
        checkNotNull(logLevel, "Invalid log level specified");

        LogEntry res = new LogEntry();
        res.setLogLevel(logLevel);

        return res;
    }

    public void addEntry(String name, String value) {
        entries.add(Pair.with(name, value));
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public String toString() {
        String separator = "";
        StringBuilder sb = new StringBuilder(32);

        for(Pair<String, String> entry : entries) {
            sb.append(separator);
            sb.append(entry.getValue0());
            sb.append("=");
            sb.append(entry.getValue1());
            separator = ", ";
        }

        return sb.toString();
    }

    public void copyFrom(LogEntry logEntry) {
        entries.clear();
        entries.addAll(logEntry.entries);
        logLevel = logEntry.logLevel;
    }
}
