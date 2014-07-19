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

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.events.TimerEvent;
import com.zaradai.kunzite.trader.events.TimerListener;
import org.joda.time.DateTime;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultTimerEngine implements TimerEngine {
    private final ContextLogger logger;
    private final EventAggregator eventAggregator;
    private final TimerService timerService;
    private final Map<UUID, TimerListener> subscriptions;

    @Inject
    DefaultTimerEngine(ContextLogger logger, EventAggregator eventAggregator, TimerService timerService) {
        this.logger = logger;
        this.eventAggregator = eventAggregator;
        this.timerService = timerService;

        eventAggregator.subscribe(this);
        subscriptions = createSubscriptionMap();
    }

    protected Map<UUID, TimerListener> createSubscriptionMap() {
        return Maps.newHashMap();
    }

    @Override
    public UUID subscribe(long duration, TimeUnit unit, boolean repeat, TimerListener listener) {
        checkArgument(duration > 0, "Invalid duration");
        checkNotNull(unit, "Invalid unit");
        checkNotNull(listener, "Invalid listener callback");
        // create a subscription
        UUID id = createSubscription(listener);
        // timeout = now + duration
        long nextTimeout = DateTime.now().getMillis() + unit.toMillis(duration);
        // create the request
        TimerRequest request = TimerRequest.newInstance(id, nextTimeout, duration, unit, repeat);
        // add the request to the timer service
        timerService.submit(request);
        // all setup return the id so that the subscriber can unsubscribe
        return id;
    }

    private UUID createSubscription(TimerListener listener) {
        UUID res = UUID.randomUUID();
        subscriptions.put(res, listener);

        return res;
    }

    @Override
    public void unsubscribe(UUID timerId) {
        // remove from subscriptions, note there may be existing timers
        // before the cancel is acknowledge by the service, in such cases a warninng message will be logged
        // and can safely be ignored.
        removeSubscription(timerId);
        // remove from the timer service if it still exists
        timerService.cancel(TimerCancelRequest.newInstance(timerId));
    }

    @Subscribe
    public void onTimer(TimerEvent event) {
        // get the target for this event
        TimerListener listener = subscriptions.get(event.getTimerId());

        if (listener != null) {
            listener.onTimer(event);
            // if this was the last event for the id then remove the subscriber from list
            if (event.isLast()) {
                removeSubscription(event.getTimerId());
            }
        } else {
            LogHelper.warn(logger)
                    .addContext("Timer Engine")
                    .addReason("Unhandled timer event")
                    .add("ID", event.getTimerId().toString())
                    .log();
        }
    }

    private void removeSubscription(UUID id) {
        subscriptions.remove(id);
    }
}
