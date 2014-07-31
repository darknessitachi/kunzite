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
import com.zaradai.kunzite.trader.services.md.eod.EodWriter;

import java.io.*;

public class CompactEodWriter implements EodWriter {
    private final EodEncoder encoder;
    private final String folder;
    private DataOutput dataOutput = null;
    private String symbol;

    @Inject
    CompactEodWriter(EodEncoder encoder, @Assisted String folder) {
        this.encoder = encoder;
        this.folder = folder;
    }

    @Override
    public void open(String symbol) throws EodIOException {
        this.symbol = symbol;
        try {
            dataOutput = openStream(CompactIO.getFilename(folder, symbol));
            // write the header
            writeHeader();
        }catch (Exception e) {
            throw new EodIOException("Unable to open file", e);
        }
    }

    private void writeHeader() throws IOException {
        dataOutput.writeInt(CompactIO.HEADER_MAGIC_CODE);
    }

    protected DataOutput openStream(String uri) throws FileNotFoundException {
        return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(uri)));
    }

    @Override
    public void write(EodData eodData) throws Exception {
        encoder.encode(dataOutput, eodData);
    }

    @Override
    public void close() throws Exception {
        if (dataOutput instanceof AutoCloseable) {
            ((AutoCloseable)dataOutput).close();
        }
    }
}
