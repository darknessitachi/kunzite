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
import com.zaradai.kunzite.trader.services.md.eod.EodReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvEodReader implements EodReader {
    private BufferedReader reader;
    private final String folder;
    private EodData last;
    private String symbol;
    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    CsvEodReader(@Assisted String folder) {
        this.folder = folder;
        dateTimeFormatter = DateTimeFormat.forPattern(CsvIO.DATE_FORMAT_PATTERN);
    }

    @Override
    public void open(String symbol) throws EodIOException {
        this.symbol = symbol;
        try {
            reader = openStream(CsvIO.getFilename(folder, symbol));
            validateHeader();
            last = EodData.START_EOD;
        } catch (Exception e) {
            throw new EodIOException("Unable to open file", e);
        }
    }

    protected BufferedReader openStream(String uri) throws FileNotFoundException {
        return new BufferedReader(new FileReader(uri));
    }

    private void validateHeader() throws IOException {
        // read and drop the header row
        reader.readLine();
    }

    @Override
    public EodData getNext(DateTime date) {
        while (last.getDate().isBefore(date)) {
            try {
                readNext();
            } catch (Exception e) {
                return null;
            }
        }

        return last.getDate().equals(date) ? last : null;
    }

    @Override
    public EodData getNext() {
        try {
            readNext();
        } catch (Exception e) {
            return null;
        }

        return last;
    }

    private void readNext() throws Exception {
        String row = reader.readLine();
        String[] columns = row.split(",");
        // create new eod item
        last = new EodData();
        last.setSymbol(symbol);
        last.setDate(DateTime.parse(columns[0], dateTimeFormatter));
        last.setOpen(Double.parseDouble(columns[1]));
        last.setHigh(Double.parseDouble(columns[2]));
        last.setLow(Double.parseDouble(columns[3]));
        last.setClose(Double.parseDouble(columns[4]));
        last.setClose(Long.parseLong(columns[5]));
    }


    @Override
    public void close() throws Exception {
        if (reader instanceof AutoCloseable) {
            ((AutoCloseable)reader).close();
        }
    }
}
