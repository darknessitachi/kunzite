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
import com.zaradai.kunzite.optimizer.model.Row;

import java.util.UUID;

public final class DataResult {
    private final UUID requestId;
    private final Row row;

    private DataResult(UUID requestId, Row row) {
        this.requestId = requestId;
        this.row = row;
    }

    public static DataResult newResult(UUID requestId, Row row) {
        Preconditions.checkNotNull(requestId, "Invalid request id");
        Preconditions.checkNotNull(row, "Invalid Row");

        return new DataResult(requestId, row);
    }

    public UUID getRequestId() {
        return requestId;
    }

    public Row getRow() {
        return row;
    }
}
