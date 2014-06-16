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
package com.zaradai.kunzite.optimizer.config;

import com.zaradai.kunzite.config.ConfigurationSource;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class OptimizerConfigurationImplTest {
    private ConfigurationSource source;
    private OptimizerConfigurationImpl uut;

    @Before
    public void setUp() throws Exception {
        source = mock(ConfigurationSource.class);
        uut = new OptimizerConfigurationImpl(source);
    }

    @Test
    public void shouldGetMaxCacheSize() throws Exception {
        uut.getMaxCacheSize();

        verify(source).get(OptimizerConfigurationImpl.MAX_CACHE_SIZE,
                OptimizerConfigurationImpl.DEFAULT_MAX_CACHE_SIZE);
    }

    @Test
    public void shouldGetEvaluatorThreadSize() throws Exception {
        uut.getEvaluatorThreadSize();

        verify(source).get(OptimizerConfigurationImpl.EVALUATOR_THREAD_SIZE,
                OptimizerConfigurationImpl.DEFAULT_EVALUATOR_THREAD_SIZE);
    }

    @Test
    public void shouldGetResultRingSize() throws Exception {
        uut.getResultRingSize();

        verify(source).get(OptimizerConfigurationImpl.RESULT_RING_SIZE,
                OptimizerConfigurationImpl.DEFAULT_RESULT_RING_SIZE);
    }

    @Test
    public void shouldGetRequestRingSize() throws Exception {
        uut.getRequestRingSize();

        verify(source).get(OptimizerConfigurationImpl.REQUEST_RING_SIZE,
                OptimizerConfigurationImpl.DEFAULT_REQUEST_RING_SIZE);
    }

    @Test
    public void shouldGetFloodBatchSize() throws Exception {
        uut.getFloodBatchSize();

        verify(source).get(OptimizerConfigurationImpl.FLOOD_BATCH_SIZE,
                OptimizerConfigurationImpl.DEFAULT_FLOOD_BATCH_SIZE);
    }

    @Test
    public void shouldGetNumShotgunClimbers() throws Exception {
        uut.getNumShotgunClimbers();

        verify(source).get(OptimizerConfigurationImpl.NUM_SHOTGUN_CLIMBERS,
                OptimizerConfigurationImpl.DEFAULT_NUM_SHOTGUN_CLIMBERS);
    }
}
