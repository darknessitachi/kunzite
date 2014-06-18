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
package com.zaradai.kunzite.optimizer.data;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.eval.CalcEngine;
import com.zaradai.kunzite.optimizer.model.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultDataManager implements DataManager, RequestListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataManager.class);
    public static final String METRIC_NAME_NEW_CALC = "DataManager.new.calc";
    public static final String METRIC_NAME_FROM_DB = "DataManager.hit.db";

    private final DataSet dataSet;
    private final CalcEngine calcEngine;
    private final DataRequestManager requestManager;
    private final Meter newCalc;
    private final Meter fromDb;

    @Inject
    DefaultDataManager(MetricRegistry metrics, DataRequestManager requestManager, @Assisted CalcEngine calcEngine,
                       @Assisted DataSet dataSet) {
        this.dataSet = dataSet;
        this.calcEngine = calcEngine;
        newCalc = metrics.meter(METRIC_NAME_NEW_CALC);
        fromDb =  metrics.meter(METRIC_NAME_FROM_DB);

        this.requestManager = requestManager;
        this.requestManager.addListener(this);
        this.calcEngine.setDataManager(this);
    }

    @Override
    public void onDataResult(DataResult dataResult) {
        // get the result
        Row row = dataResult.getRow();
        // add to the data set
        dataSet.add(row);
        // put on the result queue
        requestManager.handleResult(dataResult);
    }

    @Override
    public void onRequest(DataRequest request) {
        Preconditions.checkNotNull(request, "Data request cannot be null");
        // try and get from data set
        Row result = dataSet.get(request.getRequest());

        if (result != null) {
            //db hit
            fromDb.mark();
            // put on the result queue
            requestManager.handleResult(DataResult.newResult(request.getRequester(), result));
        } else {
            newCalc.mark();
            calcEngine.calculate(request);
        }
    }
}
