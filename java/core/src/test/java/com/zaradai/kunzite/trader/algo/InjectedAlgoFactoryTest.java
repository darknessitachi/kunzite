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

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class InjectedAlgoFactoryTest {
    private static Class<Algo> ALGO_CLASS = Algo.class;
    private static final String TEST_NAME = ALGO_CLASS.getName();
    private static final Algo TEST_ALGO = mock(ALGO_CLASS);

    private Injector injector;
    private InjectedAlgoFactory uut;

    @Before
    public void setUp() throws Exception {
        injector = mock(Injector.class);
        uut = new InjectedAlgoFactory(injector);
    }

    @Test
    public void shouldCreate() throws Exception {
        when(injector.getInstance(ALGO_CLASS)).thenReturn(TEST_ALGO);
        Algo res = uut.create(TEST_NAME);

        assertThat(res, is(TEST_ALGO));
    }

    @Test(expected = AlgoException.class)
    public void shouldCaptureConfigurationException() throws Exception {
        doThrow(ConfigurationException.class).when(injector).getInstance(ALGO_CLASS);

        uut.create(TEST_NAME);
    }

    @Test(expected = AlgoException.class)
    public void shouldCaptureProvisionException() throws Exception {
        doThrow(ProvisionException.class).when(injector).getInstance(ALGO_CLASS);

        uut.create(TEST_NAME);
    }

    @Test(expected = AlgoException.class)
    public void shouldCaptureClassNotFoundException() throws Exception {
        doThrow(ClassNotFoundException.class).when(injector).getInstance(ALGO_CLASS);

        uut.create(TEST_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailIfCreateWithInvalidName() throws Exception {
        uut.create(null);
    }
}
