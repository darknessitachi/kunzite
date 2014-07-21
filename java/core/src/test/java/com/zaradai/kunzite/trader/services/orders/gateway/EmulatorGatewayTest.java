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
package com.zaradai.kunzite.trader.services.orders.gateway;

import com.zaradai.kunzite.trader.events.OrderStatus;
import com.zaradai.kunzite.trader.events.OrderStatusEvent;
import com.zaradai.kunzite.trader.orders.model.NewOrder;
import com.zaradai.kunzite.trader.orders.model.OrderRefData;
import com.zaradai.kunzite.trader.orders.model.OrderRequestType;
import com.zaradai.kunzite.trader.services.orders.OrderGatewayService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmulatorGatewayTest {
    private static final String TEST_ALGO_ID = "algoId";
    private static final String TEST_ORDER_ID = "orderId";
    private static final double TEST_PRICE = 42.42;
    private static final long TEST_QUANTITY = 2500;

    private OrderGatewayService orderGatewayService;
    private EmulatorGateway uut;
    @Captor
    ArgumentCaptor<OrderStatusEvent> orderStatusEventArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        orderGatewayService = mock(OrderGatewayService.class);
        uut = new EmulatorGateway(orderGatewayService);
    }

    @Test
    public void shouldFillCreateOrder() throws Exception {
        NewOrder order = new NewOrder();
        order.setRefData(OrderRefData.builder().id(TEST_ORDER_ID).algo(TEST_ALGO_ID).build());
        order.setRequestType(OrderRequestType.Create);
        order.setPrice(TEST_PRICE);
        order.setQuantity(TEST_QUANTITY);

        uut.processOrder(order);

        verify(orderGatewayService, times(2)).onOrderStatus(orderStatusEventArgumentCaptor.capture());
        assertThat(orderStatusEventArgumentCaptor.getAllValues().get(0).getOrderStatus(), is(OrderStatus.New));
        assertThat(orderStatusEventArgumentCaptor.getAllValues().get(1).getOrderStatus(), is(OrderStatus.Filled));
    }

    @Test
    public void shouldFillAmendedOrder() throws Exception {
        NewOrder order = new NewOrder();
        order.setRefData(OrderRefData.builder().id(TEST_ORDER_ID).algo(TEST_ALGO_ID).build());
        order.setRequestType(OrderRequestType.Amend);
        order.setPrice(TEST_PRICE);
        order.setQuantity(TEST_QUANTITY);

        uut.processOrder(order);

        verify(orderGatewayService, times(2)).onOrderStatus(orderStatusEventArgumentCaptor.capture());
        assertThat(orderStatusEventArgumentCaptor.getAllValues().get(0).getOrderStatus(), is(OrderStatus.New));
        assertThat(orderStatusEventArgumentCaptor.getAllValues().get(1).getOrderStatus(), is(OrderStatus.Filled));
    }

    @Test
    public void shouldCancelOrder() throws Exception {
        NewOrder order = new NewOrder();
        order.setRefData(OrderRefData.builder().id(TEST_ORDER_ID).algo(TEST_ALGO_ID).build());
        order.setRequestType(OrderRequestType.Cancel);

        uut.processOrder(order);

        verify(orderGatewayService, times(2)).onOrderStatus(orderStatusEventArgumentCaptor.capture());
        assertThat(orderStatusEventArgumentCaptor.getAllValues().get(0).getOrderStatus(), is(OrderStatus.New));
        assertThat(orderStatusEventArgumentCaptor.getAllValues().get(1).getOrderStatus(), is(OrderStatus.Cancelled));
    }

    @Test
    public void shouldGetGatewayName() throws Exception {
        assertThat(uut.getName(), is(EmulatorGateway.GATEWAY_NAME));
    }
}
