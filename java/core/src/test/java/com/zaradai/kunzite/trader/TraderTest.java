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
package com.zaradai.kunzite.trader;

import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.TraderConfiguration;
import com.zaradai.kunzite.trader.config.md.MarketDataConfigLoader;
import com.zaradai.kunzite.trader.config.md.MarketDataConfiguration;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfigLoader;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfiguration;
import com.zaradai.kunzite.trader.config.statics.StaticConfiguration;
import com.zaradai.kunzite.trader.config.statics.StaticDataLoader;
import com.zaradai.kunzite.trader.services.md.MarketDataService;
import com.zaradai.kunzite.trader.services.orders.OrderGatewayService;
import com.zaradai.kunzite.trader.services.timer.TimerService;
import com.zaradai.kunzite.trader.services.trader.TraderService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TraderTest {
    private static final String STATIC_URI = "static_uri";
    private static final String MD_URI = "md_uri";
    private static final String ORDER_URI = "order_uri";

    private TraderConfiguration traderConfiguration;
    private StaticDataLoader staticDataLoader;
    private OrderGatewayConfigLoader orderGatewayConfigLoader;
    private MarketDataConfigLoader marketDataConfigLoader;
    private TraderService traderService;
    private OrderGatewayService orderGatewayService;
    private MarketDataService marketDataService;
    private TimerService timerService;
    private Trader uut;
    private StaticConfiguration staticConfiguration;
    private MarketDataConfiguration marketDataConfiguration;
    private OrderGatewayConfiguration orderGatewayConfiguration;

    @Before
    public void setUp() throws Exception {
        traderConfiguration = mock(TraderConfiguration.class);
        when(traderConfiguration.getStaticConfigUri()).thenReturn(STATIC_URI);
        when(traderConfiguration.getMarketDataConfigUri()).thenReturn(MD_URI);
        when(traderConfiguration.getOrderGatewayConfigUri()).thenReturn(ORDER_URI);
        staticConfiguration = mock(StaticConfiguration.class);
        staticDataLoader = mock(StaticDataLoader.class);
        when(staticDataLoader.load(STATIC_URI)).thenReturn(staticConfiguration);
        marketDataConfiguration = mock(MarketDataConfiguration.class);
        marketDataConfigLoader = mock(MarketDataConfigLoader.class);
        when(marketDataConfigLoader.load(MD_URI)).thenReturn(marketDataConfiguration);
        orderGatewayConfigLoader = mock(OrderGatewayConfigLoader.class);
        orderGatewayConfiguration = mock(OrderGatewayConfiguration.class);
        when(orderGatewayConfigLoader.load(ORDER_URI)).thenReturn(orderGatewayConfiguration);
        traderService = mock(TraderService.class);
        when(traderService.startAsync()).thenReturn(traderService);
        when(traderService.stopAsync()).thenReturn(traderService);
        orderGatewayService = mock(OrderGatewayService.class);
        when(orderGatewayService.startAsync()).thenReturn(orderGatewayService);
        when(orderGatewayService.stopAsync()).thenReturn(orderGatewayService);
        marketDataService = mock(MarketDataService.class);
        when(marketDataService.startAsync()).thenReturn(marketDataService);
        when(marketDataService.stopAsync()).thenReturn(marketDataService);
        timerService = mock(TimerService.class);
        when(timerService.startAsync()).thenReturn(timerService);
        when(timerService.stopAsync()).thenReturn(timerService);
        uut = new Trader(traderConfiguration, staticDataLoader, orderGatewayConfigLoader, marketDataConfigLoader,
                traderService, orderGatewayService, marketDataService, timerService);
    }

    @Test(expected = ConfigException.class)
    public void shouldFailIfUnableToLoadStaticConfig() throws Exception {
        doThrow(ConfigException.class).when(staticDataLoader).load(STATIC_URI);
        uut.startUp();
    }

    @Test(expected = ConfigException.class)
    public void shouldFailIfUnableToBuildTraderService() throws Exception {
        doThrow(ConfigException.class).when(traderService).build(staticConfiguration);
        uut.startUp();
    }

    @Test(expected = ConfigException.class)
    public void shouldFailIfUnableToLoadMarketDataConfig() throws Exception {
        doThrow(ConfigException.class).when(marketDataConfigLoader).load(MD_URI);
        uut.startUp();
    }

    @Test(expected = ConfigException.class)
    public void shouldFailIfUnableToBuildMarketDataService() throws Exception {
        doThrow(ConfigException.class).when(marketDataService).build(marketDataConfiguration);
        uut.startUp();
    }

    @Test(expected = ConfigException.class)
    public void shouldFailIfUnableToLoadOrderGatewayConfig() throws Exception {
        doThrow(ConfigException.class).when(orderGatewayConfigLoader).load(ORDER_URI);
        uut.startUp();
    }

    @Test(expected = ConfigException.class)
    public void shouldFailIfUnableToBuildOrderGatewayService() throws Exception {
        doThrow(ConfigException.class).when(orderGatewayService).build(orderGatewayConfiguration);
        uut.startUp();
    }

    @Test
    public void shouldBuildOnStartUp() throws Exception {
        uut.startUp();

        verify(staticDataLoader).load(STATIC_URI);
        verify(marketDataConfigLoader).load(MD_URI);
        verify(orderGatewayConfigLoader).load(ORDER_URI);
        verify(traderService).build(staticConfiguration);
        verify(marketDataService).build(marketDataConfiguration);
        verify(orderGatewayService).build(orderGatewayConfiguration);
    }

    @Test
    public void shouldStartServicesAndAwaitRunning() throws Exception {
        uut.startUp();

        verify(traderService).startAsync();
        verify(traderService).awaitRunning();
        verify(marketDataService).startAsync();
        verify(marketDataService).awaitRunning();
        verify(orderGatewayService).startAsync();
        verify(orderGatewayService).awaitRunning();
        verify(timerService).startAsync();
        verify(timerService).awaitRunning();
    }

    @Test
    public void shouldStopServicesAndAwaitTermination() throws Exception {
        uut.shutDown();

        verify(traderService).stopAsync();
        verify(traderService).awaitTerminated();
        verify(marketDataService).stopAsync();
        verify(marketDataService).awaitTerminated();
        verify(orderGatewayService).stopAsync();
        verify(orderGatewayService).awaitTerminated();
        verify(timerService).stopAsync();
        verify(timerService).awaitTerminated();
    }
}
