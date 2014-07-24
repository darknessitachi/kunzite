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
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.control.TradingManager;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultTraderServiceTest {
    private static final Object TEST_EVENT = new Object();
    private ContextLogger logger;
    private EventAggregator eventAggregator;
    private DefaultTraderService uut;
    private TradingManager tradingManager;
    @Mock
    private BlockingQueue<Object> mockQueue;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        tradingManager = mock(TradingManager.class);
        uut = new DefaultTraderService(logger, eventAggregator, tradingManager);
    }

    @Test
    public void shouldHandleEvent() throws Exception {
        uut.handleEvent(TEST_EVENT);

        verify(eventAggregator).publish(TEST_EVENT);
    }

    @Test
    public void shouldHandleEventForTrader() throws Exception {
        uut = new DefaultTraderService(logger, eventAggregator, tradingManager) {
            @Override
            protected BlockingQueue<Object> createQueue() {
                return mockQueue;
            }
        };

        uut.onTraderEvent(TEST_EVENT);

        verify(mockQueue).put(TEST_EVENT);
    }

    @Test
    public void shouldGetServiceName() throws Exception {
        assertThat(uut.getName(), is(DefaultTraderService.SERVICE_NAME));
    }

    @Test
    public void shouldBuild() throws Exception {
        StaticConfiguration config = mock(StaticConfiguration.class);

        uut.build(config);

        verify(tradingManager).build(config);
    }

    @Test
    public void shouldInitializeOnStartUp() throws Exception {
        uut.startUp();

        verify(tradingManager).initialize();
    }
}
