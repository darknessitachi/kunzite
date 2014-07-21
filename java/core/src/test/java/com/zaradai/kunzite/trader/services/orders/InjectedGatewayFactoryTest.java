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
package com.zaradai.kunzite.trader.services.orders;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.zaradai.kunzite.trader.services.orders.gateway.EmulatorGateway;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InjectedGatewayFactoryTest {
    private static final Class<EmulatorGateway> TEST_CLASS = EmulatorGateway.class;
    private static final String TEST_NAME = TEST_CLASS.getName();
    private static final EmulatorGateway TEST_GATEWAY = mock(EmulatorGateway.class);
    private Injector injector;
    private InjectedGatewayFactory uut;

    @Before
    public void setUp() throws Exception {
        injector = mock(Injector.class);
        uut = new InjectedGatewayFactory(injector);
    }

    @Test
    public void shouldCreate() throws Exception {
        when(injector.getInstance(TEST_CLASS)).thenReturn(TEST_GATEWAY);

        OrderGateway res = uut.create(TEST_NAME);

        assertThat(res, is((OrderGateway) TEST_GATEWAY));
    }

    @Test(expected = GatewayException.class)
    public void shouldCatchConfigurationException() throws Exception {
        doThrow(ConfigurationException.class).when(injector).getInstance(TEST_CLASS);

        OrderGateway res = uut.create(TEST_NAME);
    }

    @Test(expected = GatewayException.class)
    public void shouldCatchProvisionException() throws Exception {
        doThrow(ProvisionException.class).when(injector).getInstance(TEST_CLASS);

        OrderGateway res = uut.create(TEST_NAME);
    }

    @Test(expected = GatewayException.class)
    public void shouldCatchClassNotFoundException() throws Exception {
        doThrow(ClassNotFoundException.class).when(injector).getInstance(TEST_CLASS);

        OrderGateway res = uut.create(TEST_NAME);
    }
}
