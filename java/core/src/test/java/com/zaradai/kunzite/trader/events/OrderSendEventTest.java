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
package com.zaradai.kunzite.trader.events;

import com.zaradai.kunzite.trader.orders.model.NewOrder;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class OrderSendEventTest {
    @Test
    public void shouldAdd() throws Exception {
        NewOrder order = mock(NewOrder.class);
        OrderSendEvent uut = OrderSendEvent.newInstance();

        uut.add(order);

        assertThat(uut.hasOrders(), is(true));
        assertThat(uut.getOrders(), IsIterableWithSize.<NewOrder>iterableWithSize(1));
    }
}
