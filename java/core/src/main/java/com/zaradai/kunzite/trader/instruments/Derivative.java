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
import org.joda.time.DateTime;

public abstract class Derivative extends MembershipInstrument {
    private String underlyingId;
    private DateTime maturity;

    @Inject
    Derivative(InstrumentResolver instrumentResolver) {
        super(instrumentResolver);
    }

    public String getUnderlyingId() {
        return underlyingId;
    }

    public void setUnderlyingId(String underlyingId) {
        this.underlyingId = underlyingId;
    }

    public MembershipInstrument getUnderlying() {
        return getInstrument(underlyingId);
    }

    public DateTime getMaturity() {
        return maturity;
    }

    public void setMaturity(DateTime maturity) {
        this.maturity = maturity;
    }
}
