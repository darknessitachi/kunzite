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
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.model.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;
import com.zaradai.kunzite.trader.positions.Portfolio;
import com.zaradai.kunzite.trader.positions.PortfolioResolver;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PortfolioFilterTest {
    private static final String TEST_PTF_ID = "ptf";
    private ContextLogger logger;
    private PortfolioFilter uut;
    private PortfolioResolver portfolioResolver;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        portfolioResolver = mock(PortfolioResolver.class);

        uut = new PortfolioFilter(logger, portfolioResolver);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(Constants.PORTFOLIO_FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidOrderRequest() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldReturnFalseIfPortfolioIdIsNull() throws Exception {
        OrderRequest request = new OrderRequest();

        uut.check(request);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.InvalidPortfolio));
    }

    @Test
    public void shouldReturnFalseIfPortfolioIsInvalid() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setPortfolioId(TEST_PTF_ID);

        uut.check(request);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.InvalidPortfolio));
    }

    @Test
    public void shouldReturnTrueIfPortfolioIsValid() throws Exception {
        Portfolio portfolio = mock(Portfolio.class);
        when(portfolioResolver.resolvePortfolio(TEST_PTF_ID)).thenReturn(portfolio);
        OrderRequest request = new OrderRequest();
        request.setPortfolioId(TEST_PTF_ID);

        uut.check(request);

        assertThat(uut.check(request), is(true));
    }
}
