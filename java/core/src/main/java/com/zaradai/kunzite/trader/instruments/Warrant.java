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

public class Warrant extends Option {
    private double conversionRatio;
    private long issueSize;
    private String issuer;
    private DateTime issueDate;

    @Inject
    Warrant(InstrumentResolver instrumentResolver) {
        super(instrumentResolver);
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

    @Override
    public InstrumentType getType() {
        return InstrumentType.Warrant;
    }
}
