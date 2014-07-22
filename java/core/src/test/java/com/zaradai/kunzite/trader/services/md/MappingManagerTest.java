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
package com.zaradai.kunzite.trader.services.md;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.trader.config.md.MappingConfig;
import com.zaradai.kunzite.trader.config.md.MappingValue;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class MappingManagerTest {
    private static final String MAP_1 = "map1";
    private static final String MAP_2 = "map2";
    private static final String ID1_MAP_1 = "A";
    private static final String SID1_MAP_1 = "A-S1";
    private static final String ID2_MAP_1 = "B";
    private static final String SID2_MAP_1 = "B-S2";
    private static final String ID1_MAP_2 = "H";
    private static final String SID1_MAP_2 = "H-S1";
    private static final String ID2_MAP_2 = "G";
    private static final String SID2_MAP_2 = "G-S2";

    private List<MappingConfig> testConfig;
    private MappingManager uut;

    @Before
    public void setUp() throws Exception {
        testConfig = Lists.newArrayList();
        MappingConfig config1 = new MappingConfig();
        config1.setName(MAP_1);
        MappingValue mappingValue = new MappingValue();
        mappingValue.setId(ID1_MAP_1);
        mappingValue.setSid(SID1_MAP_1);
        config1.add(mappingValue);
        mappingValue = new MappingValue();
        mappingValue.setId(ID2_MAP_1);
        mappingValue.setSid(SID2_MAP_1);
        config1.add(mappingValue);
        testConfig.add(config1);
        MappingConfig config2 = new MappingConfig();
        config2.setName(MAP_2);
        mappingValue = new MappingValue();
        mappingValue.setId(ID1_MAP_2);
        mappingValue.setSid(SID1_MAP_2);
        config2.add(mappingValue);
        mappingValue = new MappingValue();
        mappingValue.setId(ID2_MAP_2);
        mappingValue.setSid(SID2_MAP_2);
        config2.add(mappingValue);
        testConfig.add(config2);
        uut = new MappingManager(testConfig);
    }

    @Test
    public void shouldGetSid() throws Exception {
        assertThat(uut.getSid(MAP_1, ID1_MAP_1), is(SID1_MAP_1));
        assertThat(uut.getSid(MAP_1, ID2_MAP_1), is(SID2_MAP_1));
        assertThat(uut.getSid(MAP_2, ID1_MAP_2), is(SID1_MAP_2));
        assertThat(uut.getSid(MAP_2, ID2_MAP_2), is(SID2_MAP_2));
    }

    @Test
    public void shouldGetReverseAfterGetSid() throws Exception {
        uut.getSid(MAP_1, ID1_MAP_1);
        assertThat(uut.getId(SID1_MAP_1), is(ID1_MAP_1));
    }

    @Test
    public void shouldNotGetReverseBeforeGetSidCalled() throws Exception {
        assertThat(uut.getId(SID1_MAP_1), is(nullValue()));
    }
}
