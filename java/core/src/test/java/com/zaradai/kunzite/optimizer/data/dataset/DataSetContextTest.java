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
package com.zaradai.kunzite.optimizer.data.dataset;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class DataSetContextTest {
    private static final String TEST_NAME = "name";
    private static final String TEST_DESC = "desc";
    private static final String TEST_VERSION = "ver";
    private static final String TEST_CREATOR = "createdBy";
    private static final DateTime TEST_CREATED_ON = DateTime.now();
    private static final String TEST_HOST = "host";
    private static final UUID TEST_ID = UUID.randomUUID();

    private DataSetContext uut;

    @Before
    public void setUp() throws Exception {
        uut = DataSetContext.builder()
                .name(TEST_NAME)
                .description(TEST_DESC)
                .version(TEST_VERSION)
                .createdBy(TEST_CREATOR)
                .createdOn(TEST_CREATED_ON)
                .host(TEST_HOST)
                .id(TEST_ID)
                .build();
    }

    @Test
    public void shouldGetName() throws Exception {
        assertThat(uut.getName(), is(TEST_NAME));
    }

    @Test
    public void shouldGetDescription() throws Exception {
        assertThat(uut.getDescription(), is(TEST_DESC));
    }

    @Test
    public void shouldGetId() throws Exception {
        assertThat(uut.getId(), is(TEST_ID));
    }

    @Test
    public void shouldGetCreatedOn() throws Exception {
        assertThat(uut.getCreatedOn(), is(TEST_CREATED_ON));
    }

    @Test
    public void shouldGetCreatedBy() throws Exception {
        assertThat(uut.getCreatedBy(), is(TEST_CREATOR));
    }

    @Test
    public void shouldGetCreatedHost() throws Exception {
        assertThat(uut.getCreatedHost(), is(TEST_HOST));
    }

    @Test
    public void shouldGetVersion() throws Exception {
        assertThat(uut.getVersion(), is(TEST_VERSION));
    }

    @Test
    public void shouldBuildWithDefaults() throws Exception {
        DataSetContext uut = DataSetContext.builder().name(TEST_NAME).build();

        assertThat(uut.getId(), not(nullValue()));
        assertThat(uut.getVersion(), not(nullValue()));
        assertThat(uut.getCreatedOn(), not(nullValue()));
        assertThat(uut.getCreatedBy(), not(nullValue()));
        assertThat(uut.getCreatedHost(), not(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIfNameNotSet() throws Exception {
        DataSetContext uut = DataSetContext.builder().build();
    }

    @Test
    public void shouldEqualSameInstance() throws Exception {
        assertThat(uut.equals(uut), is(true));
    }

    @Test
    public void shouldNotEqualIfOtherIsNull() throws Exception {
        assertThat(uut.equals(null), is(false));
    }

    @Test
    public void shouldNotEqualIfOtherNotSameType() throws Exception {
        assertThat(uut.equals(new Object()), is(false));
    }

    @Test
    public void shouldNotEqualIfIdDifferent() throws Exception {
        DataSetContext other = DataSetContext.builder().name(TEST_NAME).build();

        assertThat(uut.equals(other), is(false));
    }

    @Test
    public void shouldEqualIfIdSame() throws Exception {
        DataSetContext other = DataSetContext.builder().name(TEST_NAME).id(uut.getId()).build();

        assertThat(uut.equals(other), is(true));
    }

    @Test
    public void shouldHashOnId() throws Exception {
        int hash = uut.getId().hashCode();

        assertThat(uut.hashCode(), is(hash));
    }
}
