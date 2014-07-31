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
package com.zaradai.kunzite.trader.services.md.eod.mongo;

import com.zaradai.kunzite.trader.services.md.eod.EodData;
import com.zaradai.kunzite.trader.services.md.eod.EodIOException;
import com.zaradai.kunzite.trader.services.md.eod.EodReader;
import org.joda.time.DateTime;

public class MongoEodReader implements EodReader {
    @Override
    public void open(String symbol) throws EodIOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EodData getNext(DateTime date) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public EodData getNext() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
