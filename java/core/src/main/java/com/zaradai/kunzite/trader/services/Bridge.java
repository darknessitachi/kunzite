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
package com.zaradai.kunzite.trader.services;

import com.google.common.util.concurrent.Service;

/**
 * Provides a multi-threaded bridge from one domain to another. E.g, trader is single threaded and processes events
 * one by one.  The TraderService will implement the bridge to allow events from other services to be dispatched
 * safely within the trader.
 */
public interface Bridge extends Service {
    /**
     * Push an event to the bridge consumer
     * @param event
     */
    void onEvent(Object event);

    /**
     * The bridge consumer will handle this event.
     * @param event
     */
    void handleEvent(Object event);

    /**
     * Returns the name of the bridge
     * @return
     */
    String getName();
}
