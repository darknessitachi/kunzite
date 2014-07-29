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
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class CompactEodEncoderTest {
    private static final int TEST_YEAR = 1999;
    private static final int TEST_MONTH = 6;
    private static final int TEST_DAY = 8;
    private static final DateTime TEST_DATE = new DateTime(TEST_YEAR, TEST_MONTH, TEST_DAY, 0, 0);
    private static final String TEST_SYMBOL = "INTC";
    public static final double TEST_OPEN = 12.21;
    public static final double TEST_HIGH = 21.12;
    public static final double TEST_LOW = 10.45;
    public static final double TEST_CLOSE = 11.11;
    public static final long TEST_VOLUME = 4500;

    private DataOutputStream mockOutput;
    private DataInputStream mockInput;

    @Before
    public void setUp() throws Exception {
        mockOutput = mock(DataOutputStream.class);
        mockInput = mock(DataInputStream.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToEncodeWithInvalidStream() throws Exception {
        CompactEodEncoder uut = new CompactEodEncoder();

        uut.encode(null, new EodDayData());
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToEncodeWithInvalidData() throws Exception {
        CompactEodEncoder uut = new CompactEodEncoder();

        uut.encode(mockOutput, null);
    }

    @Test
    public void shouldEncodeThenDecode() throws Exception {
        CompactEodEncoder uut = new CompactEodEncoder();
        EodData eodData = new EodData();
        eodData.setSymbol(TEST_SYMBOL);
        eodData.setVolume(TEST_VOLUME);
        eodData.setClose(TEST_CLOSE);
        eodData.setLow(TEST_LOW);
        eodData.setHigh(TEST_HIGH);
        eodData.setOpen(TEST_OPEN);
        EodDayData eodDayData = new EodDayData();
        eodDayData.setDate(TEST_DATE);
        eodDayData.add(eodData);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);

        uut.encode(outputStream, eodDayData);
        outputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        DataInputStream inputStream = new DataInputStream(byteArrayInputStream);

        EodDayData read = new EodDayData();
        uut.decode(inputStream, read);

        assertThat(read.getDate(), is(TEST_DATE));
        assertThat(read.getData().size(), is(1));
        EodData testEodData = read.getData().get(0);

        assertThat(testEodData.getSymbol(), is(TEST_SYMBOL));
        assertThat(testEodData.getOpen(), is(TEST_OPEN));
        assertThat(testEodData.getHigh(), is(TEST_HIGH));
        assertThat(testEodData.getLow(), is(TEST_LOW));
        assertThat(testEodData.getClose(), is(TEST_CLOSE));
        assertThat(testEodData.getVolume(), is(TEST_VOLUME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToDecodeWithInvalidStream() throws Exception {
        CompactEodEncoder uut = new CompactEodEncoder();

        uut.decode(null, new EodDayData());
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToDecodeWithInvalidData() throws Exception {
        CompactEodEncoder uut = new CompactEodEncoder();

        uut.decode(mockInput, null);
    }
}
