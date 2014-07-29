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

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

public class AbstractQueueBridgeTest {
    private static final String TEST_NAME = "test";
    private static final Object TEST_EVENT = new Object();
    @Mock
    private BlockingQueue<Object> mockQueue;
    private ContextLogger logger;
    private AbstractQueueBridge uut;
    private MetricRegistry metricRegistry;
    private Meter meter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        metricRegistry = mock(MetricRegistry.class);
        meter = mock(Meter.class);
        when(metricRegistry.meter(TEST_NAME)).thenReturn(meter);
        uut = new AbstractQueueBridge(logger, metricRegistry) {
            @Override
            public void handleEvent(Object event) {

            }

            @Override
            public String getName() {
                return TEST_NAME;
            }

            @Override
            protected BlockingQueue<Object> createQueue() {
                return mockQueue;
            }
        };
    }

    @Test
    public void shouldPutEventOnQueue() throws Exception {
        uut.onEvent(TEST_EVENT);

        verify(mockQueue).put(TEST_EVENT);
    }

    @Test
    public void shouldHandleInterruptedOnQueuePut() throws Exception {
        doThrow(InterruptedException.class).when(mockQueue).put(TEST_EVENT);

        uut.onEvent(TEST_EVENT);

        verify(logger).debug();
    }

    @Test
    public void shouldHandleEvent() throws Exception {
        final CountDownLatch barrier = new CountDownLatch(1);
        uut = new AbstractQueueBridge(logger, metricRegistry) {
            @Override
            public void handleEvent(Object event) {
                barrier.countDown();
            }

            @Override
            public String getName() {
                return TEST_NAME;
            }
        };
        uut.startAsync().awaitRunning();
        uut.onEvent(TEST_EVENT);

        barrier.await();

        uut.stopAsync().awaitTerminated();

        verify(meter).mark();
    }
}
