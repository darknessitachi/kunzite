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
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.instruments.InstrumentResolver;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaxNotionalFilterTest {
    private static final String TEST_INST_ID = "inst";
    private static final String TEST_PTF_ID = "ptf";
    private static final long TEST_REQUEST_QTY = 4000;
    private static final double TEST_MULTIPLIER = 10.0;
    private static final double TEST_PRICE = 24.5;

    private ContextLogger logger;
    private Instrument instrument;
    private MaxNotionalFilter uut;
    private InstrumentResolver resolver;
    private FilterParameterManager parameterManager;


    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        resolver = mock(InstrumentResolver.class);
        instrument = mock(Instrument.class);
        when(instrument.getMultiplier()).thenReturn(TEST_MULTIPLIER);
        when(resolver.resolveInstrument(TEST_INST_ID)).thenReturn(instrument);
        parameterManager = mock(FilterParameterManager.class);

        uut = new MaxNotionalFilter(logger, resolver, parameterManager);
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(MaxNotionalFilter.FILTER_NAME));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidRequest() throws Exception {
        uut.check(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidInstrument() throws Exception {
        when(resolver.resolveInstrument(TEST_INST_ID)).thenReturn(null);
        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);

        uut.check(request);
    }

    @Test
    public void shouldReturnFalseIfNotionalExceeds() throws Exception {
        double invalidNotional = TEST_REQUEST_QTY * TEST_PRICE * TEST_MULTIPLIER - 10.0;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setQuantity(TEST_REQUEST_QTY);
        request.setPrice(TEST_PRICE);
        when(parameterManager.getMaxNotional(any(FilterRequest.class))).thenReturn(invalidNotional);

        assertThat(uut.check(request), is(false));
    }

    @Test
    public void shouldReturnTrueIfNotionalIsLessThanMaxNotional() throws Exception {
        double validNotional = TEST_REQUEST_QTY * TEST_PRICE * TEST_MULTIPLIER + 10;

        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setPortfolioId(TEST_PTF_ID);
        request.setQuantity(TEST_REQUEST_QTY);
        request.setPrice(TEST_PRICE);
        when(parameterManager.getMaxNotional(any(FilterRequest.class))).thenReturn(validNotional);

        assertThat(uut.check(request), is(true));
    }
}
