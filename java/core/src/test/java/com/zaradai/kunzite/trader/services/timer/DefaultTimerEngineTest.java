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

import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.events.TimerEvent;
import com.zaradai.kunzite.trader.events.TimerListener;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultTimerEngineTest {
    private static final TimeUnit TEST_UNIT = TimeUnit.MILLISECONDS;
    private static final boolean TEST_REPEAT = true;
    private static final TimerListener TEST_LISTENER = mock(TimerListener.class);
    private static final long TEST_DURATION = 1000;
    private static final UUID TEST_ID = UUID.randomUUID();

    private ContextLogger logger;
    private EventAggregator eventAggregator;
    private TimerService timerService;
    private DefaultTimerEngine uut;
    @Mock
    private Map<UUID, TimerListener> mockMap;
    @Captor
    ArgumentCaptor<TimerRequest> requestArgumentCaptor;
    @Captor
    ArgumentCaptor<TimerCancelRequest> requestCancelArgumentCaptor;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        timerService = mock(TimerService.class);
        uut = new DefaultTimerEngine(logger, eventAggregator, timerService) {
            @Override
            protected Map<UUID, TimerListener> createSubscriptionMap() {
                return mockMap;
            }
        };
    }

    @Test
    public void shouldSubscribeToEventAggregatorOnConstruction() throws Exception {
        verify(eventAggregator).subscribe(uut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailToSubscribeWithInvalidDuration() throws Exception {
        uut.subscribe(0, TEST_UNIT, TEST_REPEAT, TEST_LISTENER);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToSubscribeWithInvalidUnit() throws Exception {
        uut.subscribe(TEST_DURATION, null, TEST_REPEAT, TEST_LISTENER);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailToSubscribeWithInvalidSubscriber() throws Exception {
        uut.subscribe(TEST_DURATION, TEST_UNIT, TEST_REPEAT, null);
    }

    @Test
    public void shouldSubscribe() throws Exception {
        UUID res = uut.subscribe(TEST_DURATION, TEST_UNIT, TEST_REPEAT, TEST_LISTENER);

        assertThat(res, not(nullValue()));
        verify(mockMap).put(res, TEST_LISTENER);
        verify(timerService).submit(requestArgumentCaptor.capture());

        assertThat(requestArgumentCaptor.getValue().isRepeat(), is(TEST_REPEAT));
        assertThat(requestArgumentCaptor.getValue().getUnit(), is(TEST_UNIT));
        assertThat(requestArgumentCaptor.getValue().getDuration(), is(TEST_DURATION));
        assertThat(requestArgumentCaptor.getValue().getId(), is(res));
    }

    @Test
    public void shouldUnsubscribe() throws Exception {
        uut.unsubscribe(TEST_ID);

        verify(mockMap).remove(TEST_ID);
        verify(timerService).cancel(requestCancelArgumentCaptor.capture());

        assertThat(requestCancelArgumentCaptor.getValue().getId(), is(TEST_ID));
    }

    @Test
    public void shouldUpdateListenerOnTimeout() throws Exception {
        TimerEvent event = TimerEvent.newInstance(TEST_ID, false);
        when(mockMap.get(TEST_ID)).thenReturn(TEST_LISTENER);

        uut.onTimer(event);

        verify(TEST_LISTENER).onTimer(event);
    }

    @Test
    public void shouldUpdateOnTimeoutAndRemoveIfLast() throws Exception {
        TimerEvent event = TimerEvent.newInstance(TEST_ID, true);
        when(mockMap.get(TEST_ID)).thenReturn(TEST_LISTENER);

        uut.onTimer(event);

        verify(TEST_LISTENER).onTimer(event);
        verify(mockMap).remove(TEST_ID);
    }

    @Test
    public void shouldLogIfNoSubscriberToUpdate() throws Exception {
        TimerEvent event = TimerEvent.newInstance(TEST_ID, true);

        uut.onTimer(event);

        verify(logger).warn();
    }
}

