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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompactIOTest {
    private static final String TEST_SYMBOL = "INTC";
    private static final String TEST_PATH_TRAILING = "c:\\a\\b\\c\\";
    private static final String TEST_PATH_NO_TRAILING = "c:\\a\\b\\c";
    private static final String VALID_PATH = "c:\\a\\b\\c\\INTC.bmd";

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfPathIsNull() throws Exception {
        CompactIO.getFilename(null, TEST_SYMBOL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfPathIsEmpty() throws Exception {
        CompactIO.getFilename("", TEST_SYMBOL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfSymbolIsNull() throws Exception {
        CompactIO.getFilename(TEST_PATH_TRAILING, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfSymbolIsEmpty() throws Exception {
        CompactIO.getFilename(TEST_PATH_TRAILING, "");
    }

    @Test
    public void shouldConstructFilenameWithTrainingPath() throws Exception {
        assertThat(CompactIO.getFilename(TEST_PATH_TRAILING, TEST_SYMBOL), is(VALID_PATH));
    }

    @Test
    public void shouldConstructFilenameWithNoTrainingPath() throws Exception {
        assertThat(CompactIO.getFilename(TEST_PATH_NO_TRAILING, TEST_SYMBOL), is(VALID_PATH));
    }
}
