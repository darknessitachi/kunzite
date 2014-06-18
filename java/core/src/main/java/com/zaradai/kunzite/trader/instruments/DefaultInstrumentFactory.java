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

import com.google.inject.Inject;

public class DefaultInstrumentFactory implements InstrumentFactory {
    private final BasketFactory basketFactory;
    private final BondFactory bondFactory;
    private final ForwardFactory forwardFactory;
    private final FutureFactory futureFactory;
    private final IndexFactory indexFactory;
    private final OptionFactory optionFactory;
    private final StockFactory stockFactory;
    private final WarrantFactory warrantFactory;

    @Inject
    public DefaultInstrumentFactory(BasketFactory basketFactory, BondFactory bondFactory, ForwardFactory forwardFactory,
                                    FutureFactory futureFactory, IndexFactory indexFactory, OptionFactory optionFactory,
                                    StockFactory stockFactory, WarrantFactory warrantFactory) {
        this.basketFactory = basketFactory;
        this.bondFactory = bondFactory;
        this.forwardFactory = forwardFactory;
        this.futureFactory = futureFactory;
        this.indexFactory = indexFactory;
        this.optionFactory = optionFactory;
        this.stockFactory = stockFactory;
        this.warrantFactory = warrantFactory;
    }

    @Override
    public Basket createBasket() {
        return basketFactory.create();
    }

    @Override
    public Bond createBond() {
        return bondFactory.create();
    }

    @Override
    public Forward createForward() {
        return forwardFactory.create();
    }

    @Override
    public Future createFuture() {
        return futureFactory.create();
    }

    @Override
    public Index createIndex() {
        return indexFactory.create();
    }

    @Override
    public Option createOption() {
        return optionFactory.create();
    }

    @Override
    public Stock createStock() {
        return stockFactory.create();
    }

    @Override
    public Warrant createWarrant() {
        return warrantFactory.create();
    }
}
