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

import com.zaradai.kunzite.trader.algo.AlgoState;

public class AlgoStatusUpdateEvent {
    private final AlgoState state;
    private final String id;
    private final String reason;

    public AlgoStatusUpdateEvent(AlgoState state, String id, String reason) {

        this.state = state;
        this.id = id;
        this.reason = reason;
    }

    public AlgoState getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public String getReason() {
        return reason;
    }
}
