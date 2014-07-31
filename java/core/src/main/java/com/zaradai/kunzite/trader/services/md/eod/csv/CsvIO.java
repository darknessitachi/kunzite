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
package com.zaradai.kunzite.trader.services.md.eod.csv;

import com.google.common.base.Strings;

import java.io.File;

import static com.google.common.base.Preconditions.checkArgument;

public final class CsvIO {
    static final String DATE_FORMAT_PATTERN = "MM/dd/YYYY";
    static final String FILE_EXT = "csv";
    static final String HEADER_ROW = "Date,Open,High,Low,Close,Volume";

    private CsvIO() {
    }


    public static String getFilename(String folder, String symbol) {
        checkArgument(!Strings.isNullOrEmpty(folder));
        checkArgument(!Strings.isNullOrEmpty(symbol));

        StringBuilder sb = new StringBuilder(folder);

        if (folder.charAt(folder.length() - 1) != File.separatorChar) {
            sb.append(File.separatorChar);
        }
        sb.append(symbol);
        sb.append(".");
        sb.append(FILE_EXT);

        return sb.toString();
    }
}
