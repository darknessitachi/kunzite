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

import com.google.common.collect.Lists;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

public class EodDayData {
    private DateTime date;
    private final List<EodData> data;

    public EodDayData() {
        date = new DateTime(1800, 1, 1, 0, 0);
        data = Lists.newArrayList();
    }

    public EodDayData(DateTime date, Collection<EodData> values) {
        this.date = date;
        data = Lists.newArrayList(values);
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void add(EodData eodData) {
        data.add(eodData);
    }

    public List<EodData> getData() {
        return data;
    }

    public void clear() {
        data.clear();
    }
}
