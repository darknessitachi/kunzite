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

import com.google.inject.Inject;
import com.zaradai.kunzite.config.ConfigurationSource;

public class OptimizerConfigurationImpl implements OptimizerConfiguration {
    private static final String PRE = "opt";

    public static final String MAX_CACHE_SIZE = PRE + ".max.cache.size";
    public static final String EVALUATOR_THREAD_SIZE = PRE + ".evaluator.thread.size";
    public static final String RESULT_RING_SIZE = PRE + ".result.ring.size";
    public static final String REQUEST_RING_SIZE = PRE + ".request.ring.size";
    public static final String FLOOD_BATCH_SIZE = PRE + ".flood.batch.size";
    public static final String NUM_SHOTGUN_CLIMBERS = PRE + ".num.shotgun.climbers";

    public static final int DEFAULT_MAX_CACHE_SIZE = 1048576;
    public static final int DEFAULT_EVALUATOR_THREAD_SIZE = 8;
    public static final int DEFAULT_RESULT_RING_SIZE = 1048576;
    public static final int DEFAULT_REQUEST_RING_SIZE = 1048576;
    public static final int DEFAULT_FLOOD_BATCH_SIZE = 100;
    public static final int DEFAULT_NUM_SHOTGUN_CLIMBERS = 6;

    private final ConfigurationSource source;

    @Inject
    OptimizerConfigurationImpl(ConfigurationSource source) {
        this.source = source;
    }

    @Override
    public int getMaxCacheSize() {
        return source.get(MAX_CACHE_SIZE, DEFAULT_MAX_CACHE_SIZE);
    }

    @Override
    public int getEvaluatorThreadSize() {
        return source.get(EVALUATOR_THREAD_SIZE, DEFAULT_EVALUATOR_THREAD_SIZE);
    }

    @Override
    public int getResultRingSize() {
        return source.get(RESULT_RING_SIZE, DEFAULT_RESULT_RING_SIZE);
    }

    @Override
    public int getRequestRingSize() {
        return source.get(REQUEST_RING_SIZE, DEFAULT_REQUEST_RING_SIZE);
    }

    @Override
    public int getFloodBatchSize() {
        return source.get(FLOOD_BATCH_SIZE, DEFAULT_FLOOD_BATCH_SIZE);
    }

    @Override
    public int getNumShotgunClimbers() {
        return source.get(NUM_SHOTGUN_CLIMBERS, DEFAULT_NUM_SHOTGUN_CLIMBERS);
    }
}
