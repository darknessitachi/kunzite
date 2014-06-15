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
package com.zaradai.kunzite.optimizer.data.dataset.cache.memory;

import com.zaradai.kunzite.optimizer.config.Configuration;
import com.zaradai.kunzite.optimizer.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InMemoryDataCacheTest {
    private static final int CACHE_SIZE = 100;
    @Mock
    Map<InputRow, Row> cache;
    private InMemoryDataCache uut;
    private int createdCacheSize;
    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        configuration = mock(Configuration.class);
        when(configuration.getMaxCacheSize()).thenReturn(CACHE_SIZE);
        uut = new InMemoryDataCache(configuration) {
            @Override
            protected Map<InputRow, Row> createCache(int maxCacheSize) {
                createdCacheSize = maxCacheSize;
                return cache;
            }
        };
    }

    @Test
    public void shouldCreateWithRequiredCacheSize() throws Exception {
        assertThat(createdCacheSize, is(CACHE_SIZE));
    }

    @Test
    public void shouldPut() throws Exception {
        Row row = createRow();

        uut.put(row);

        verify(cache).put(row.getInput(), row);
    }

    @Test
    public void shouldGet() throws Exception {
        Row row = createRow();

        uut.get(row.getInput());

        verify(cache).get(row.getInput());
    }

    @Test
    public void shouldGetSameAfterPut() throws Exception {
        Row row = createRow();
        uut = new InMemoryDataCache(configuration);
        uut.put(row);

        assertThat(uut.get(row.getInput()), is(row));
    }

    private Row createRow() {
        return Row.fromSchema(RowSchema.newInstance(
                InputRowSchema.newBuilder().build(),
                OutputRowSchema.newBuilder().build()
        ));
    }
}
