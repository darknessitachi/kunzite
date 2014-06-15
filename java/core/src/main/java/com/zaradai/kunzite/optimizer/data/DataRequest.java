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

import com.google.common.base.Preconditions;
import com.zaradai.kunzite.optimizer.eval.Evaluator;
import com.zaradai.kunzite.optimizer.model.InputRow;

import java.util.UUID;

public final class DataRequest {
    private UUID requester;
    private InputRow request;
    private Class<? extends Evaluator> evaluator;

    private DataRequest() {

    }

    private DataRequest(UUID requester, InputRow request, Class<? extends Evaluator> evaluator) {
        this.requester = requester;
        this.request = request;
        this.evaluator = evaluator;
    }

    public static DataRequest newRequest() {
        return new DataRequest();
    }

    public static DataRequest newRequest(UUID requester, InputRow request, Class<? extends Evaluator> evaluator) {
        Preconditions.checkNotNull(requester, "Invalid requester");
        Preconditions.checkNotNull(request, "Invalid request");
        Preconditions.checkNotNull(evaluator, "Invalid evaluator");

        return new DataRequest(requester, request, evaluator);
    }

    public UUID getRequester() {
        return requester;
    }

    public InputRow getRequest() {
        return request;
    }

    public Class<? extends Evaluator> getEvaluator() {
        return evaluator;
    }

    public void setRequester(UUID requester) {
        this.requester = requester;
    }

    public void setRequest(InputRow request) {
        this.request = request;
    }

    public void setEvaluator(Class<? extends Evaluator> evaluator) {
        this.evaluator = evaluator;
    }
}
