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
package com.zaradai.kunzite.trader.config.statics;

import com.google.common.collect.Lists;
import com.zaradai.kunzite.trader.instruments.InstrumentType;
import com.zaradai.kunzite.trader.instruments.OptionType;
import org.joda.time.DateTime;

import java.util.List;

public final class InstrumentConfig {
    public InstrumentConfig() {
        members = Lists.newArrayList();
        partOf = Lists.newArrayList();
        basketConstituents = Lists.newArrayList();
    }

    private String id;
    private String name;
    private double multiplier;
    private int lotSize;
    private String marketId;
    private InstrumentType type;
    // membership
    private List<String> members;
    private List<String> partOf;
    // Basket
    private List<String> basketConstituents;
    // Bond
    private DateTime bondIssue;
    private DateTime bondMaturity;
    private DateTime bondFirstCoupon;
    private double bondCoupon;
    private double bondNotional;
    //derivative
    private String underlyingId;
    private DateTime maturity;
    // options
    private OptionType optionType;
    private double strike;
    // warrant
    private double conversionRatio;
    private long issueSize;
    private String issuer;
    private DateTime issueDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public InstrumentType getType() {
        return type;
    }

    public void setType(InstrumentType type) {
        this.type = type;
    }

    public Iterable<String> getMembers() {
        return members;
    }

    public void addMember(String member) {
        members.add(member);
    }

    public Iterable<String> getPartOf() {
        return partOf;
    }

    public void addPartOf(String of) {
        this.partOf.add(of);
    }

    public Iterable<String> getBasketConstituents() {
        return basketConstituents;
    }

    public void addBasketConstituent(String constituent) {
        basketConstituents.add(constituent);
    }

    public DateTime getBondIssue() {
        return bondIssue;
    }

    public void setBondIssue(DateTime bondIssue) {
        this.bondIssue = bondIssue;
    }

    public DateTime getBondMaturity() {
        return bondMaturity;
    }

    public void setBondMaturity(DateTime bondMaturity) {
        this.bondMaturity = bondMaturity;
    }

    public DateTime getBondFirstCoupon() {
        return bondFirstCoupon;
    }

    public void setBondFirstCoupon(DateTime bondFirstCoupon) {
        this.bondFirstCoupon = bondFirstCoupon;
    }

    public double getBondCoupon() {
        return bondCoupon;
    }

    public void setBondCoupon(double bondCoupon) {
        this.bondCoupon = bondCoupon;
    }

    public double getBondNotional() {
        return bondNotional;
    }

    public void setBondNotional(double bondNotional) {
        this.bondNotional = bondNotional;
    }

    public String getUnderlyingId() {
        return underlyingId;
    }

    public void setUnderlyingId(String underlyingId) {
        this.underlyingId = underlyingId;
    }

    public DateTime getMaturity() {
        return maturity;
    }

    public void setMaturity(DateTime maturity) {
        this.maturity = maturity;
    }

    public OptionType getOptionType() {
        return optionType;
    }

    public void setOptionType(OptionType optionType) {
        this.optionType = optionType;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public double getConversionRatio() {
        return conversionRatio;
    }

    public void setConversionRatio(double conversionRatio) {
        this.conversionRatio = conversionRatio;
    }

    public long getIssueSize() {
        return issueSize;
    }

    public void setIssueSize(long issueSize) {
        this.issueSize = issueSize;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public DateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(DateTime issueDate) {
        this.issueDate = issueDate;
    }
}
