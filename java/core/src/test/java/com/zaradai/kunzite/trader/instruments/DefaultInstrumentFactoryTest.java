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
package com.zaradai.kunzite.trader.instruments;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultInstrumentFactoryTest {
    private BasketFactory basketFactory;
    private BondFactory bondFactory;
    private ForwardFactory forwardFactory;
    private FutureFactory futureFactory;
    private IndexFactory indexFactory;
    private OptionFactory optionFactory;
    private StockFactory stockFactory;
    private WarrantFactory warrantFactory;
    private DefaultInstrumentFactory uut;

    @Before
    public void setUp() throws Exception {
        basketFactory = mock(BasketFactory.class);
        bondFactory = mock(BondFactory.class);
        forwardFactory = mock(ForwardFactory.class);
        futureFactory = mock(FutureFactory.class);
        indexFactory = mock(IndexFactory.class);
        optionFactory = mock(OptionFactory.class);
        stockFactory = mock(StockFactory.class);
        warrantFactory = mock(WarrantFactory.class);
        uut = new DefaultInstrumentFactory(basketFactory, bondFactory, forwardFactory, futureFactory, indexFactory,
                optionFactory, stockFactory, warrantFactory);
    }

    @Test
    public void shouldCreateBasket() throws Exception {
        uut.createBasket();

        verify(basketFactory).create();
    }

    @Test
    public void shouldCreateBond() throws Exception {
        uut.createBond();

        verify(bondFactory).create();
    }

    @Test
    public void shouldCreateForward() throws Exception {
        uut.createForward();

        verify(forwardFactory).create();
    }

    @Test
    public void shouldCreateFuture() throws Exception {
        uut.createFuture();

        verify(futureFactory).create();
    }

    @Test
    public void shouldCreateIndex() throws Exception {
        uut.createIndex();

        verify(indexFactory).create();
    }

    @Test
    public void shouldCreateOption() throws Exception {
        uut.createOption();

        verify(optionFactory).create();
    }

    @Test
    public void shouldCreateStock() throws Exception {
        uut.createStock();

        verify(stockFactory).create();
    }

    @Test
    public void shouldCreateWarrant() throws Exception {
        uut.createWarrant();

        verify(warrantFactory).create();
    }
}
