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
package com.zaradai.kunzite.optimizer.data.matrix;

import com.google.common.base.Objects;

public final class Pair {
    private final int x;
    private final int y;
    private final int hashCode;

    private Pair(int x, int y) {
        this.x = x;
        this.y = y;

        hashCode = Objects.hashCode(getX(), getY());
    }

    public static Pair newPair(int x, int y) {
        //clamp pair to positive??
        return new Pair(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Pair other = (Pair) obj;

        return (other.x == x && other.y == y);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("X", getX()).add("Y", getY()).toString();
    }
}
