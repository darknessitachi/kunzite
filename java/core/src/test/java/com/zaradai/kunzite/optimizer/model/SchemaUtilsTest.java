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
package com.zaradai.kunzite.optimizer.model;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SchemaUtilsTest {
    @Test
    public void shouldGenerateStringFromArrayMaxShort() throws Exception {
        int[] values = new int[] { 27, 32321, 4567, 8925};

        assertThat(SchemaUtils.intArrayToHexUsingShort(values), is("001B7E4111D722DD"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidValueUsingShort() throws Exception {
        int[] values = new int[] { 27, 65536, 4567, 8925};

        SchemaUtils.intArrayToHexUsingShort(values);
    }

    @Test
    public void shouldGenerateStringFromArrayMaxByte() throws Exception {
        int[] values = new int[] { 27, 126, 44, 159};

        assertThat(SchemaUtils.intArrayToHexUsingByte(values), is("1B7E2C9F"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCatchInvalidValueUsingByte() throws Exception {
        int[] values = new int[] { 27, 257, 44, 159 };

        SchemaUtils.intArrayToHexUsingByte(values);
    }
}
