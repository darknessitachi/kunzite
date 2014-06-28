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
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaxQuantityFilterTest {
    private static final String TEST_INST_ID = "inst";
    private static final String TEST_PTF_ID = "ptf";
    private static final long TEST_REQUEST_QTY = 4000;

    private ContextLogger logger;
    private MaxQuantityFilter uut;
    private FilterParameterManager parameterManager;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        parameterManager = mock(FilterParameterManager.class);
        uut = new MaxQuantityFilter(logger, parameterManager);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(Constants.MAX_QUANTITY_FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidRequest() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldReturnFalseIfQuantityExceedsMaxQuantity() throws Exception {
        long invalidQuantity = TEST_REQUEST_QTY - 10;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setQuantity(TEST_REQUEST_QTY);
        when(parameterManager.getMaxQuantity(any(FilterRequest.class))).thenReturn(invalidQuantity);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.MaxQuantity));
    }

    @Test
    public void shouldReturnTrueIfNotionalIsLessThanMaxNotional() throws Exception {
        long validQuantity = TEST_REQUEST_QTY + 10;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setQuantity(TEST_REQUEST_QTY);
        when(parameterManager.getMaxQuantity(any(FilterRequest.class))).thenReturn(validQuantity);

        assertThat(uut.check(request), is(true));
    }
}
