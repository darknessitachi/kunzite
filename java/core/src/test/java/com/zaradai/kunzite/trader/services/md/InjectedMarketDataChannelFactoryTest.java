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
package com.zaradai.kunzite.trader.services.md;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import com.zaradai.kunzite.trader.services.md.channel.EmulatorChannel;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class InjectedMarketDataChannelFactoryTest {
    private static final Class<EmulatorChannel> TEST_CLASS = EmulatorChannel.class;
    private static final String TEST_NAME = TEST_CLASS.getName();
    private static final EmulatorChannel TEST_CHANNEL = mock(EmulatorChannel.class);

    private Injector injector;
    private InjectedMarketDataChannelFactory uut;

    @Before
    public void setUp() throws Exception {
        injector = mock(Injector.class);
        uut = new InjectedMarketDataChannelFactory(injector);
    }

    @Test
    public void shouldCreate() throws Exception {
        when(injector.getInstance(TEST_CLASS)).thenReturn(TEST_CHANNEL);

        MarketDataChannel res = uut.create(TEST_NAME);

        assertThat(res, is((MarketDataChannel) TEST_CHANNEL));
    }

    @Test(expected = MarketDataException.class)
    public void shouldCatchConfigurationException() throws Exception {
        doThrow(ConfigurationException.class).when(injector).getInstance(TEST_CLASS);

        MarketDataChannel res = uut.create(TEST_NAME);
    }

    @Test(expected = MarketDataException.class)
    public void shouldCatchProvisionException() throws Exception {
        doThrow(ProvisionException.class).when(injector).getInstance(TEST_CLASS);

        MarketDataChannel res = uut.create(TEST_NAME);
    }

    @Test(expected = MarketDataException.class)
    public void shouldCatchClassNotFoundException() throws Exception {
        doThrow(ClassNotFoundException.class).when(injector).getInstance(TEST_CLASS);

        MarketDataChannel res = uut.create(TEST_NAME);
    }
}
