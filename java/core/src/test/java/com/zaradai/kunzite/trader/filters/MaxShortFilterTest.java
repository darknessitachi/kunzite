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
import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.control.TradingStateResolver;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.OrderBook;
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import com.zaradai.kunzite.trader.orders.OrderSide;
import com.zaradai.kunzite.trader.positions.PositionBook;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaxShortFilterTest {
    private static final String TEST_INST_ID = "test";
    private static final String TEST_PTF_ID = "ptf";
    private static final long TOTAL_NET = 1500;
    private static final long TOTAL_OUTSTANDING = 3500;
    private static final long TEST_REQUEST_QTY = 4500;

    private TradingStateResolver resolver;
    private MaxShortFilter uut;
    private ContextLogger logger;
    private FilterParameterManager parameterManager;
    private PositionBook positionBook;
    private OrderBook orderBook;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        resolver = mock(TradingStateResolver.class);
        TradingState state = mock(TradingState.class);
        positionBook = mock(PositionBook.class);
        orderBook = mock(OrderBook.class);
        when(state.getPositionBook()).thenReturn(positionBook);
        when(state.getOrderBook()).thenReturn(orderBook);
        when(resolver.resolveTradingState(TEST_INST_ID)).thenReturn(state);
        parameterManager = mock(FilterParameterManager.class);

        uut = new MaxShortFilter(logger, resolver, parameterManager);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(MaxShortFilter.FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidOrderRequest() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldReturnFalseIfPositionExceedsShortLimit() throws Exception {
        long invalidSize = TOTAL_NET - TOTAL_OUTSTANDING - TEST_REQUEST_QTY + 10;

        OrderRequest request = new OrderRequest();
        request.setSide(OrderSide.Sell);
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setQuantity(TEST_REQUEST_QTY);
        when(positionBook.getTotalNetPosition()).thenReturn(TOTAL_NET);
        when(orderBook.getOutstandingSellQuantity()).thenReturn(TOTAL_OUTSTANDING);
        when(parameterManager.getMaxShort(any(FilterRequest.class))).thenReturn(invalidSize);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.MaxShort));
    }

    @Test
    public void shouldReturnTrueIfPositionIsLessThanMaxShort() throws Exception {
        long validSize = TOTAL_NET - TOTAL_OUTSTANDING - TEST_REQUEST_QTY - 10;

        OrderRequest request = new OrderRequest();
        request.setSide(OrderSide.Sell);
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setQuantity(TEST_REQUEST_QTY);
        when(positionBook.getTotalNetPosition()).thenReturn(TOTAL_NET);
        when(orderBook.getOutstandingSellQuantity()).thenReturn(TOTAL_OUTSTANDING);
        when(parameterManager.getMaxShort(any(FilterRequest.class))).thenReturn(validSize);

        assertThat(uut.check(request), is(true));
    }
}
