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

import com.zaradai.kunzite.trader.instruments.InstrumentType;
import com.zaradai.kunzite.trader.instruments.OptionType;
import org.junit.Test;

public class InstrumentTypeConverterTest {
    private static final Class TEST_TYPE = InstrumentType.class;
    public static final String VALUE = "Stock";

    @Test(expected = NullPointerException.class)
    public void shouldCatchNullType() throws Exception {
        InstrumentTypeConverter uut = new InstrumentTypeConverter();

        uut.convert(null, VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCatchNullValue() throws Exception {
        InstrumentTypeConverter uut = new InstrumentTypeConverter();

        uut.convert(TEST_TYPE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidType() throws Exception {
        InstrumentTypeConverter uut = new InstrumentTypeConverter();

        uut.convert(OptionType.class, VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidValueType() throws Exception {
        InstrumentTypeConverter uut = new InstrumentTypeConverter();

        uut.convert(TEST_TYPE, new Integer(24));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowInvalidConversion() throws Exception {
        InstrumentTypeConverter uut = new InstrumentTypeConverter();

        uut.convert(TEST_TYPE, "Something");
    }

    @Test
    public void shouldConvert() throws Exception {
        InstrumentTypeConverter uut = new InstrumentTypeConverter();

        uut.convert(TEST_TYPE, VALUE);
    }
}
