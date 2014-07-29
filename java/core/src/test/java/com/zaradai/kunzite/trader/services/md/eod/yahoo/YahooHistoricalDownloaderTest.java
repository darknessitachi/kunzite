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
package com.zaradai.kunzite.trader.services.md.eod.yahoo;

import com.zaradai.kunzite.trader.services.md.eod.EodData;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class YahooHistoricalDownloaderTest {
    public static final int SIZE_OF_DATA = 186;
    private static final DateTime FROM_DATE = new DateTime(2013, 6, 15, 0, 0);
    private static final DateTime UNTIL_DATE = new DateTime(2014, 3, 12, 0, 0);
    private static final String SYMBOL = "DATA";

    @Test
    public void shouldDownload() throws Exception {
        YahooHistoricalDownloader uut = new YahooHistoricalDownloader(new YahooQuoteUrlFormatter());

        Map<DateTime, EodData> data = uut.download(SYMBOL, FROM_DATE, UNTIL_DATE);

        assertThat(data.size(), is(SIZE_OF_DATA));
    }
}
