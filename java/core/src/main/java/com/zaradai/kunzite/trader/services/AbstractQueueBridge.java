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

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class AbstractQueueBridge extends AbstractExecutionThreadService implements Bridge {
    private static final long TIMEOUT = 1000;   // 1 Second

    private final BlockingQueue<Object> events;
    private final ContextLogger logger;

    public AbstractQueueBridge(ContextLogger logger) {
        this.logger = logger;
        events = createQueue();
    }

    protected BlockingQueue<Object> createQueue() {
        return Queues.newLinkedBlockingQueue();
    }

    protected ContextLogger getLogger() {
        return logger;
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            // get the next event from the queue
            Object event = events.poll(TIMEOUT, TimeUnit.MILLISECONDS);
            // null will be returned if timed out
            if (event != null) {
                // process the event within the service thread
                handleEvent(event);
            }
        }
    }

    @Override
    public void onEvent(Object event) {
        checkNotNull(event, "Invalid event to be added to queue");
        // add to the queue
        try {
            events.put(event);
        } catch (InterruptedException e) {
            LogHelper.debug(logger)
                    .addReason("Interrupted exception whilst pushing event")
                    .log();
        }
    }
}
