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

import com.zaradai.kunzite.trader.services.md.eod.EodData;
import org.joda.time.DateTime;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class CompactEodEncoder implements EodEncoder {
    @Override
    public void encode(DataOutput dataOutput, EodData data) throws Exception {
        checkNotNull(dataOutput, "Output is null");
        checkNotNull(data, "Invalid data to write");

        writeEntry(dataOutput, data);
    }

    private void writeEntry(DataOutput dataOutput, EodData entry) throws IOException {
        // write the date
        writeDate(dataOutput, entry);
        // now write the MD
        dataOutput.writeDouble(entry.getOpen());
        dataOutput.writeDouble(entry.getHigh());
        dataOutput.writeDouble(entry.getLow());
        dataOutput.writeDouble(entry.getClose());
        dataOutput.writeLong(entry.getVolume());
    }

    private void writeDate(DataOutput dataOutput, EodData entry) throws IOException {
        DateTime date = entry.getDate();

        dataOutput.writeShort(date.getYear());
        dataOutput.writeByte(date.getMonthOfYear());
        dataOutput.writeByte(date.getDayOfMonth());
    }

    @Override
    public EodData decode(DataInput dataInput) throws Exception {
        checkNotNull(dataInput, "Input is null");

        return readEntry(dataInput);
    }

    private EodData readEntry(DataInput dataInput) throws IOException {
        EodData res = new EodData();
        // read the date
        res.setDate(readDate(dataInput));
        // read the MD
        res.setOpen(dataInput.readDouble());
        res.setHigh(dataInput.readDouble());
        res.setLow(dataInput.readDouble());
        res.setClose(dataInput.readDouble());
        res.setVolume(dataInput.readLong());

        return res;
    }

    private DateTime readDate(DataInput dataInput) throws IOException {
        short year = dataInput.readShort();
        byte month = dataInput.readByte();
        byte day = dataInput.readByte();

        return new DateTime(year, month, day, 0, 0);
    }
}
