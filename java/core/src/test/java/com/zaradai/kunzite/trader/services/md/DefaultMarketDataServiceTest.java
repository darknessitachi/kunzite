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

import com.google.common.collect.Lists;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.trader.config.md.*;
import com.zaradai.kunzite.trader.events.MarketData;
import com.zaradai.kunzite.trader.events.MarketDataField;
import com.zaradai.kunzite.trader.mocks.ContextLoggerMocker;
import com.zaradai.kunzite.trader.services.trader.TraderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class DefaultMarketDataServiceTest {
    private static final String CHANNEL_1_NAME = "chan_name";
    private static final String CHANNEL_1_CLASS = "chan_class";
    private static final String MAP_NAME = "map_name";
    private static final String TEST_SID = "sid";
    private static final String TEST_ID = "id";

    private MarketDataConfiguration configuration;
    private ContextLogger logger;
    private MarketDataChannelFactory marketDataChannelFactory;
    private TraderService traderService;
    private DefaultMarketDataService uut;
    private MarketDataChannel channel;
    @Captor
    ArgumentCaptor<Object> argumentCaptor;
    @Mock
    BlockingQueue<Object> mockQueue;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        logger = ContextLoggerMocker.create();
        marketDataChannelFactory = mock(MarketDataChannelFactory.class);
        traderService = mock(TraderService.class);
        configuration = createConfiguration();
        channel = mock(MarketDataChannel.class);
        when(channel.startAsync()).thenReturn(channel);
        when(channel.stopAsync()).thenReturn(channel);
        uut = new DefaultMarketDataService(logger, marketDataChannelFactory, traderService, configuration);
    }

    private MarketDataConfiguration createConfiguration() {
        MarketDataConfiguration res = new MarketDataConfiguration();
        ChannelConfig channelConfig = new ChannelConfig();
        channelConfig.setName(CHANNEL_1_NAME);
        channelConfig.setClazz(CHANNEL_1_CLASS);
        res.add(channelConfig);
        MappingConfig mappingConfig = new MappingConfig();
        mappingConfig.setName(MAP_NAME);
        MappingValue mappingValue = new MappingValue();
        mappingValue.setSid(TEST_SID);
        mappingValue.setId(TEST_ID);
        mappingConfig.add(mappingValue);
        res.add(mappingConfig);
        Subscription subscription = new Subscription();
        subscription.setMap(MAP_NAME);
        subscription.setChannel(CHANNEL_1_NAME);
        subscription.setId(TEST_ID);
        res.add(subscription);

        return res;
    }

    @Test
    public void shouldLoadChannelAndSubscribeOnStartup() throws Exception {
        when(marketDataChannelFactory.create(CHANNEL_1_CLASS)).thenReturn(channel);

        uut.startUp();

        verify(channel).startAsync();
        verify(channel).awaitRunning();
        verify(channel).subscribe(TEST_SID);
    }

    @Test
    public void shouldLogIfUnableToLoadChannel() throws Exception {
        doThrow(MarketDataException.class).when(marketDataChannelFactory).create(CHANNEL_1_CLASS);

        uut.startUp();

        verify(logger).error();
        verify(channel, never()).subscribe(TEST_SID);
    }

    @Test
    public void shouldCloseStartedChannelsOnShutdown() throws Exception {
        when(marketDataChannelFactory.create(CHANNEL_1_CLASS)).thenReturn(channel);
        uut.startUp();

        uut.shutDown();

        verify(channel).stopAsync();
        verify(channel).awaitTerminated();
    }

    @Test
    public void shouldGetServiceName() throws Exception {
        assertThat(uut.getName(), is(DefaultMarketDataService.SERVICE_NAME));
    }

    @Test
    public void shouldMapIdsOnHandlingSubscribedMarketDataEvent() throws Exception {
        when(marketDataChannelFactory.create(CHANNEL_1_CLASS)).thenReturn(channel);
        List<MarketDataField> fields = Lists.newArrayList();
        MarketData marketData = MarketData.newInstance(TEST_SID, fields);
        uut.startUp();

        uut.handleEvent(marketData);

        verify(traderService).onEvent(argumentCaptor.capture());

        MarketData res = (MarketData) argumentCaptor.getValue();

        assertThat(res.getInstrumentId(), is(TEST_ID));
    }

    @Test
    public void shouldWarnOnHandlingUnknownSid() throws Exception {
        List<MarketDataField> fields = Lists.newArrayList();
        MarketData marketData = MarketData.newInstance("Unknown", fields);

        uut.handleEvent(marketData);

        verify(logger).warn();
    }

    @Test
    public void shouldQueueMarketDataFromChannel() throws Exception {
        List<MarketDataField> fields = Lists.newArrayList();
        MarketData marketData = MarketData.newInstance(TEST_ID, fields);
        uut = new DefaultMarketDataService(logger, marketDataChannelFactory, traderService, configuration) {
            @Override
            protected BlockingQueue<Object> createQueue() {
                return mockQueue;
            }
        };

        uut.onEvent(marketData);

        verify(mockQueue).put(marketData);
    }
}
