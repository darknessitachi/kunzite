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
import com.zaradai.kunzite.trader.orders.OrderRejectReason;
import com.zaradai.kunzite.trader.orders.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LotSizeFilterTest {
    private static final String TEST_INST_ID = "inst";
    public static final int LOT_SIZE = 10;
    public static final int INVALID_LOT_SIZE_QUANTITY = 101;
    public static final int VALID_LOT_SIZE_QUANTITY = 100;
    private ContextLogger logger;
    private Instrument instrument;
    private LotSizeFilter uut;
    private InstrumentResolver resolver;

    @Before
    public void setUp() throws Exception {
        logger = ContextLoggerMocker.create();
        resolver = mock(InstrumentResolver.class);
        instrument = mock(Instrument.class);
        when(resolver.resolveInstrument(TEST_INST_ID)).thenReturn(instrument);
        uut = new LotSizeFilter(logger, resolver);
    }

    @Test
    public void shouldReturnFalseIfInvalidLotSize() throws Exception {
        when(instrument.getLotSize()).thenReturn(LOT_SIZE);
        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setQuantity(INVALID_LOT_SIZE_QUANTITY);

        assertThat(uut.check(request), is(false));
        assertThat(request.isValid(), is(false));
        assertThat(request.getRejectReason(), is(OrderRejectReason.LotSize));
    }

    @Test
    public void shouldReturnTrueIfValidLotSize() throws Exception {
        when(instrument.getLotSize()).thenReturn(LOT_SIZE);
        OrderRequest request = new OrderRequest();
        request.setInstrumentId(TEST_INST_ID);
        request.setQuantity(VALID_LOT_SIZE_QUANTITY);

        assertThat(uut.check(request), is(true));
        assertThat(request.isValid(), is(true));
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(Constants.LOT_SIZE_FILTER_NAME));
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
}
