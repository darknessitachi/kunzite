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

import com.google.common.base.Preconditions;

public final class SchemaUtils {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final int MAX_UNSIGNED_SHORT_VALUE = 0xFFFF;
    private static final int MAX_UNSIGNED_BYTE_VALUE = 0xFF;
    private static final int NUM_NIBBLE_IN_SHORT = 4;
    private static final int NUM_NIBBLE_IN_BYTE = 2;


    private SchemaUtils() {

    }

    public static String intArrayToHexUsingShort(int[] data) {
        char[] hexChars = new char[data.length * NUM_NIBBLE_IN_SHORT];

        for (int j = 0; j < data.length; j++) {
            Preconditions.checkArgument(data[j] <= MAX_UNSIGNED_SHORT_VALUE);

            hexChars[j * NUM_NIBBLE_IN_SHORT] = HEX_ARRAY[(data[j] & 0xF000) >>> 12];
            hexChars[j * NUM_NIBBLE_IN_SHORT + 1] = HEX_ARRAY[(data[j] & 0x0F00) >>> 8];
            hexChars[j * NUM_NIBBLE_IN_SHORT + 2] = HEX_ARRAY[(data[j] & 0x00F0) >>> 4];
            hexChars[j * NUM_NIBBLE_IN_SHORT + 3] = HEX_ARRAY[data[j] & 0x0F];
        }
        return new String(hexChars);
    }

    public static String intArrayToHexUsingByte(int[] data) {
        char[] hexChars = new char[data.length * NUM_NIBBLE_IN_BYTE];
        int v;
        for (int j = 0; j < data.length; j++) {
            Preconditions.checkArgument(data[j] <= MAX_UNSIGNED_BYTE_VALUE);

            v = data[j] & 0xFF;
            hexChars[j * NUM_NIBBLE_IN_BYTE] = HEX_ARRAY[v >>> 4];
            hexChars[j * NUM_NIBBLE_IN_BYTE + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
