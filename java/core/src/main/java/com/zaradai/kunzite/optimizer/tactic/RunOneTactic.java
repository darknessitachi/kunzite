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
package com.zaradai.kunzite.optimizer.tactic;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.zaradai.kunzite.optimizer.data.DataRequestManager;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.List;

public class RunOneTactic extends AbstractTactic {
    static final String OPTIMIZER_NAME = "Run One";

    @Inject
    RunOneTactic(DataRequestManager dataRequestManager) {
        super(dataRequestManager);
    }

    @Override
    protected void initialize() {
        //NOP
    }

    @Override
    protected List<InputRow> prepare() {
        return Lists.newArrayList(getStart());
    }

    @Override
    protected boolean process(List<Row> results) {
        testValue(results.get(0));

        return true;
    }

    @Override
    public String getName() {
        return OPTIMIZER_NAME;
    }
}
