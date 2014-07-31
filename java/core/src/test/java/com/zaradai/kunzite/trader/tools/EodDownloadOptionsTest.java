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
package com.zaradai.kunzite.trader.tools;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.trader.services.md.eod.SupportedEodIO;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EodDownloadOptionsTest {
    private static final SupportedEodIO TEST_TAGET_TYPE = SupportedEodIO.Csv;
    private static final String TEST_STRING = "test";
    private static final List<String> TEST_SYMBOLS = Lists.newArrayList("AMAT", "IBM", "INTC");
    private static final Properties TEST_PROPERTIES = new Properties();
    private static final int TEST_INT = 4;
    private static final DateTime TEST_DATE = new DateTime(2014,12,25, 0, 0, DateTimeZone.UTC);
    private static final String TEST_GENERATED = "EodDownloadOptions[debug=false,targetType=Csv,targetFolder=test,symbols=[AMAT, IBM, INTC],properties={},threads=4,from=2014-12-25T00:00:00.000Z,until=2014-12-25T00:00:00.000Z]";

    private EodDownloadOptions uut;

    @Before
    public void setUp() throws Exception {
        uut = new EodDownloadOptions();
    }

    @Test
    public void shouldGetTargetType() throws Exception {
        uut.setTargetType(TEST_TAGET_TYPE);

        assertThat(uut.getTargetType(), is(TEST_TAGET_TYPE));
    }

    @Test
    public void shouldGetTargetFolder() throws Exception {
        uut.setTargetFolder(TEST_STRING);

        assertThat(uut.getTargetFolder(), is(TEST_STRING));
    }

    @Test
    public void shouldGetSymbols() throws Exception {
        uut.setSymbols(TEST_SYMBOLS);

        assertThat(uut.getSymbols(), is(TEST_SYMBOLS));
    }

    @Test
    public void shouldGetProperties() throws Exception {
        uut.setProperties(TEST_PROPERTIES);

        assertThat(uut.getProperties(), is(TEST_PROPERTIES));
    }

    @Test
    public void shouldGetThreads() throws Exception {
        uut.setThreads(TEST_INT);

        assertThat(uut.getThreads(), is(TEST_INT));
    }

    @Test
    public void shouldGetFrom() throws Exception {
        uut.setFrom(TEST_DATE);

        assertThat(uut.getFrom(), is(TEST_DATE));
    }

    @Test
    public void shouldGetUntil() throws Exception {
        uut.setUntil(TEST_DATE);

        assertThat(uut.getUntil(), is(TEST_DATE));
    }

    @Test
    public void shouldGenerateString() throws Exception {
        uut.setProperties(TEST_PROPERTIES);
        uut.setSymbols(TEST_SYMBOLS);
        uut.setUntil(TEST_DATE);
        uut.setFrom(TEST_DATE);
        uut.setThreads(TEST_INT);
        uut.setTargetFolder(TEST_STRING);
        uut.setTargetType(TEST_TAGET_TYPE);

        assertThat(uut.toString(), is(TEST_GENERATED));
    }
}
