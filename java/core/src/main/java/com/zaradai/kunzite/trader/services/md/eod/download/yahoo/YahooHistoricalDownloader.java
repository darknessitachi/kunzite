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

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.zaradai.kunzite.trader.services.md.eod.EodData;
import com.zaradai.kunzite.trader.services.md.eod.download.EodDataDownloader;
import com.zaradai.kunzite.trader.services.md.eod.download.EodDownloadException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class YahooHistoricalDownloader implements EodDataDownloader {
    private final QuoteUrlFormatter urlFormatter;
    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    YahooHistoricalDownloader(QuoteUrlFormatter urlFormatter) {
        this.urlFormatter = urlFormatter;
        dateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd");
    }

    @Override
    public List<EodData> download(String symbol, DateTime from, DateTime until) throws EodDownloadException {
        List<EodData> data = Lists.newArrayList();
        String encodedUrl = "";

        try {
            encodedUrl = urlFormatter.getUrl(symbol, from, until);
            URL url = new URL(encodedUrl);
            URLConnection connection = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            // remove the header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                data.add(parseLine(line, symbol));
            }
            reader.close();
        } catch (IOException e) {
            throw new EodDownloadException("Unable to download data for: " + symbol + " using URL: " + encodedUrl, e);
        }

        return data;
    }

    private EodData parseLine(String line, String symbol) {
        String[] entry = line.split(",");

        return EodData.builder()
                .symbol(symbol)
                .date(dateTimeFormatter.parseDateTime(entry[0]))
                .open(Double.parseDouble(entry[1]))
                .high(Double.parseDouble(entry[2]))
                .low(Double.parseDouble(entry[3]))
                .close(Double.parseDouble(entry[6]))
                .volumne(Long.parseLong(entry[5]))
                .build();
    }
}
