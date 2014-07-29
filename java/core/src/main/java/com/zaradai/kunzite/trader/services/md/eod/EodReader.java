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
package com.zaradai.kunzite.trader.services.md.eod;

import org.joda.time.DateTime;

public interface EodReader extends AutoCloseable {
    void open(String uri) throws EodIOException;
    /**
     * Gets a list of <Code>EodData</Code> data for the given date.
     * Note implementations will only read forward, so it is invalid to call getNext with a date previous
     * to last called
     * @param date
     * @return
     */
    EodDayData getNext(DateTime date);
    // Simple iterative reading of Eod Data
    EodDayData getNext();
}
