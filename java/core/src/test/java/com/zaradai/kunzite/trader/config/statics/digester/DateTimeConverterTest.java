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

import com.zaradai.kunzite.trader.instruments.OptionType;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DateTimeConverterTest {
    private static final String TEST_VALUE = "25-Dec-2013";
    private static final Class TEST_TYPE = DateTime.class;

    @Test(expected = NullPointerException.class)
    public void shouldCatchNullType() throws Exception {
        DateTimeConverter uut = new DateTimeConverter();

        uut.convert(null, TEST_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCatchNullValue() throws Exception {
        DateTimeConverter uut = new DateTimeConverter();

        uut.convert(TEST_TYPE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidType() throws Exception {
        DateTimeConverter uut = new DateTimeConverter();

        uut.convert(OptionType.class, TEST_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidValueType() throws Exception {
        DateTimeConverter uut = new DateTimeConverter();

        uut.convert(TEST_TYPE, new Integer(24));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowInvalidFormatForIsoDateFormatter() throws Exception {
        DateTimeConverter uut = new DateTimeConverter();

        uut.convert(TEST_TYPE, TEST_VALUE);
    }

    @Test
    public void shouldConvertIsoDateFormat() throws Exception {
        String isoDate = "2013-12-25";
        DateTimeConverter uut = new DateTimeConverter();

        DateTime res = (DateTime) uut.convert(TEST_TYPE, isoDate);

        assertThat(res.year().get(), is(2013));
        assertThat(res.monthOfYear().get(), is(12));
        assertThat(res.dayOfMonth().get(), is(25));
    }

    @Test
    public void shouldConvertSpecificFormat() throws Exception {
        DateTimeConverter uut = new DateTimeConverter("dd-MMM-yyyy");

        DateTime res = (DateTime) uut.convert(TEST_TYPE, TEST_VALUE);

        assertThat(res.year().get(), is(2013));
        assertThat(res.monthOfYear().get(), is(12));
        assertThat(res.dayOfMonth().get(), is(25));
    }
}
