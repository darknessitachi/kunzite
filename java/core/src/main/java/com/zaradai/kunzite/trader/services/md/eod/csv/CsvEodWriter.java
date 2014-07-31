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

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.trader.services.md.eod.EodData;
import com.zaradai.kunzite.trader.services.md.eod.EodIOException;
import com.zaradai.kunzite.trader.services.md.eod.EodWriter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import static com.google.common.base.Preconditions.checkNotNull;

public class CsvEodWriter implements EodWriter {
    private final String folder;
    private BufferedWriter writer;
    private final DateTimeFormatter dateTimeFormatter;
    private final DecimalFormat doubleFormat;

    @Inject
    CsvEodWriter(@Assisted String folder) {
        this.folder = folder;
        dateTimeFormatter = DateTimeFormat.forPattern(CsvIO.DATE_FORMAT_PATTERN);
        doubleFormat = new DecimalFormat("0.00");
    }

    @Override
    public void open(String symbol) throws EodIOException {
        try {
            writer = new BufferedWriter(new FileWriter(CsvIO.getFilename(folder, symbol)));
            writeHeader();
        } catch (IOException e) {
            throw new EodIOException("Unable to open file for writing", e);
        }
    }

    private void writeHeader() throws IOException {
        writer.write(CsvIO.HEADER_ROW);
        writer.newLine();
    }

    @Override
    public void write(EodData eodData) throws Exception {
        checkNotNull(eodData);

        writer.write(eodData.getDate().toString(dateTimeFormatter));
        writer.write(",");
        writer.write(doubleFormat.format(eodData.getOpen()));
        writer.write(",");
        writer.write(doubleFormat.format(eodData.getHigh()));
        writer.write(",");
        writer.write(doubleFormat.format(eodData.getLow()));
        writer.write(",");
        writer.write(doubleFormat.format(eodData.getClose()));
        writer.write(",");
        writer.write(Long.toString(eodData.getVolume()));
        writer.newLine();
    }

    @Override
    public void close() throws Exception {
        writer.close();
    }
}
