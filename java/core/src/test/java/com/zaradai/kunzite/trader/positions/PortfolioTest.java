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
package com.zaradai.kunzite.trader.positions;

import com.zaradai.kunzite.trader.mocks.PositionMocker;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class PortfolioTest {
    private static final String TEST_ID = "test";
    private static final String TEST_INS_ID_1 = "ins1";
    private static final String TEST_INS_ID_2 = "ins2";
    private static final Long TEST_NET_POS_1 = 1234L;
    private static final Long TEST_NET_POS_2 = 1234L;
    private static final Long TEST_NET_NET = TEST_NET_POS_1 + TEST_NET_POS_2;

    @Test
    public void shouldCreateWithId() throws Exception {
        Portfolio uut = createPortfolio();

        assertThat(uut.getId(), is(TEST_ID));
    }

    @Test
    public void shouldAddNewPosition() throws Exception {
        Portfolio uut = createPortfolio();
        Position position = createPosition(TEST_ID, TEST_INS_ID_1);

        uut.addPosition(position);

        assertThat(uut.hasPosition(TEST_INS_ID_1), is(true));
    }

    @Test
    public void shouldReturnZeroForUnknownPosition() throws Exception {
        Portfolio uut = createPortfolio();

        assertThat(uut.getPositionFor(TEST_INS_ID_1), is(0L));
    }

    @Test
    public void shouldReturnKnownPositionsNetPosition() throws Exception {
        Portfolio uut = createPortfolio();
        Position position = createPosition(TEST_ID, TEST_INS_ID_1);
        when(position.getNet()).thenReturn(TEST_NET_POS_1);

        uut.addPosition(position);

        assertThat(uut.getPositionFor(TEST_INS_ID_1), is(TEST_NET_POS_1));
    }


    @Test
    public void shouldGetNetPosition() throws Exception {
        Portfolio uut = createPortfolio();
        Position position1 = createPosition(TEST_ID, TEST_INS_ID_1);
        when(position1.getNet()).thenReturn(TEST_NET_POS_1);
        Position position2 = createPosition(TEST_ID, TEST_INS_ID_2);
        when(position2.getNet()).thenReturn(TEST_NET_POS_2);

        uut.addPosition(position1);
        uut.addPosition(position2);

        assertThat(uut.getNetPosition(), is(TEST_NET_NET));
    }

    private Portfolio createPortfolio() {
        return new Portfolio(TEST_ID);
    }

    private Position createPosition(String portfolioId, String instrumentId) {
        return PositionMocker.create(portfolioId, instrumentId);
    }
}
