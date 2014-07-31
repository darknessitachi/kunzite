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
package com.zaradai.kunzite.trader.services.md.eod.download.yahoo;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class YahooQuoteUrlFormatterTest {
    private static final DateTime FROM_DATE = new DateTime(2013, 6, 15, 0, 0);
    private static final DateTime UNTIL_DATE = new DateTime(2014, 3, 12, 0, 0);
    private static final String SYMBOL = "DATA";

    private static final String EXPECTED = "http://real-chart.finance.yahoo.com/table.csv?s=DATA&a=5&b=15&c=2013&d=2&e=12&f=2014&ignore=.csv";

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithNullSymbol() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();
        uut.getUrl(null, FROM_DATE, UNTIL_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithEmptySymbol() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();
        uut.getUrl("", FROM_DATE, UNTIL_DATE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithNullFromDate() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();
        uut.getUrl(SYMBOL, null, UNTIL_DATE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithNullUntilDate() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();
        uut.getUrl(SYMBOL, FROM_DATE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenDatesAreSame() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();
        uut.getUrl(SYMBOL, FROM_DATE, FROM_DATE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWhenFromDateIsAfterUntilDate() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();
        uut.getUrl(SYMBOL, UNTIL_DATE, FROM_DATE);
    }

    @Test
    public void shouldGenerateUrl() throws Exception {
        YahooQuoteUrlFormatter uut = new YahooQuoteUrlFormatter();

        String res = uut.getUrl(SYMBOL, FROM_DATE, UNTIL_DATE);

        assertThat(res, is(EXPECTED));
    }
}
