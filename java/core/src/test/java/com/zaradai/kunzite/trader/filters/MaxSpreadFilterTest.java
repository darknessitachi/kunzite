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
import com.zaradai.kunzite.trader.marketdata.MarketBook;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaxSpreadFilterTest {
    private static final String TEST_INST_ID = "test";
    private static final String TEST_PTF_ID = "ptf";
    private static final double LAST_TRADED = 45.56;

    private TradingStateResolver resolver;
    private MaxSpreadFilter uut;
    private ContextLogger logger;
    private FilterParameterManager parameterManager;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        resolver = mock(TradingStateResolver.class);
        TradingState state = mock(TradingState.class);
        MarketBook marketBook = mock(MarketBook.class);
        when(marketBook.getLastTradedPrice()).thenReturn(LAST_TRADED);
        when(state.getMarketBook()).thenReturn(marketBook);
        when(resolver.resolveTradingState(TEST_INST_ID)).thenReturn(state);
        parameterManager = mock(FilterParameterManager.class);

        uut = new MaxSpreadFilter(logger, resolver, parameterManager);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(Constants.MAX_SPREAD_FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidOrderRequest() throws Exception {
        uut.check(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidInstrument() throws Exception {
        when(resolver.resolveTradingState(TEST_INST_ID)).thenReturn(null);
        uut.check(null);
    }

    @Test
     public void shouldReturnFalseIfSpreadExceedsLimit() throws Exception {
        double testPrice = LAST_TRADED - 2.0;
        double invalidSpread = LAST_TRADED - testPrice - 0.5;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setPrice(testPrice);
        when(parameterManager.getMaxSpread(any(FilterRequest.class))).thenReturn(invalidSpread);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.MaxSpread));
    }

    @Test
    public void shouldReturnTrueIfSpreadWithinLimits() throws Exception {
        double testPrice = LAST_TRADED - 2.0;
        double validSpread = LAST_TRADED - testPrice + 0.5;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setPrice(testPrice);
        when(parameterManager.getMaxSpread(any(FilterRequest.class))).thenReturn(validSpread);

        assertThat(uut.check(request), is(true));
    }
}
