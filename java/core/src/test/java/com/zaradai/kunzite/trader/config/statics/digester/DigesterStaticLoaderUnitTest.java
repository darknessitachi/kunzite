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
package com.zaradai.kunzite.trader.config.statics.digester;

import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.instruments.InstrumentType;
import com.zaradai.kunzite.trader.instruments.OptionType;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.digester3.Digester;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class DigesterStaticLoaderUnitTest {
    private InputStream stream;
    private Digester digester;
    private DigesterStaticLoader uut;

    @Before
    public void setUp() throws Exception {
        stream = mock(InputStream.class);
        digester = mock(Digester.class);
        uut = new DigesterStaticLoader() {
            @Override
            protected InputStream getStream(String sourceUri) {
                return stream;
            }

            @Override
            protected Digester getDigester() {
                return digester;
            }
        };
    }

    @Test
    public void shouldRegisterConverters() throws Exception {
        uut.load("");

        assertThat(ConvertUtils.lookup(DateTime.class), instanceOf(DateTimeConverter.class));
        assertThat(ConvertUtils.lookup(OptionType.class), instanceOf(OptionTypeConverter.class));
        assertThat(ConvertUtils.lookup(InstrumentType.class), instanceOf(InstrumentTypeConverter.class));
    }

    @Test(expected = ConfigException.class)
    public void shouldCatchSaxException() throws Exception {
        doThrow(SAXException.class).when(digester).parse(stream);

        uut.load("");
    }

    @Test(expected = ConfigException.class)
    public void shouldCatchIOException() throws Exception {
        doThrow(IOException.class).when(digester).parse(stream);

        uut.load("");
    }

}
