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
package com.zaradai.kunzite.trader.algo;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.LogHelper;
import com.zaradai.kunzite.trader.control.TradingState;
import com.zaradai.kunzite.trader.events.*;
import com.zaradai.kunzite.trader.instruments.Instrument;
import com.zaradai.kunzite.trader.orders.model.OrderRequest;

import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GroupAlgo implements Algo {
    private final ContextLogger logger;
    private final String id;
    private final Map<String, Algo> algoByInstrument;

    @Inject
    GroupAlgo(ContextLogger logger, String id) {
        this.logger = logger;
        this.id = id;
        algoByInstrument = createAlgoMap();
    }

    protected Map<String, Algo> createAlgoMap() {
        return Maps.newHashMap();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public TradingState getState() {
        // no group state!!
        return null;
    }

    @Override
    public void setState(TradingState tradingState) {

    }

    @Override
    public void initialize() throws AlgoException {
        for (Algo algo : algoByInstrument.values()) {
            algo.initialize();
        }
    }

    public void add(Algo algo) {
        String id = getAlgoInstrumentId(algo);
        checkNotNull(algo, "Invalid algo");
        checkNotNull(algo.getState(), "Invalid algo state");
        checkNotNull(algo.getState().getInstrument(), "Invalid algo instrument");

        algoByInstrument.put(getAlgoInstrumentId(algo), algo);
    }

    private String getAlgoInstrumentId(Algo algo) {
        checkNotNull(algo, "Invalid algo");
        TradingState state = algo.getState();
        checkNotNull(state, "Invalid algo state");
        Instrument instrument = state.getInstrument();
        checkNotNull(instrument, "Invalid algo instrument");
        // finally does the instrument have a valid id
        String id = instrument.getId();
        checkArgument(!Strings.isNullOrEmpty(id));

        return id;
    }

    @Override
    public void start() throws AlgoException {
        for (Algo algo : algoByInstrument.values()) {
            algo.start();
        }
    }

    @Override
    public void stop() throws AlgoException {
        for (Algo algo : algoByInstrument.values()) {
            algo.stop();
        }
    }

    @Override
    public boolean isRunning() {
        for (Algo algo : algoByInstrument.values()) {
            if (!algo.isRunning()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onMarketBookUpdate(MarketBookUpdateEvent marketBookUpdateEvent) {
        // get the target instrument
        String id = marketBookUpdateEvent.getInstrumentId();
        // get the target algo
        Algo algo = algoByInstrument.get(id);

        if (algo != null) {
            algo.onMarketBookUpdate(marketBookUpdateEvent);
        } else {
            logEventForInstrumentNotInGroup(id);
        }
    }

    private void logEventForInstrumentNotInGroup(String id) {
        LogHelper.error(logger)
                .addContext("Algo Group")
                .addReason("Algo not found")
                .add("Instrument", id)
                .log();
    }

    @Override
    public void onReject(OrderRequest request) {
        String id = request.getInstrumentId();
        // get the target algo
        Algo algo = algoByInstrument.get(id);

        if (algo != null) {
            algo.onReject(request);
        } else {
            logEventForInstrumentNotInGroup(id);
        }
    }

    @Override
    public void onPositionInitiated(PositionInitiatedEvent event) {
        String id = event.getPosition().getInstrumentId();
        // get the target algo
        Algo algo = algoByInstrument.get(id);

        if (algo != null) {
            algo.onPositionInitiated(event);
        } else {
            logEventForInstrumentNotInGroup(id);
        }
    }

    @Override
    public void onPositionLiquidated(PositionLiquidatedEvent event) {
        String id = event.getPosition().getInstrumentId();
        // get the target algo
        Algo algo = algoByInstrument.get(id);

        if (algo != null) {
            algo.onPositionLiquidated(event);
        } else {
            logEventForInstrumentNotInGroup(id);
        }
    }

    @Override
    public void onPositionChanged(PositionChangedEvent event) {
        String id = event.getPosition().getInstrumentId();
        // get the target algo
        Algo algo = algoByInstrument.get(id);

        if (algo != null) {
            algo.onPositionChanged(event);
        } else {
            logEventForInstrumentNotInGroup(id);
        }
    }

    @Override
    public void onTrade(TradeEvent event) {
        String id = event.getInstrumentId();
        // get the target algo
        Algo algo = algoByInstrument.get(id);

        if (algo != null) {
            algo.onTrade(event);
        } else {
            logEventForInstrumentNotInGroup(id);
        }
    }
}
