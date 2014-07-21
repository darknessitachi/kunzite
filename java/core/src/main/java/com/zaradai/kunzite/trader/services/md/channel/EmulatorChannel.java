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
package com.zaradai.kunzite.trader.services.md.channel;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.zaradai.kunzite.trader.services.md.MarketDataChannel;

public class EmulatorChannel extends AbstractExecutionThreadService implements MarketDataChannel {
    @Override
    protected void run() throws Exception {
        while (isRunning()) {

        }
    }

    @Override
    public void subscribe(String sid) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsubscribe(String sid) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unsubscribeAll() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
