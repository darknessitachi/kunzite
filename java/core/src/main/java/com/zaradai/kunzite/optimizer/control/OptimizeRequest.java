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
package com.zaradai.kunzite.optimizer.control;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zaradai.kunzite.optimizer.model.InputRow;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTactic;

public final class OptimizeRequest {
    private final Class<? extends OptimizerTactic> tactic;
    private final String target;
    private final boolean lookForMaxima;
    private final InputRow start;

    private OptimizeRequest(Class<? extends OptimizerTactic> tactic, String target, boolean lookForMaxima,
                            InputRow start) {
        this.tactic = tactic;
        this.target = target;
        this.lookForMaxima = lookForMaxima;
        this.start = start;
    }

    public static OptimizeRequest newRequest(Class<? extends OptimizerTactic> tactic, String target,
                                             boolean lookForMaxima, InputRow start) {
        Preconditions.checkNotNull(tactic, "Invalid tactic specified");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(target), "No target specified");
        Preconditions.checkNotNull(start, "No start position specified");

        return new OptimizeRequest(tactic, target, lookForMaxima, start);
    }

    public Class<? extends OptimizerTactic> getTactic() {
        return tactic;
    }

    public String getTarget() {
        return target;
    }

    public boolean isLookForMaxima() {
        return lookForMaxima;
    }

    public InputRow getStart() {
        return start;
    }
}
