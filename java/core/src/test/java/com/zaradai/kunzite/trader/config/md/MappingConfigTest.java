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
package com.zaradai.kunzite.trader.config.md;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class MappingConfigTest {
    private static final String TEST_STRING = "test";
    private static final MappingValue MAP_VALUE_1 = mock(MappingValue.class);
    private static final MappingValue MAP_VALUE_2 = mock(MappingValue.class);

    @Test
    public void shouldGetName() throws Exception {
        MappingConfig uut = new MappingConfig();
        uut.setName(TEST_STRING);

        assertThat(uut.getName(), is(TEST_STRING));
    }

    @Test
    public void shouldHaveNoMappingsOnConstruction() throws Exception {
        MappingConfig uut = new MappingConfig();

        assertThat(uut.getMappings(), emptyIterable());
    }

    @Test
    public void shouldHaveMappingsAfterAdding() throws Exception {
        MappingConfig uut = new MappingConfig();

        uut.add(MAP_VALUE_1);
        uut.add(MAP_VALUE_2);

        assertThat(uut.getMappings(), containsInAnyOrder(MAP_VALUE_1, MAP_VALUE_2));
    }
}
