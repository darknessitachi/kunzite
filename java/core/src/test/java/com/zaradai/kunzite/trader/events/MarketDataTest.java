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
package com.zaradai.kunzite.trader.events;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MarketDataTest {
    private static final String TEST_INST_ID = "test";
    private static final DateTime TEST_TIMESTAMP = DateTime.now();
    private static final List<MarketDataField> TEST_FIELDS = Lists.newArrayList();

    @Test
    public void shouldCreate() throws Exception {
        MarketData uut = MarketData.newInstance(TEST_INST_ID, TEST_TIMESTAMP, TEST_FIELDS);

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), is(TEST_TIMESTAMP));
        assertThat(uut.getFields(), is(TEST_FIELDS));
    }

    @Test
    public void shouldCreateWithDefaultTimestamp() throws Exception {
        MarketData uut = MarketData.newInstance(TEST_INST_ID, TEST_FIELDS);

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), not(nullValue()));
        assertThat(uut.getFields(), is(TEST_FIELDS));
    }

    @Test
    public void shouldCreateWithFieldIterator() throws Exception {
        MarketData uut = MarketData.newInstance(TEST_INST_ID, TEST_TIMESTAMP, TEST_FIELDS.iterator());

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), is(TEST_TIMESTAMP));
        assertThat(uut.getFields(), is(TEST_FIELDS));
    }

    @Test
    public void shouldCreateWithDefaultTimestampWithFieldIterator() throws Exception {
        MarketData uut = MarketData.newInstance(TEST_INST_ID, TEST_FIELDS.iterator());

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), not(nullValue()));
        assertThat(uut.getFields(), is(TEST_FIELDS));
    }

    @Test
    public void shouldCreateWithFieldIterable() throws Exception {
        Iterable<MarketDataField> iterable = TEST_FIELDS;
        MarketData uut = MarketData.newInstance(TEST_INST_ID, TEST_TIMESTAMP, iterable);

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), is(TEST_TIMESTAMP));
        assertThat(uut.getFields(), is(TEST_FIELDS));
    }

    @Test
    public void shouldCreateWithDefaultTimestampWithVarargs() throws Exception {
        MarketDataField field = MarketDataField.newDoubleValue(MarketDataFieldType.ASK10_SIZE, 0.0);
        MarketData uut = MarketData.newInstance(TEST_INST_ID, field);

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), not(nullValue()));
        assertThat(uut.getFields().size(), is(1));
    }

    @Test
    public void shouldCreateWithVarargs() throws Exception {
        MarketDataField field = MarketDataField.newDoubleValue(MarketDataFieldType.ASK10_SIZE, 0.0);
        MarketData uut = MarketData.newInstance(TEST_INST_ID, TEST_TIMESTAMP, field);

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), is(TEST_TIMESTAMP));
        assertThat(uut.getFields().size(), is(1));
    }

    @Test
    public void shouldCreateWithDefaultTimestampWithFieldIterable() throws Exception {
        Iterable<MarketDataField> iterable = TEST_FIELDS;

        MarketData uut = MarketData.newInstance(TEST_INST_ID, iterable);

        assertThat(uut.getInstrumentId(), is(TEST_INST_ID));
        assertThat(uut.getTimestamp(), not(nullValue()));
        assertThat(uut.getFields(), is(TEST_FIELDS));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldFailWithInvalidInstrument() throws Exception {
        MarketData uut = MarketData.newInstance(null, TEST_FIELDS);
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailWithInvalidFields() throws Exception {
        MarketData uut = MarketData.newInstance(TEST_INST_ID, (List< MarketDataField >) null);
    }
}
