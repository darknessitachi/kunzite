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
package com.zaradai.kunzite.trader.algo;

import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultAlgoStateControllerTest {
    private ContextLogger logger;
    private EventAggregator eventAggregator;
    private DefaultAlgoStateController uut;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        uut = new DefaultAlgoStateController(logger, eventAggregator);
    }

    @Test
    public void shouldControlUncontrolledAlgo() throws Exception {
        Algo toControl = mock(Algo.class);

        uut.control(toControl);

        assertThat(uut.getState(), is(AlgoState.Off));
        assertThat(uut.getControlled(), is(toControl));
    }

    @Test
    public void shouldNotControlIfAlreadyControllingAnother() throws Exception {
        Algo controlled = mock(Algo.class);
        Algo toControl = mock(Algo.class);
        uut.control(controlled);

        uut.control(toControl);

        assertThat(uut.getControlled(), not(toControl));
        assertThat(uut.getControlled(), is(controlled));
    }

    @Test
    public void shouldNotStartIfNotControlled() throws Exception {
        uut.start();

        verify(logger).error();
    }

    @Test
    public void shouldStartIfNoErrors() throws Exception {
        Algo controlled = mock(Algo.class);
        uut.control(controlled);

        uut.start();

        verify(controlled).start();
        assertThat(uut.getState(), is(AlgoState.On));
        verify(logger, times(4)).info();    //1. initial, 2. request to start, 3. starting, 4. on
        verify(eventAggregator, times(3)).publish(any()); // 1. Initial, Starting, 2. On
    }

    @Test
    public void shouldSuspendIfFailToStart() throws Exception {
        Algo controlled = mock(Algo.class);
        doThrow(new AlgoException("Error")).when(controlled).start();
        uut.control(controlled);

        uut.start();

        assertThat(uut.getState(), is(AlgoState.Suspended));
        verify(logger).error();
    }

    @Test
    public void shouldLogWrongStateIfStartingAStartedAlgo() throws Exception {
        Algo controlled = mock(Algo.class);
        uut.control(controlled);
        uut.start();

        uut.start();

        verify(logger).warn();
    }

    @Test
    public void shouldNotStopIfNotControlled() throws Exception {
        uut.stop();

        verify(logger).error();
    }

    @Test
    public void shouldStopARunningAlgoIfNoErrors() throws Exception {
        Algo controlled = mock(Algo.class);
        uut.control(controlled);
        uut.start();

        uut.stop();

        verify(controlled).stop();
        assertThat(uut.getState(), is(AlgoState.Off));
        verify(logger, times(7)).info();    //4 for starting in setup and 3 for actual stop request
        verify(eventAggregator, times(5)).publish(any()); // 3 for start and 2 for stop
    }

    @Test
    public void shouldLogWrongStateIfStopingAStoppedAlgo() throws Exception {
        Algo controlled = mock(Algo.class);
        uut.control(controlled);

        uut.stop();

        verify(logger).warn();
    }

    @Test
    public void shouldLogErrorIfFailToStop() throws Exception {
        Algo controlled = mock(Algo.class);
        doThrow(AlgoException.class).when(controlled).stop();
        uut.control(controlled);
        uut.start();

        uut.stop();
        // error should not prevent final state being stopped
        assertThat(uut.getState(), is(AlgoState.Off));
        verify(logger).error();
    }

    @Test
    public void shouldSuspendIfRunning() throws Exception {
        Algo controlled = mock(Algo.class);
        uut.control(controlled);
        uut.start();

        uut.suspend("");
    }

    @Test
    public void shouldWarnIfSuspendingStoppedAlgo() throws Exception {
        Algo controlled = mock(Algo.class);
        uut.control(controlled);
        uut.suspend("");

        verify(logger).warn();
    }
}
