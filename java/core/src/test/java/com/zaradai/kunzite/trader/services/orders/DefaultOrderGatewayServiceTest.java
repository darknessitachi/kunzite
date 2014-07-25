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

import com.google.common.collect.Maps;
import com.zaradai.kunzite.events.EventAggregator;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.orders.GatewayConfig;
import com.zaradai.kunzite.trader.config.orders.OrderGatewayConfiguration;
import com.zaradai.kunzite.trader.events.OrderSendEvent;
import com.zaradai.kunzite.trader.events.OrderStatus;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.model.NewOrder;
import com.zaradai.kunzite.trader.orders.model.OrderRefData;
import com.zaradai.kunzite.trader.services.trader.DefaultTraderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultOrderGatewayServiceTest {
    private static final String TEST_MARKET_ID = "test_id";
    private static final String TEST_GATEWAY_NAME = "gateway";

    private ContextLogger logger;
    private EventAggregator eventAggregator;
    private DefaultTraderService traderService;
    private OrderGatewayFactory orderGatewayFactory;
    private DefaultOrderGatewayService uut;
    @Mock
    private BlockingQueue<Object> mockQueue;
    @Mock
    private Map<String, OrderGateway> gatewayMap;
    @Captor
    ArgumentCaptor<OrderStatusEvent> statusEventArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        eventAggregator = mock(EventAggregator.class);
        traderService = mock(DefaultTraderService.class);
        orderGatewayFactory = mock(OrderGatewayFactory.class);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected BlockingQueue<Object> createQueue() {
                return mockQueue;
            }
        };
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(DefaultOrderGatewayService.SERVICE_NAME));
    }

    @Test
    public void shouldSubscribeOnConstruction() throws Exception {
        verify(eventAggregator).subscribe(uut);
    }

    @Test
    public void shouldProcessOrderStatusEvent() throws Exception {
        OrderStatusEvent orderStatusEvent = mock(OrderStatusEvent.class);

        uut.handleEvent(orderStatusEvent);

        verify(traderService).onTraderEvent(orderStatusEvent);
    }

    @Test
    public void shouldProcessOrderForGateway() throws Exception {
        NewOrder order = mock(NewOrder.class);
        OrderRefData refData = mock(OrderRefData.class);
        when(order.getRefData()).thenReturn(refData);
        when(refData.getMarketId()).thenReturn(TEST_MARKET_ID);
        OrderGateway gateway = mock(OrderGateway.class);
        when(gatewayMap.get(TEST_MARKET_ID)).thenReturn(gateway);

        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gatewayMap;
            }
        };
        OrderSendEvent sendEvent = OrderSendEvent.newInstance();
        sendEvent.add(order);

        uut.handleEvent(sendEvent);

        verify(gateway).processOrder(order);
    }

    @Test
    public void shouldProcessInvalidGatewayForOrder() throws Exception {
        NewOrder order = mock(NewOrder.class);
        OrderRefData refData = mock(OrderRefData.class);
        when(order.getRefData()).thenReturn(refData);
        when(refData.getMarketId()).thenReturn(TEST_MARKET_ID);
        when(gatewayMap.get(TEST_MARKET_ID)).thenReturn(null);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gatewayMap;
            }

            @Override
            protected BlockingQueue<Object> createQueue() {
                return mockQueue;
            }
        };
        OrderSendEvent sendEvent = OrderSendEvent.newInstance();
        sendEvent.add(order);

        uut.handleEvent(sendEvent);

        verify(mockQueue).put(statusEventArgumentCaptor.capture());
        assertThat(statusEventArgumentCaptor.getValue().getOrderStatus(), is(OrderStatus.NoExchange));
        verify(logger).error();
    }

    @Test
    public void shouldEnqueueOrderStatusFromGateway() throws Exception {
        OrderStatusEvent orderStatusEvent = mock(OrderStatusEvent.class);

        uut.onOrderStatus(orderStatusEvent);

        verify(mockQueue).put(orderStatusEvent);
    }

    @Test
    public void shouldEnqueueOrderSendFromTrader() throws Exception {
        OrderSendEvent orderSendEvent = mock(OrderSendEvent.class);

        uut.onOrderSend(orderSendEvent);

        verify(mockQueue).put(orderSendEvent);
    }

    @Test
    public void shouldStartUpGateways() throws Exception {
        final Map<String, OrderGateway> gateways = Maps.newHashMap();
        OrderGateway gateway = mock(OrderGateway.class);
        when(gateway.startAsync()).thenReturn(gateway);
        gateways.put(TEST_MARKET_ID, gateway);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gateways;
            }
        };

        uut.startUp();

        verify(gateway).startAsync();
        verify(gateway).awaitRunning();
        verify(logger).info();
    }

    @Test
    public void shouldShutdownGateways() throws Exception {
        final Map<String, OrderGateway> gateways = Maps.newHashMap();
        OrderGateway gateway = mock(OrderGateway.class);
        when(gateway.stopAsync()).thenReturn(gateway);
        gateways.put(TEST_MARKET_ID, gateway);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gateways;
            }
        };

        uut.shutDown();

        verify(gateway).stopAsync();
        verify(gateway).awaitTerminated();
        verify(logger).info();
    }

    @Test
    public void shouldCreateAndBuildConfiguredGateways() throws Exception {
        GatewayConfig config = new GatewayConfig();
        config.setClazz(TEST_GATEWAY_NAME);
        config.setMarket(TEST_MARKET_ID);
        OrderGatewayConfiguration ogc = new OrderGatewayConfiguration();
        ogc.add(config);
        OrderGateway gateway = mock(OrderGateway.class);
        when(orderGatewayFactory.create(TEST_GATEWAY_NAME)).thenReturn(gateway);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gatewayMap;
            }
        };

        uut.build(ogc);

        verify(gatewayMap).put(TEST_MARKET_ID, gateway);
    }

    @Test
    public void shouldLogIfFailToCreateGateway() throws Exception {
        GatewayConfig config = new GatewayConfig();
        config.setClazz(TEST_GATEWAY_NAME);
        config.setMarket(TEST_MARKET_ID);
        OrderGatewayConfiguration ogc = new OrderGatewayConfiguration();
        ogc.add(config);
        doThrow(GatewayException.class).when(orderGatewayFactory).create(TEST_GATEWAY_NAME);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gatewayMap;
            }
        };

        // catch the expected exception
        try {
            uut.build(ogc);
        } catch (ConfigException e) {

        }

        verify(logger).error();
    }

    @Test(expected = ConfigException.class)
    public void shouldThrowIfUnableToBuildGateway() throws Exception {
        GatewayConfig config = new GatewayConfig();
        config.setClazz(TEST_GATEWAY_NAME);
        config.setMarket(TEST_MARKET_ID);
        OrderGatewayConfiguration ogc = new OrderGatewayConfiguration();
        ogc.add(config);
        doThrow(GatewayException.class).when(orderGatewayFactory).create(TEST_GATEWAY_NAME);
        uut = new DefaultOrderGatewayService(logger, eventAggregator, traderService, orderGatewayFactory) {
            @Override
            protected Map<String, OrderGateway> createGatewayMap() {
                return gatewayMap;
            }
        };

        uut.build(ogc);
    }
}
