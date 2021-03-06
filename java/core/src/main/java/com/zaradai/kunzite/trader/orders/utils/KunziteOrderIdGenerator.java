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
package com.zaradai.kunzite.trader.orders.utils;

import org.joda.time.DateTime;

import static com.google.common.base.Preconditions.checkArgument;

public class KunziteOrderIdGenerator implements OrderIdGenerator {
    public static final long INITIAL_COUNTER = 1L;
    private long orderCounter;

    private KunziteOrderIdGenerator(long initialCounter) {
        orderCounter = initialCounter;
    }

    public static KunziteOrderIdGenerator newInstance() {
        return new KunziteOrderIdGenerator(INITIAL_COUNTER);
    }

    public static KunziteOrderIdGenerator newInstanceWithStartCounter(long start) {
        checkArgument(start > 0);

        return new KunziteOrderIdGenerator(start);
    }

    @Override
    public synchronized String generate() {
        return "Kunzite-" + DateTime.now().getMillis() + "-" + orderCounter++;
    }
}
