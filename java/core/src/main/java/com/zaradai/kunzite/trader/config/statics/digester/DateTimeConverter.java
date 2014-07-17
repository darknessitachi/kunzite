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

import org.apache.commons.beanutils.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DateTimeConverter implements Converter {
    private final DateTimeFormatter formatter;

    public DateTimeConverter() {
        formatter = ISODateTimeFormat.dateOptionalTimeParser();
    }

    public DateTimeConverter(String format) {
        formatter = DateTimeFormat.forPattern(format);
    }

    @Override
    public Object convert(Class type, Object value) {
        checkNotNull(type, "Type cant be null");
        checkNotNull(value, "Value cant be null");
        checkArgument(type.equals(DateTime.class), "Conversion target must be Joda DateTime");
        checkArgument(String.class.isAssignableFrom(value.getClass()), "Value should be a string, but is a %s",
                value.getClass());

        DateTime dateTime = formatter.parseDateTime((String) value);

        return dateTime;
    }
}
