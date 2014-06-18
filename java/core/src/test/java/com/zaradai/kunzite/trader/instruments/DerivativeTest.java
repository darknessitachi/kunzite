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
package com.zaradai.kunzite.trader.instruments;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DerivativeTest {
    private static final String TEST_ID = "test";
    private static final DateTime TEST_DATE_TIME = DateTime.now();

    private Derivative uut;
    private InstrumentResolver instrumentResolver;

    @Before
    public void setUp() throws Exception {
        instrumentResolver = mock(InstrumentResolver.class);
        uut = new Derivative(instrumentResolver) {
            @Override
            public InstrumentType getType() {
                return InstrumentType.Option;
            }
        };
    }

    @Test
    public void shouldGetUnderlyingId() throws Exception {
        MembershipInstrument instrument = mock(MembershipInstrument.class);
        when(instrumentResolver.resolveInstrument(TEST_ID)).thenReturn(instrument);
        uut.setUnderlyingId(TEST_ID);

        assertThat(uut.getUnderlyingId(), is(TEST_ID));
        assertThat(uut.getUnderlying(), is(instrument));
    }

    @Test
    public void shouldGetMaturity() throws Exception {
        uut.setMaturity(TEST_DATE_TIME);

        assertThat(uut.getMaturity(), is(TEST_DATE_TIME));
    }
}
