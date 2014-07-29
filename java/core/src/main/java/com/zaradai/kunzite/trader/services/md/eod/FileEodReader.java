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

import com.google.inject.Inject;
import org.joda.time.DateTime;

import java.io.*;

public class FileEodReader implements EodReader {
    static final int HEADER_MAGIC_CODE = 0xE456D342;

    private final EodEncoder encoder;
    private DataInput dataInput = null;
    private final EodDayData last;

    @Inject
    FileEodReader(EodEncoder encoder) {
        this.encoder = encoder;
        last = new EodDayData();
    }

    @Override
    public void open(String uri) throws EodIOException {
        try {
            dataInput = openStream(uri);
            validateHeader();
        } catch (Exception e) {
            throw new EodIOException("Unable to open file", e);
        }
    }

    protected DataInput openStream(String uri) throws FileNotFoundException {
        return new DataInputStream(new BufferedInputStream(new FileInputStream(uri)));
    }

    private void validateHeader() throws EodIOException {
        try {
            if (dataInput.readInt() != HEADER_MAGIC_CODE) {
                throw new EodIOException("Header mismatch");
            }
        } catch (IOException e) {
            throw new EodIOException("Unable to read header data", e);
        }
    }

    @Override
    public EodDayData getNext(DateTime date) {
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
    public EodDayData getNext() {
        try {
            readNext();
        } catch (Exception e) {
            return null;
        }

        return last;
    }

    private void readNext() throws Exception {
        encoder.decode(dataInput, last);
    }

    @Override
    public void close() throws Exception {
        if (dataInput instanceof AutoCloseable) {
            ((AutoCloseable)dataInput).close();
        }
    }
}
