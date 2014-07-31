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

import com.zaradai.kunzite.trader.services.md.eod.EodIOException;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInput;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class CompactEodReaderTest {
    private static final String TEST_FOLDER = "test";
    private EodEncoder eodEncoder;
    private CompactEodReader uut;
    private DataInput mockInput;

    @Before
    public void setUp() throws Exception {
        eodEncoder = mock(EodEncoder.class);
        mockInput = mock(DataInput.class);
        uut = new CompactEodReader(eodEncoder, TEST_FOLDER) {
            @Override
            protected DataInput openStream(String uri) throws FileNotFoundException {
                return mockInput;
            }
        };
    }

    @Test
    public void shouldOpenAndTestHeader() throws Exception {
        when(mockInput.readInt()).thenReturn(CompactIO.HEADER_MAGIC_CODE);

        uut.open("test");
    }

    @Test(expected = EodIOException.class)
    public void shouldFailToOpenIfHeaderWrong() throws Exception {
        when(mockInput.readInt()).thenReturn(1234);

        uut.open("test");
    }

    @Test(expected = EodIOException.class)
    public void shouldFailIfOpenThrows() throws Exception {
        uut = new CompactEodReader(eodEncoder, TEST_FOLDER) {
            @Override
            protected DataInput openStream(String uri) throws FileNotFoundException {
                throw new FileNotFoundException("Error");
            }
        };

        uut.open("test");
    }

    @Test(expected = EodIOException.class)
    public void shouldFailIfValidateHeaderThrows() throws Exception {
        doThrow(IOException.class).when(mockInput).readInt();
        uut.open("test");
    }
}
