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
package com.zaradai.kunzite.trader.services.md;

import com.codahale.metrics.MetricRegistry;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.config.ConfigException;
import com.zaradai.kunzite.trader.config.md.ChannelConfig;
import com.zaradai.kunzite.trader.config.md.MarketDataConfiguration;
import com.zaradai.kunzite.trader.config.md.Subscription;
import com.zaradai.kunzite.trader.events.MarketData;
import com.zaradai.kunzite.trader.services.AbstractQueueBridge;
import com.zaradai.kunzite.trader.services.trader.DefaultTraderService;

import java.util.Map;

public class DefaultMarketDataService extends AbstractQueueBridge implements MarketDataService {
    static final String SERVICE_NAME = "Market Data Service";

    private MarketDataConfiguration configuration;
    private final MarketDataChannelFactory marketDataChannelFactory;
    private final DefaultTraderService traderService;
    private final Map<String, MarketDataChannel> channelByName;
    private final Map<String, ChannelConfig> channelConfigByName;
    private MappingManager mappingManager;
    @Inject
    DefaultMarketDataService(ContextLogger logger, MetricRegistry metricRegistry,
                             MarketDataChannelFactory marketDataChannelFactory, DefaultTraderService traderService) {
        super(logger, metricRegistry);
        this.marketDataChannelFactory = marketDataChannelFactory;
        this.traderService = traderService;
        channelByName = createChannelMap();
        channelConfigByName = createChannelConfigMap();
    }

    private Map<String, ChannelConfig> createChannelConfigMap() {
        return Maps.newHashMap();
    }

    private Map<String, MarketDataChannel> createChannelMap() {
        return Maps.newHashMap();
    }

    @Override
    public void onMarketData(MarketData marketData) {
        onEvent(marketData);
    }

    @Override
    public void build(MarketDataConfiguration configuration) throws ConfigException {
        this.configuration = configuration;
        loadChannelConfig();
        mappingManager = new MappingManager(configuration.getMappings());
    }

    private void loadChannelConfig() {
        for (ChannelConfig channelConfig : configuration.getChannels()) {
            channelConfigByName.put(channelConfig.getName(), channelConfig);
        }
    }

    @Override
    protected void startUp() throws Exception {
        for (Subscription subscription : configuration.getSubscriptions()) {
            subscribe(subscription);
        }
    }

    private void subscribe(Subscription subscription) {
        MarketDataChannel channel = getChannel(subscription.getChannel());

        if (channel != null) {
            // get the sid for given id and mapper
            String sid = mappingManager.getSid(subscription.getMap(), subscription.getId());
            // subscribe for market data
            channel.subscribe(sid);
        }
    }

    /**
     * Get the channel identified by channelName, lazily load the channel if not cached.
     * @param channelName
     * @return
     */
    private MarketDataChannel getChannel(String channelName) {
        MarketDataChannel res = channelByName.get(channelName);

        if (res == null) {
            res = loadChannel(channelName);
        }

        return res;
    }

    private MarketDataChannel loadChannel(String channelName) {
        ChannelConfig channelConfig = channelConfigByName.get(channelName);

        try {
            // create the channel
            String channelClazz = channelConfig.getClazz();
            MarketDataChannel res = marketDataChannelFactory.create(channelClazz);
            // start it up
            res.startAsync().awaitRunning();
            // add to the cache
            channelByName.put(channelName, res);
            // log running
            logChannelState(channelClazz, channelName, "running");
            // and return
            return res;
        } catch (Exception e) {
            LogHelper.error(getLogger())
                    .addContext("Market Data Service")
                    .addReason("Unable to load channel")
                    .add("Name", channelName)
                    .add("Clazz", channelConfig.getClazz())
                    .log();
        }

        return null;
    }

    private void logChannelState(String channelClazz, String channelName, String state) {
        LogHelper.info(getLogger())
                .add("MD Channel", channelName)
                .add("Class", channelClazz)
                .add("Is", state)
                .log();
    }

    @Override
    protected void shutDown() throws Exception {
        closeChannels();
    }

    private void closeChannels() {
        for (Map.Entry<String, MarketDataChannel> channelEntry : channelByName.entrySet()) {
            channelEntry.getValue().stopAsync().awaitTerminated();
            logChannelState(channelEntry.getValue().getClass().getName(), channelEntry.getKey(), "Stopped");
        }
    }

    @Override
    public void handleEvent(Object event) {
        MarketData marketData = (MarketData) event;
        // convert the incoming sid to internal instrument id
        String id = mappingManager.getId(marketData.getInstrumentId());

        if (!Strings.isNullOrEmpty(id)) {
            MarketData toProcess = MarketData.newInstance(id, marketData.getTimestamp(), marketData.getFields());
            // send to the trader
            traderService.onEvent(toProcess);
        }  else {
            LogHelper.warn(getLogger())
                    .addContext("Market Data")
                    .addReason("Unable to convert symbol")
                    .add("SID", marketData.getInstrumentId())
                    .log();
        }
    }

    @Override
    public String getName() {
        return SERVICE_NAME;
    }
}