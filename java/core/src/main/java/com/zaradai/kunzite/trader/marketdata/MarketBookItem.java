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
package com.zaradai.kunzite.trader.marketdata;

public final class MarketBookItem {
    private long size;
    private double price;

    public MarketBookItem() {
    }

    public MarketBookItem(long size, double price) {
        this.size = size;
        this.price = price;
    }

    public long getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
