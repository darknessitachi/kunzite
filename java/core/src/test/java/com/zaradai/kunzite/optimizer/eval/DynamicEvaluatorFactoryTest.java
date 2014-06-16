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
package com.zaradai.kunzite.optimizer.eval;

import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DynamicEvaluatorFactoryTest {
    private Injector injector;
    private DynamicEvaluatorFactory uut;

    @Before
    public void setUp() throws Exception {
        injector = mock(Injector.class);
        uut = new DynamicEvaluatorFactory(injector);
    }

    @Test
    public void shouldCreate() throws Exception {
        Class<? extends Evaluator> clazz = Evaluator.class;

        uut.create(clazz);

        verify(injector).getInstance(clazz);
    }
}