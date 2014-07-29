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

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.joda.time.DateTime;

public class YahooQuoteUrlFormatter implements QuoteUrlFormatter {
    private static final String PREFIX_TAG = "http://real-chart.finance.yahoo.com/table.csv?";
    private static final String SYMBOL_TAG = "s=";
    private static final String FROM_YEAR_TAG = "&c=";
    private static final String FROM_MONTH_TAG = "&a=";
    private static final String FROM_DAY_TAG = "&b=";
    private static final String TO_YEAR_TAG = "&f=";
    private static final String TO_MONTH_TAG = "&d=";
    private static final String TO_DAY_TAG = "&e=";
    private static final String POSTFIX_TAG = "&ignore=.csv";

    @Override
    public String getUrl(String symbol, DateTime from, DateTime until) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(symbol));
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(until);
        Preconditions.checkArgument(from.isBefore(until));

        StringBuilder sb = new StringBuilder(PREFIX_TAG);
        sb.append(SYMBOL_TAG);
        sb.append(symbol);
        sb.append(FROM_MONTH_TAG);
        sb.append(from.monthOfYear().get()-1);
        sb.append(FROM_DAY_TAG);
        sb.append(from.dayOfMonth().get());
        sb.append(FROM_YEAR_TAG);
        sb.append(from.year().get());
        sb.append(TO_MONTH_TAG);
        sb.append(until.monthOfYear().get()-1);
        sb.append(TO_DAY_TAG);
        sb.append(until.dayOfMonth().get());
        sb.append(TO_YEAR_TAG);
        sb.append(until.year().get());
        sb.append(POSTFIX_TAG);

        return sb.toString();
    }
}
