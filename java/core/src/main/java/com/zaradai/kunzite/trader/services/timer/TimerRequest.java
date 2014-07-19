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
package com.zaradai.kunzite.trader.services.timer;

import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TimerRequest {
    private final UUID id;
    private final long timeout;
    private final long duration;
    private final TimeUnit unit;
    private final boolean repeat;

    private TimerRequest(UUID id, long timeout, long duration, TimeUnit unit, boolean repeat) {
        this.id = id;
        this.timeout = timeout;
        this.duration = duration;
        this.unit = unit;
        this.repeat = repeat;
    }

    public static TimerRequest newInstance(UUID id, long timeout, long duration, TimeUnit unit, boolean repeat) {
        return new TimerRequest(id, timeout, duration, unit, repeat);
    }

    public UUID getId() {
        return id;
    }

    public long getTimeout() {
        return timeout;
    }

    public long getDuration() {
        return duration;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public boolean isRepeat() {
        return repeat;
    }

    /**
     * If this is a repeatable timer request, this method will calculate the next timeout and return
     * a fully constructed instance otherwise return null
     * @return
     */
    public TimerRequest getNextRequest() {
        TimerRequest res = null;

        if (repeat) {
            res = newInstance(id, timeout + unit.toMillis(duration), duration, unit, repeat);
        }

        return res;
    }

    // Add an oldest first comparator
    public static final Comparator<TimerRequest> OLDEST_FIRST = new Comparator<TimerRequest>() {
        public int compare(TimerRequest o1, TimerRequest o2) {
            return Long.compare(o2.timeout, o1.timeout);
        }
    };
}
