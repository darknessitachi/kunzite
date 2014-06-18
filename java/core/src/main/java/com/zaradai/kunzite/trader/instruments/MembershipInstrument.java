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

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Instruments can be members of other Instruments such as the stocks within an index, whilst
 * others like Indexes will have members.  This class is used to manage that linkage.
 */
public abstract class MembershipInstrument extends Instrument {
    public static final String INVALID_INSTRUMENT_SPECIFIED = "Invalid instrument specified";
    public static final String INVALID_INSTRUMENT_ID_SPECIFIED = "Invalid instrument ID specified";

    private final List<MembershipInstrument> members;
    private final List<MembershipInstrument> partOf;
    private final InstrumentResolver instrumentResolver;

    protected MembershipInstrument(InstrumentResolver instrumentResolver) {
        this.instrumentResolver = instrumentResolver;
        partOf = createPartsList();
        members = createMembersList();
    }

    protected List<MembershipInstrument> createMembersList() {
        return Lists.newArrayList();
    }

    protected List<MembershipInstrument> createPartsList() {
        return Lists.newArrayList();
    }

    protected void addTo(MembershipInstrument instrument) {
        checkNotNull(instrument, INVALID_INSTRUMENT_SPECIFIED);

        instrument.add(this);
        partOf.add(instrument);
    }

    protected void addTo(String instrumentId) {
        addTo(getInstrument(instrumentId));
    }

    protected void add(MembershipInstrument instrument) {
        checkNotNull(instrument, INVALID_INSTRUMENT_SPECIFIED);

        members.add(instrument);
    }

    protected void add(String instrumentId) {
        add(getInstrument(instrumentId));
    }

    public boolean isMemberOfAny() {
        return partOf.size() > 0;
    }

    public boolean isMemberOf(MembershipInstrument instrument) {
        checkNotNull(instrument, INVALID_INSTRUMENT_SPECIFIED);

        return instrument.hasMember(this);
    }

    public boolean isMemberOf(String instrumentId) {
        return isMemberOf(getInstrument(instrumentId));
    }

    public boolean hasMember(MembershipInstrument instrument) {
        checkNotNull(instrument, INVALID_INSTRUMENT_SPECIFIED);

        return members.contains(instrument);
    }

    public boolean hasMember(String instrumentId) {
        return hasMember(getInstrument(instrumentId));
    }

    public List<MembershipInstrument> getMembers() {
        return ImmutableList.copyOf(members);
    }

    protected MembershipInstrument getInstrument(String instrumentId) {
        checkArgument(!Strings.isNullOrEmpty(instrumentId), INVALID_INSTRUMENT_ID_SPECIFIED);

        return (MembershipInstrument) instrumentResolver.resolveInstrument(instrumentId);
    }
}
