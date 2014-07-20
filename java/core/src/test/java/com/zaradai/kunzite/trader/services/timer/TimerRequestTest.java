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
package com.zaradai.kunzite.trader.services.timer;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

public class TimerRequestTest {
    private static final boolean TEST_REPEAT = true;
    private static final TimeUnit TEST_UNIT = TimeUnit.MILLISECONDS;
    private static final long TEST_DURATION = 5;
    private static final long TEST_TIMEOUT = DateTime.now().getMillis();
    private static final UUID TEST_ID = UUID.randomUUID();

    @Test
    public void shouldCreateWithParameters() throws Exception {
        TimerRequest uut = TimerRequest.newInstance(TEST_ID, TEST_TIMEOUT, TEST_DURATION, TEST_UNIT, TEST_REPEAT);

        assertThat(uut.getId(), is(TEST_ID));
        assertThat(uut.getDuration(), is(TEST_DURATION));
        assertThat(uut.getTimeout(), is(TEST_TIMEOUT));
        assertThat(uut.getUnit(), is(TEST_UNIT));
        assertThat(uut.isRepeat(), is(TEST_REPEAT));
    }

    @Test
    public void shouldCalculateNextTimeoutIfRepeating() throws Exception {
        long expected = TEST_TIMEOUT + TEST_UNIT.toMillis(TEST_DURATION);
        TimerRequest uut = TimerRequest.newInstance(TEST_ID, TEST_TIMEOUT, TEST_DURATION, TEST_UNIT, TEST_REPEAT);

        TimerRequest res = uut.getNextRequest();

        assertThat(res.getTimeout(), is(expected));
        assertThat(res.getId(), is(TEST_ID));
        assertThat(res.getDuration(), is(TEST_DURATION));
        assertThat(res.getUnit(), is(TEST_UNIT));
        assertThat(res.isRepeat(), is(TEST_REPEAT));
    }

    @Test
    public void shouldReturnNullIfNotRepeating() throws Exception {
        TimerRequest uut = TimerRequest.newInstance(TEST_ID, TEST_TIMEOUT, TEST_DURATION, TEST_UNIT, false);

        TimerRequest res = uut.getNextRequest();

        assertThat(res, is(nullValue()));
    }

    @Test
    public void shouldCompareOldestFirst() throws Exception {
        TimerRequest oldest = TimerRequest.newInstance(TEST_ID, 1000, TEST_DURATION, TEST_UNIT, TEST_REPEAT);
        TimerRequest newest = TimerRequest.newInstance(TEST_ID, 2000, TEST_DURATION, TEST_UNIT, TEST_REPEAT);

        assertThat(TimerRequest.OLDEST_FIRST.compare(oldest, newest), lessThan(0));
        assertThat(TimerRequest.OLDEST_FIRST.compare(newest, oldest), greaterThan(0));
        assertThat(TimerRequest.OLDEST_FIRST.compare(oldest, oldest), is(0));
    }
}
