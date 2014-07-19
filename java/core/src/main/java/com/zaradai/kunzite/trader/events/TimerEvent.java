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
package com.zaradai.kunzite.trader.events;

import java.util.UUID;

public final class TimerEvent {
    private final UUID timerId;
    private final boolean last;

    private TimerEvent(UUID id, boolean last) {
        timerId = id;
        this.last = last;
    }

    public static TimerEvent newInstance(UUID id, boolean last) {
        return new TimerEvent(id, last);
    }

    public UUID getTimerId() {
        return timerId;
    }

    public boolean isLast() {
        return last;
    }
}
