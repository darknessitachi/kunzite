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

import com.google.common.collect.Sets;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestrictedListFilterTest {
    private static final String TEST_INST_ID = "test";
    private static final String TEST_PTF_ID = "ptf";
    private static final String RESTRICTED_INST_ID = "restricted";


    private ContextLogger logger;
    private FilterParameterManager parameterManager;
    private RestrictedListFilter uut;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        Set<String> restricted = Sets.newHashSet(RESTRICTED_INST_ID);
        parameterManager = mock(FilterParameterManager.class);
        when(parameterManager.getRestrictedList(any(FilterRequest.class))).thenReturn(restricted);

        uut = new RestrictedListFilter(logger, parameterManager);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(RestrictedListFilter.FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidOrderRequest() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldReturnFalseIfOrderWithRestrictedInstrument() throws Exception {

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(RESTRICTED_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);

        assertThat(uut.check(request), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.RestrictedList));
    }

    @Test
    public void shouldReturnTrueIfOrderInstrumentNotRestricted() throws Exception {

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);

        assertThat(uut.check(request), is(true));
    }
}
