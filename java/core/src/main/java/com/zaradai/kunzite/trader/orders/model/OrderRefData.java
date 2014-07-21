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
package com.zaradai.kunzite.trader.orders.model;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class OrderRefData {
    private String orderId;
    private String clientOrderId;
    private String instrumentId;
    private String portfolioId;
    private String marketId;
    private String brokerId;
    private String algoId;
    private String exchangeId;
    private final Map<String, String> fields;

    private OrderRefData() {
        fields = createFieldList();
    }

    private Map<String, String> createFieldList() {
        return Maps.newHashMap();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getBrokerId() {
        return brokerId;
    }

    public void setBrokerId(String brokerId) {
        this.brokerId = brokerId;
    }

    void addField(String key, String value) {
        fields.put(key, value);
    }

    String getField(String key) {
        return fields.get(key);
    }

    public String getAlgoId() {
        return algoId;
    }

    public void setAlgoId(String algoId) {
        this.algoId = algoId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public static OrderRefDataBuilder builder() {
        return new OrderRefDataBuilder();
    }

    public static class OrderRefDataBuilder {
        final OrderRefData refData = new OrderRefData();

        public OrderRefDataBuilder id(String id) {
            checkArgument(!Strings.isNullOrEmpty(id), "Invalid Order id");
            refData.setOrderId(id);
            return this;
        }

        public OrderRefDataBuilder instrument(String instrumentId) {
            checkArgument(!Strings.isNullOrEmpty(instrumentId), "Invalid Instrument id");
            refData.setInstrumentId(instrumentId);
            return this;
        }

        public OrderRefDataBuilder portfolio(String portfolioId) {
            checkArgument(!Strings.isNullOrEmpty(portfolioId), "Invalid Portfolio id");
            refData.setPortfolioId(portfolioId);
            return this;
        }

        public OrderRefDataBuilder client(String id) {
            checkArgument(!Strings.isNullOrEmpty(id), "Invalid Client id");
            refData.setClientOrderId(id);
            return this;
        }

        public OrderRefDataBuilder broker(String id) {
            checkArgument(!Strings.isNullOrEmpty(id), "Invalid Broker id");
            refData.setBrokerId(id);
            return this;
        }

        public OrderRefDataBuilder market(String id) {
            checkArgument(!Strings.isNullOrEmpty(id), "Invalid Market id");
            refData.setMarketId(id);
            return this;
        }

        public OrderRefDataBuilder algo(String id) {
            checkArgument(!Strings.isNullOrEmpty(id), "Invalid Algo id");
            refData.setAlgoId(id);
            return this;
        }

        public OrderRefData build() {
            return refData;
        }
    }
}
