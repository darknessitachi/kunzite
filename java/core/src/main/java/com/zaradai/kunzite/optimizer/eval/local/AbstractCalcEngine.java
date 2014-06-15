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
package com.zaradai.kunzite.optimizer.eval.local;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractIdleService;
import com.zaradai.kunzite.optimizer.data.DataManager;
import com.zaradai.kunzite.optimizer.data.DataRequest;
import com.zaradai.kunzite.optimizer.data.DataResult;
import com.zaradai.kunzite.optimizer.eval.CalcEngine;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.eval.EvaluatorFactory;
import com.zaradai.kunzite.optimizer.model.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractCalcEngine extends AbstractIdleService implements CalcEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalcEngine.class);

    private DataManager dataManager;
    private final EvaluatorFactory evaluatorFactory;
    private final Map<Class<? extends Evaluator>, Evaluator> evaluatorCache;

    protected AbstractCalcEngine(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
        evaluatorCache = createEvaluatorCache();
    }

    protected Map<Class<? extends Evaluator>, Evaluator> createEvaluatorCache() {
        return Maps.newHashMap();
    }

    @Override
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    protected void doCalc(DataRequest request) {
        try {
            // get an evaluator
            Evaluator evaluator = getEvaluator(request);
            // do the evaluation
            Row result = evaluator.evaluate(request.getRequest());
            // return result
            sendResult(request, result);
        } catch (Exception e) {
            LOGGER.error("Unable to evaluate", e);
        }
    }

    protected DataManager getDataManager() {
        return dataManager;
    }

    private void sendResult(DataRequest request, Row row) {
        Preconditions.checkState(dataManager != null, "Data Manager not set, invalid state");

        dataManager.onDataResult(DataResult.newResult(request.getRequester(), row));
    }

    private Evaluator getEvaluator(DataRequest request) {
        Class<? extends Evaluator> clazz = request.getEvaluator();
        Evaluator res = evaluatorCache.get(clazz);

        if (res == null) {
            res = evaluatorFactory.create(clazz);
            evaluatorCache.put(clazz, res);
        }

        return res;
    }
}
