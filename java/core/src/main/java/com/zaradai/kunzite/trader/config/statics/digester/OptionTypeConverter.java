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
import org.apache.commons.beanutils.Converter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class OptionTypeConverter implements Converter {
    @Override
    public Object convert(Class type, Object value) {
        checkNotNull(type, "Type cant be null");
        checkNotNull(value, "Value cant be null");
        checkArgument(type.equals(OptionType.class), "Conversion target must be OptionType");
        checkArgument(String.class.isAssignableFrom(value.getClass()), "Value should be a string, but is a %s",
                value.getClass());


        return OptionType.valueOf((String) value);
    }
}
