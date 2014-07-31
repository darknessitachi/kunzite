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
import com.zaradai.kunzite.utils.FileIO;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EodConverterOptionsParserTest {
    private static final String TEST_KEY = "key";
    private static final String TEST_VALUE = "value";
    private static final String TEST_FOLDER = "c:\\a\\b\\c\\";
    private static final String TEST_TYPE = "CSV";
    private static final int TEST_NUM_THREADS = 4;
    private static final List<String> VALID_SYMBOLS = Lists.newArrayList("-sm", "INTC AMAT IBM");
    private static final List<String> VALID_PROPERTY = Lists.newArrayList("-p", "key=value");
    private static final List<String> VALID_SOURCE = Lists.newArrayList("-s", TEST_FOLDER);
    private static final List<String> VALID_SOURCE_TYPE = Lists.newArrayList("-i", TEST_TYPE);
    private static final List<String> VALID_TARGET = Lists.newArrayList("-t", TEST_FOLDER);
    private static final List<String> VALID_TARGET_TYPE = Lists.newArrayList("-o", TEST_TYPE);
    private static final List<String> VALID_THREADS = Lists.newArrayList("-th", Integer.toString(TEST_NUM_THREADS));

    private FileIO fileIO;
    private EodConverterOptionsParser uut;

    @Before
    public void setUp() throws Exception {
        fileIO = mock(FileIO.class);
        uut = new EodConverterOptionsParser(fileIO);
    }

    @Test
    public void shouldReadThreads() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_THREADS);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getThreads(), is(TEST_NUM_THREADS));
    }

    @Test
    public void shouldReadProperties() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_PROPERTY);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getProperties().getProperty(TEST_KEY), is(TEST_VALUE));
    }

    @Test
    public void shouldReadSourceType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getSourceType(), is(SupportedEodIO.Csv));
    }

    @Test
    public void shouldReadOddFormatSourceType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.add("-i");
        options.add("MONgo");
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getSourceType(), is(SupportedEodIO.Mongo));
    }

    @Test
    public void shouldFailToParseInvalidSourceType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.add("-i");
        options.add("Cassandra");
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldFailWithMissingSourceType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldReadTargetType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getTargetType(), is(SupportedEodIO.Csv));
    }

    @Test
    public void shouldReadOddFormatTargetType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.add("-o");
        options.add("MONgo");

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getTargetType(), is(SupportedEodIO.Mongo));
    }

    @Test
    public void shouldFailToParseInvalidTargetType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.add("-o");
        options.add("Cassandra");

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldFailWithMissingTargetType() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldReadValidSymbols() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getSymbols(), hasSize(3));
    }

    @Test
    public void shouldFailWithNoSuppliedSymbols() throws Exception {
        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldGetSourceFolder() throws Exception {
        when(fileIO.exists(TEST_FOLDER)).thenReturn(true);
        when(fileIO.isDirectory(TEST_FOLDER)).thenReturn(true);

        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_SOURCE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getSourceFolder(), is(TEST_FOLDER));
    }

    @Test
    public void shouldFailIfSourceFolderDoesNotExist() throws Exception {
        when(fileIO.exists(TEST_FOLDER)).thenReturn(false);
        when(fileIO.isDirectory(TEST_FOLDER)).thenReturn(true);

        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_SOURCE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldFailIfSourceFolderIsNotDirectory() throws Exception {
        when(fileIO.exists(TEST_FOLDER)).thenReturn(true);
        when(fileIO.isDirectory(TEST_FOLDER)).thenReturn(false);

        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_SOURCE);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldGetTargetFolder() throws Exception {
        when(fileIO.exists(TEST_FOLDER)).thenReturn(true);
        when(fileIO.isDirectory(TEST_FOLDER)).thenReturn(true);

        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_TARGET);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res.getTargetFolder(), is(TEST_FOLDER));
    }

    @Test
    public void shouldFailIfTargetFolderDoesNotExist() throws Exception {
        when(fileIO.exists(TEST_FOLDER)).thenReturn(false);
        when(fileIO.isDirectory(TEST_FOLDER)).thenReturn(true);

        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_TARGET);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldFailIfTargetFolderIsNotDirectory() throws Exception {
        when(fileIO.exists(TEST_FOLDER)).thenReturn(true);
        when(fileIO.isDirectory(TEST_FOLDER)).thenReturn(false);

        List<String> options = Lists.newArrayList();
        options.addAll(VALID_SYMBOLS);
        options.addAll(VALID_SOURCE_TYPE);
        options.addAll(VALID_TARGET_TYPE);
        options.addAll(VALID_TARGET);

        EodConverterOptions res = (EodConverterOptions) uut.parse(options.toArray(new String[0]));

        assertThat(res, is(nullValue()));
    }
}
