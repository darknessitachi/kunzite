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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.google.inject.Inject;
import com.zaradai.kunzite.trader.events.TimerEvent;
import com.zaradai.kunzite.trader.services.trader.TraderService;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DefaultTimerService extends AbstractExecutionThreadService implements TimerService {
    private static final long TIMER_QUANTUM = 500; // smallest unit in millis to check for timeouts.
    private static final int INITIAL_CAPACITY = 1000;   // Big fat guess

    private final PriorityBlockingQueue<TimerRequest> requestQueue;
    private final TraderService traderService;
    private final TimeBase timeBase;

    @Inject
    DefaultTimerService(TraderService traderService, TimeBase timeBase) {
        this.traderService = traderService;
        this.timeBase = timeBase;
        requestQueue = new PriorityBlockingQueue<TimerRequest>(INITIAL_CAPACITY, TimerRequest.OLDEST_FIRST);
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            process();
        }
    }

    @VisibleForTesting
    void process() {
        timeBase.sleep(TIMER_QUANTUM, TimeUnit.MILLISECONDS);
        checkForTimeouts();
    }

    private void checkForTimeouts() {
        long now = timeBase.now();
        TimerRequest request;

        while ((request = getNextTimeout(now)) != null) {
            processTimeout(request);
        }
    }

    private void processTimeout(TimerRequest request) {
        boolean repeat = request.isRepeat();
        // fire an event on the trader service
        traderService.onEvent(TimerEvent.newInstance(request.getId(), !repeat));
        // if repeating push another request on the queue
        if (repeat) {
            submit(request.getNextRequest());
        }
    }

    private TimerRequest getNextTimeout(long test) {
        TimerRequest res = requestQueue.peek();

        if (res != null && res.getTimeout() <= test) {
            return requestQueue.poll();
        }

        return null;
    }

    @Override
    public void submit(TimerRequest request) {
        requestQueue.put(request);
    }

    @Override
    public void cancel(TimerCancelRequest request) {
        Iterator<TimerRequest> iterator = requestQueue.iterator();
        while (iterator.hasNext()) {
            TimerRequest next = iterator.next();

            if (next.getId().equals(request.getId())) {
                iterator.remove();
                return;
            }
        }
    }
}
