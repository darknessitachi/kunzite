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
package com.zaradai.kunzite.trader.filters;

import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.instruments.Market;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.model.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TickFilterTest {
    private static final double TEST_PRICE = 42.50;
    private ContextLogger logger;
    private Market market;
    private TickFilter uut;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        market = mock(Market.class);
        uut = new TickFilter(logger, market);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(Constants.TICK_FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidOrderRequest() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldReturnFalseIfTickSizeInvalid() throws Exception {
        when(market.validTick(TEST_PRICE)).thenReturn(false);
        OrderRequest request = new OrderRequest();
        request.setPrice(TEST_PRICE);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.TICK_SIZE));
    }

    @Test
    public void shouldReturnTrueIfTickSizeValid() throws Exception {
        when(market.validTick(TEST_PRICE)).thenReturn(true);
        OrderRequest request = new OrderRequest();
        request.setPrice(TEST_PRICE);

        assertThat(uut.check(request), is(true));
    }
}
