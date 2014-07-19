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
package com.zaradai.kunzite.trader.services.trader;

import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TraderServiceTest {
    private static final Object TEST_EVENT = new Object();
    private ContextLogger logger;
    private EventAggregator eventAggregator;
    private TraderService uut;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        uut = new TraderService(logger, eventAggregator);
    }

    @Test
    public void shouldHandleEvent() throws Exception {
        uut.handleEvent(TEST_EVENT);

        verify(eventAggregator).publish(TEST_EVENT);
    }

    @Test
    public void shouldGetServiceName() throws Exception {
        assertThat(uut.getName(), is(TraderService.SERVICE_NAME));
    }
}
