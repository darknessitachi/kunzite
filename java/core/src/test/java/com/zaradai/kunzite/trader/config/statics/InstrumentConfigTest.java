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
package com.zaradai.kunzite.trader.config.statics;

import com.zaradai.kunzite.trader.instruments.InstrumentType;
import com.zaradai.kunzite.trader.instruments.OptionType;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class InstrumentConfigTest {
    private static final String TEST_STRING = "test";
    private static final DateTime TEST_DATE_TIME = DateTime.now();
    private static final long TEST_LONG = 1234L;
    private static final double TEST_DOUBLE = 12.34;
    private static final OptionType TEST_OPTION_TYPE = OptionType.Call;
    private static final InstrumentType TEST_TYPE = InstrumentType.Basket;
    private static final int TEST_INT = 1234;

    @Test
    public void shouldGetId() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setId(TEST_STRING);

        assertThat(uut.getId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetName() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setName(TEST_STRING);

        assertThat(uut.getName(), is(TEST_STRING));
    }

    @Test
    public void shouldGetMultiplier() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setMultiplier(TEST_DOUBLE);

        assertThat(uut.getMultiplier(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetLotSize() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setLotSize(TEST_INT);

        assertThat(uut.getLotSize(), is(TEST_INT));
    }

    @Test
    public void shouldGetMarketId() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setMarketId(TEST_STRING);

        assertThat(uut.getMarketId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetType() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setType(TEST_TYPE);

        assertThat(uut.getType(), is(TEST_TYPE));
    }

    @Test
    public void shouldGetMembers() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.addMember(TEST_STRING);

        assertThat(uut.getMembers(), not(nullValue()));
    }

    @Test
    public void shouldGetPartOf() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.addPartOf(TEST_STRING);

        assertThat(uut.getPartOf(), not(nullValue()));
    }

    @Test
    public void shouldGetBasketConstituents() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.addBasketConstituent(TEST_STRING);

        assertThat(uut.getBasketConstituents(), not(nullValue()));
    }

    @Test
    public void shouldGetBondIssue() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setBondIssue(TEST_DATE_TIME);

        assertThat(uut.getBondIssue(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetBondMaturity() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setBondMaturity(TEST_DATE_TIME);

        assertThat(uut.getBondMaturity(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetBondFirstCoupon() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setBondFirstCoupon(TEST_DATE_TIME);

        assertThat(uut.getBondFirstCoupon(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetBondCoupon() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setBondCoupon(TEST_DOUBLE);

        assertThat(uut.getBondCoupon(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetBondNotional() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setBondNotional(TEST_DOUBLE);

        assertThat(uut.getBondNotional(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetUnderlyingId() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setUnderlyingId(TEST_STRING);

        assertThat(uut.getUnderlyingId(), is(TEST_STRING));
    }

    @Test
    public void shouldGetMaturity() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setMaturity(TEST_DATE_TIME);

        assertThat(uut.getMaturity(), is(TEST_DATE_TIME));
    }

    @Test
    public void shouldGetOptionType() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setOptionType(TEST_OPTION_TYPE);

        assertThat(uut.getOptionType(), is(TEST_OPTION_TYPE));
    }

    @Test
    public void shouldGetStrike() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setStrike(TEST_DOUBLE);

        assertThat(uut.getStrike(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetConversionRatio() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setConversionRatio(TEST_DOUBLE);

        assertThat(uut.getConversionRatio(), is(TEST_DOUBLE));
    }

    @Test
    public void shouldGetIssueSize() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setIssueSize(TEST_LONG);

        assertThat(uut.getIssueSize(), is(TEST_LONG));
    }

    @Test
    public void shouldGetIssuer() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setIssuer(TEST_STRING);

        assertThat(uut.getIssuer(), is(TEST_STRING));
    }

    @Test
    public void shouldGetIssueDate() throws Exception {
        InstrumentConfig uut = new InstrumentConfig();
        uut.setIssueDate(TEST_DATE_TIME);

        assertThat(uut.getIssueDate(), is(TEST_DATE_TIME));
    }
}
