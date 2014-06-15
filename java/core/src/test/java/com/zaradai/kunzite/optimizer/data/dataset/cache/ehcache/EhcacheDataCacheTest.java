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
package com.zaradai.kunzite.optimizer.data.dataset.cache.ehcache;

import com.zaradai.kunzite.optimizer.model.*;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class EhcacheDataCacheTest {
    private static final int CACHE_SIZE = 100;
    @Mock
    Ehcache cache;
    private EhcacheDataCache uut;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        uut = new EhcacheDataCache(cache);
    }

    @Test
    public void shouldPut() throws Exception {
        Row row = createRow();

        uut.put(row);

        ArgumentCaptor<Element> captor = ArgumentCaptor.forClass(Element.class);
        verify(cache).put(captor.capture());

        assertThat((InputRow) captor.getValue().getObjectKey(), is(row.getInput()));
        assertThat((Row) captor.getValue().getObjectValue(), is(row));
    }

    @Test
    public void shouldGetIfInCache() throws Exception {
        Row row = createRow();
        Element element = new Element(row.getInput(), row);
        when(cache.get(row.getInput())).thenReturn(element);

        Row res = uut.get(row.getInput());

        assertThat(res, is(row));
    }

    @Test
    public void shouldReturnNullIfNotInCache() throws Exception {
        Row row = createRow();

        Row res = uut.get(row.getInput());

        assertThat(res, is(nullValue()));
    }

    private Row createRow() {
        return Row.fromSchema(RowSchema.newInstance(
                InputRowSchema.newBuilder().build(),
                OutputRowSchema.newBuilder().build()
        ));
    }
}
