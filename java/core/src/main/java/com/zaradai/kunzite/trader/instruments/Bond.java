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

public class Bond extends MembershipInstrument {
    private DateTime issue;
    private DateTime maturity;
    private DateTime firstCoupon;
    private double coupon;
    private double notional;

    @Inject
    Bond(InstrumentResolver instrumentResolver) {
        super(instrumentResolver);
    }

    public DateTime getFirstCoupon() {
        return firstCoupon;
    }

    public void setFirstCoupon(DateTime firstCoupon) {
        this.firstCoupon = firstCoupon;
    }

    public double getCoupon() {
        return coupon;
    }

    public void setCoupon(double coupon) {
        this.coupon = coupon;
    }

    public double getNotional() {
        return notional;
    }

    public void setNotional(double notional) {
        this.notional = notional;
    }

    public DateTime getIssue() {
        return issue;
    }

    public void setIssue(DateTime issue) {
        this.issue = issue;
    }

    public DateTime getMaturity() {
        return maturity;
    }

    public void setMaturity(DateTime maturity) {
        this.maturity = maturity;
    }

    @Override
    public InstrumentType getType() {
        return InstrumentType.Bond;
    }
}
