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
package com.zaradai.kunzite.trader.services.md.eod;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import static com.google.common.base.Preconditions.checkNotNull;

public class CsvFileEodWriter implements EodWriter {
    private BufferedWriter writer;
    private final DateTimeFormatter dateTimeFormatter;
    private final DecimalFormat doubleFormat;

    public CsvFileEodWriter() {
        dateTimeFormatter = DateTimeFormat.forPattern("MM/dd/YYYY");
        doubleFormat = new DecimalFormat("0.00");
    }

    @Override
    public void open(String uri) throws EodIOException {
        try {
            writer = new BufferedWriter(new FileWriter(uri));
            writeHeader();
        } catch (IOException e) {
            throw new EodIOException("Unable to open file for writing", e);
        }
    }

    private void writeHeader() throws IOException {
        writer.write("Date,Symbol,Open,High,Low,Close,Volume");
        writer.newLine();
    }

    @Override
    public void write(EodDayData eodDayData) throws Exception {
        checkNotNull(eodDayData);

        for (EodData eodData : eodDayData.getData()) {
            write(eodDayData.getDate(), eodData);
        }
    }

    private void write(DateTime date, EodData eodData) throws IOException {
        writer.write(date.toString(dateTimeFormatter));
        writer.write(",");
        writer.write(eodData.getSymbol());
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
