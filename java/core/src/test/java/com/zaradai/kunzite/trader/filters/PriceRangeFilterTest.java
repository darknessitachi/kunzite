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
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceRangeFilterTest {
    private static final String TEST_INST_ID = "test";
    private static final String TEST_PTF_ID = "ptf";
    private static final double MIN_PRICE = 5.0;
    private static final double MAX_PRICE = 15.0;

    private PriceRangeFilter uut;
    private ContextLogger logger;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        FilterParameterManager parameterManager = mock(FilterParameterManager.class);
        when(parameterManager.getMinPrice(any(FilterRequest.class))).thenReturn(MIN_PRICE);
        when(parameterManager.getMaxPrice(any(FilterRequest.class))).thenReturn(MAX_PRICE);

        uut = new PriceRangeFilter(logger, parameterManager);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(PriceRangeFilter.FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidOrderRequest() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldReturnFalseIfPriceBelowMinRange() throws Exception {
        double invalidPrice = MIN_PRICE - 0.5;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setPrice(invalidPrice);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.PriceRange));
    }

    @Test
    public void shouldReturnFalseIfPriceAboveMaxRange() throws Exception {
        double invalidPrice = MAX_PRICE + 0.5;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setPrice(invalidPrice);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.PriceRange));
    }

    @Test
    public void shouldReturnTrueIfPriceWithinPriceRangeLimits() throws Exception {
        double validPrice = MAX_PRICE - ((MAX_PRICE - MIN_PRICE)/2.0);

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setPrice(validPrice);

        assertThat(uut.check(request), is(true));
    }

}
