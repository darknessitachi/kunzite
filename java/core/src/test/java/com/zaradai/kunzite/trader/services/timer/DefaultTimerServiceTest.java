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

import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.events.TimerEvent;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.services.trader.DefaultTraderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultTimerServiceTest {
    private DefaultTraderService traderService;
    private TimeBase timeBase;
    private DefaultTimerService uut;
    @Captor
    ArgumentCaptor<TimerEvent> timerEventArgumentCaptor;
    private ContextLogger logger;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        traderService = mock(DefaultTraderService.class);
        timeBase = mock(TimeBase.class);
        uut = new DefaultTimerService(logger, traderService, timeBase);
    }

    @Test
    public void shouldProcessExpiredTimeouts() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        long timeout1 = 5000;
        long timeout2 = 4500;   // oldest
        long timeout3 = 5500;   // latest

        long target = 5000;     // include 1&2

        TimerRequest request1 = TimerRequest.newInstance(id1, timeout1, 1000, TimeUnit.MILLISECONDS, true);
        TimerRequest request2 = TimerRequest.newInstance(id2, timeout2, 1000, TimeUnit.MILLISECONDS, true);
        TimerRequest request3 = TimerRequest.newInstance(id3, timeout3, 1000, TimeUnit.MILLISECONDS, true);

        uut.submit(request1);
        uut.submit(request2);
        uut.submit(request3);

        when(timeBase.now()).thenReturn(target);

        uut.process();
        // timeouts id2 and id1 should have been fired in that order
        verify(traderService, times(2)).onEvent(timerEventArgumentCaptor.capture());

        assertThat(timerEventArgumentCaptor.getAllValues().get(0).getTimerId(), is(id2));
        assertThat(timerEventArgumentCaptor.getAllValues().get(1).getTimerId(), is(id1));
    }

    @Test
    public void shouldCancelRegisteredRequest() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        long timeout1 = 5000;
        long timeout2 = 4500;   // oldest
        long timeout3 = 5500;   // latest
        long target = 5000;     // include 1&2
        TimerRequest request1 = TimerRequest.newInstance(id1, timeout1, 1000, TimeUnit.MILLISECONDS, true);
        TimerRequest request2 = TimerRequest.newInstance(id2, timeout2, 1000, TimeUnit.MILLISECONDS, true);
        TimerRequest request3 = TimerRequest.newInstance(id3, timeout3, 1000, TimeUnit.MILLISECONDS, true);
        uut.submit(request1);
        uut.submit(request2);
        uut.submit(request3);
        when(timeBase.now()).thenReturn(target);
        // remove id1
        uut.cancel(TimerCancelRequest.newInstance(id1));
        // run the process
        uut.process();
        // Only timeout id2 should have been fired.
        verify(traderService).onEvent(timerEventArgumentCaptor.capture());

        assertThat(timerEventArgumentCaptor.getValue().getTimerId(), is(id2));
    }
}
