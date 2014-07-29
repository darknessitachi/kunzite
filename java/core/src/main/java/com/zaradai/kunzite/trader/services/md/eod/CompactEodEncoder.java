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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class CompactEodEncoder implements EodEncoder {
    @Override
    public void encode(DataOutput dataOutput, EodDayData data) throws Exception {
        checkNotNull(dataOutput, "Output is null");
        checkNotNull(data, "Invalid data to write");

        writeDate(dataOutput, data);
        writeData(dataOutput, data);
    }

    private void writeDate(DataOutput dataOutput, EodDayData data) throws IOException {
        DateTime date = data.getDate();

        dataOutput.writeShort(date.getYear());
        dataOutput.writeByte(date.getMonthOfYear());
        dataOutput.writeByte(date.getDayOfMonth());
    }

    private void writeData(DataOutput dataOutput, EodDayData data) throws IOException {
        dataOutput.writeInt(data.getData().size());

        for (EodData eodData : data.getData()) {
            writeEntry(dataOutput, eodData);
        }
    }

    private void writeEntry(DataOutput dataOutput, EodData entry) throws IOException {
        dataOutput.writeUTF(entry.getSymbol());
        dataOutput.writeDouble(entry.getOpen());
        dataOutput.writeDouble(entry.getHigh());
        dataOutput.writeDouble(entry.getLow());
        dataOutput.writeDouble(entry.getClose());
        dataOutput.writeLong(entry.getVolume());
    }

    @Override
    public void decode(DataInput dataInput, EodDayData data) throws Exception {
        checkNotNull(dataInput, "Input is null");
        checkNotNull(data, "Invalid data to populate");

        readDate(dataInput, data);
        readData(dataInput, data);
    }

    private void readDate(DataInput dataInput, EodDayData data) throws IOException {
        short year = dataInput.readShort();
        byte month = dataInput.readByte();
        byte day = dataInput.readByte();

        data.setDate(new DateTime(year, month, day, 0, 0));
    }


    private void readData(DataInput dataInput, EodDayData data) throws IOException {
        data.clear();
        int items = dataInput.readInt();

        for (int i = 0; i < items; ++i) {
            readEntry(dataInput, data);
        }
    }

    private void readEntry(DataInput dataInput, EodDayData data) throws IOException {
        EodData entry = new EodData();
        entry.setSymbol(dataInput.readUTF());
        entry.setOpen(dataInput.readDouble());
        entry.setHigh(dataInput.readDouble());
        entry.setLow(dataInput.readDouble());
        entry.setClose(dataInput.readDouble());
        entry.setVolume(dataInput.readLong());
        // add to last data
        data.add(entry);
    }
}
