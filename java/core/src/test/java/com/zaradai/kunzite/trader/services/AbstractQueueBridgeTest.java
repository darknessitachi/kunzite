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

import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class AbstractQueueBridgeTest {
    private static final String TEST_NAME = "test";
    private static final Object TEST_EVENT = new Object();
    @Mock
    private BlockingQueue<Object> mockQueue;
    private ContextLogger logger;
    private AbstractQueueBridge uut;
    private boolean handleEventCalled;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        handleEventCalled = false;

        logger = ContextLoggerMocker.create();
        uut = createAbstractQueueBridge(true);
    }

    private AbstractQueueBridge createAbstractQueueBridge(boolean mockedQueue) {
        return (mockedQueue) ?
                new AbstractQueueBridge(logger) {
                    @Override
                    public void handleEvent(Object event) {
                        handleEventCalled = true;
                    }

                    @Override
                    public String getName() {
                        return TEST_NAME;
                    }

                    @Override
                    protected BlockingQueue<Object> createQueue() {
                        return mockQueue;
                    }
                } :
                new AbstractQueueBridge(logger) {
                    @Override
                    public void handleEvent(Object event) {
                        handleEventCalled = true;
                    }

                    @Override
                    public String getName() {
                        return TEST_NAME;
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
        uut = createAbstractQueueBridge(false);
        uut.onEvent(TEST_EVENT);
        uut.startAsync().awaitRunning();
        TimeUnit.MILLISECONDS.sleep(500);

        uut.stopAsync().awaitTerminated();

        assertThat(handleEventCalled, is(true));
    }
}
