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
package com.zaradai.kunzite.optimizer.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class InputSpec {
    private final int position;
    private final Series series;

    private InputSpec(int position, Series series) {
        this.position = position;
        this.series = series;
    }

    public static InputSpec newInstance(int position, Series series) {
        Preconditions.checkArgument(position >= 0, "Invalid input position specified");
        Preconditions.checkNotNull(series, "Invalid Series supplied");

        return new InputSpec(position, series);
    }

    public int getPosition() {
        return position;
    }

    public Series getSeries() {
        return series;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position, series);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final InputSpec other = (InputSpec) obj;

        return Objects.equal(position, other.position)
                && Objects.equal(series, other.series);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("Pos", position)
                .addValue(series)
                .toString();
    }
}
