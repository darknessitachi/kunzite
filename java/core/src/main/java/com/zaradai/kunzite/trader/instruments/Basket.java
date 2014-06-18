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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Basket extends MembershipInstrument {
    private final List<Instrument> constituents;

    @Inject
    Basket(InstrumentResolver instrumentResolver) {
        super(instrumentResolver);
        constituents = createConstituentList();
    }

    private List<Instrument> createConstituentList() {
        return Lists.newArrayList();
    }

    public List<Instrument> getConstituents() {
        return ImmutableList.copyOf(constituents);
    }

    public void addToBasket(Instrument instrument) {
        checkNotNull(instrument, INVALID_INSTRUMENT_SPECIFIED);

        constituents.add(instrument);
    }

    public void addToBasket(String instrumentId) {
        addToBasket(getInstrument(instrumentId));
    }

    @Override
    public InstrumentType getType() {
        return InstrumentType.Basket;
    }
}
