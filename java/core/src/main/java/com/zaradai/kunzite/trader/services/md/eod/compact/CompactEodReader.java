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
package com.zaradai.kunzite.trader.services.md.eod.compact;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.trader.services.md.eod.EodData;
import com.zaradai.kunzite.trader.services.md.eod.EodIOException;
import com.zaradai.kunzite.trader.services.md.eod.EodReader;
import org.joda.time.DateTime;

import java.io.*;

/**
 * Reads EOD data for a given symbol from a binary encoded file.
 * The file is located in a supplied folder with a filename of Symbol.bmd
 */
public class CompactEodReader implements EodReader {

    private final EodEncoder encoder;
    private final String folder;
    private DataInput dataInput = null;
    private EodData last;
    private String symbol;

    @Inject
    CompactEodReader(EodEncoder encoder, @Assisted String folder) {
        this.encoder = encoder;
        this.folder = folder;
        last = new EodData();
    }

    @Override
    public void open(String symbol) throws EodIOException {
        this.symbol = symbol;
        try {
            dataInput = openStream(CompactIO.getFilename(folder, symbol));
            validateHeader();
            last = EodData.START_EOD;
        } catch (Exception e) {
            throw new EodIOException("Unable to open file", e);
        }
    }

    protected DataInput openStream(String uri) throws FileNotFoundException {
        return new DataInputStream(new BufferedInputStream(new FileInputStream(uri)));
    }

    private void validateHeader() throws EodIOException {
        try {
            if (dataInput.readInt() != CompactIO.HEADER_MAGIC_CODE) {
                throw new EodIOException("Header mismatch");
            }
        } catch (IOException e) {
            throw new EodIOException("Unable to read header data", e);
        }
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
        last = encoder.decode(dataInput);
        // for  efficiency of space symbol is not stored with data as the filename contains this information
        last.setSymbol(symbol);
    }

    @Override
    public void close() throws Exception {
        if (dataInput instanceof AutoCloseable) {
            ((AutoCloseable)dataInput).close();
        }
    }
}
