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
package com.zaradai.kunzite.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UtilsTest {
    @Test
    public void shouldBeApproxSameWithinEpsilon() throws Exception {
        double epsilon = 0.1;
        double test1 = 10.21;
        double test2 = 10.3;

        assertThat(Utils.areApproxSame(test1, test2, epsilon), is(true));
    }

    @Test
    public void shouldNotBeSameIfOutsideEpsilon() throws Exception {
        double epsilon = 0.1;
        double test1 = 10.21;
        double test2 = 10.4;

        assertThat(Utils.areApproxSame(test1, test2, epsilon), is(false));
    }

    @Test
     public void shouldBeSameUsingDefaultEpsilon() throws Exception {
        assertThat(Utils.areApproxSame(1.1e-7, 1.6e-7), is(true));
    }

    @Test
    public void shouldNotBeSameUsingDefaultEpsilon() throws Exception {
        assertThat(Utils.areApproxSame(1.1e-7, 1.8e-7), is(false));
    }
}
