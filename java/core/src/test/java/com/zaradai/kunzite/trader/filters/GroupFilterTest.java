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

import com.zaradai.kunzite.trader.orders.OrderRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GroupFilterTest {
    private GroupFilter uut;
    private Filter goodFilter1;
    private Filter goodFilter2;
    private Filter badFilter1;
    private Filter badFilter2;

    @Before
    public void setUp() throws Exception {
        uut = new GroupFilter();
        goodFilter1 = mock(Filter.class);
        when(goodFilter1.check(any(OrderRequest.class))).thenReturn(true);
        goodFilter2 = mock(Filter.class);
        when(goodFilter2.check(any(OrderRequest.class))).thenReturn(true);
        badFilter1 = mock(Filter.class);
        when(badFilter1.check(any(OrderRequest.class))).thenReturn(false);
        badFilter2 = mock(Filter.class);
        when(badFilter2.check(any(OrderRequest.class))).thenReturn(false);
    }

    @Test
    public void shouldCheckAndReturnTrueIfAllFiltersPass() throws Exception {
        uut.add(goodFilter1);
        uut.add(goodFilter2);

        OrderRequest orderRequest = new OrderRequest();
        assertThat(uut.check(orderRequest), is(true));
    }

    @Test
    public void shouldCheckAndReturnFalseIfOneFiltersFails() throws Exception {
        uut.add(goodFilter1);
        uut.add(badFilter1);
        uut.add(goodFilter2);
        uut.add(badFilter2);

        OrderRequest orderRequest = new OrderRequest();
        assertThat(uut.check(orderRequest), is(false));
    }

    @Test
    public void shouldReturnTrueIfContainingNoFilters() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        assertThat(uut.check(orderRequest), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailIfRequestIsInvalid() throws Exception {
        uut.check(null);
    }

    @Test
    public void shouldGetName() throws Exception {
        GroupFilter uut = new GroupFilter();

        assertThat(uut.getName(), is(GroupFilter.FILTER_NAME));
    }
}
