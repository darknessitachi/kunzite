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

import com.zaradai.kunzite.trader.events.MarketBookUpdate;
import com.zaradai.kunzite.trader.events.MarketData;
import com.zaradai.kunzite.trader.events.MarketDataField;

public class DefaultMarketBookUpdater implements MarketBookUpdater {
    @Override
    public MarketBookUpdate update(MarketBook book, MarketData marketData) {
        boolean bestPriceChanged = false;
        boolean bestSizeChanged = false;
        boolean depthChanged = false;
        boolean lastTradeUpdated = false;
        boolean ohlcChanged = false;

        for (MarketDataField field : marketData.getFields()) {
            switch (field.getType()) {
                case BEST_BID:
                    book.setPrice(Side.Bid, 0, field.getDoubleValue());
                    bestPriceChanged = true;
                    break;
                case BEST_BID2:
                    book.setPrice(Side.Bid, 1, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID3:
                    book.setPrice(Side.Bid, 2, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID4:
                    book.setPrice(Side.Bid, 3, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID5:
                    book.setPrice(Side.Bid, 4, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID6:
                    book.setPrice(Side.Bid, 5, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID7:
                    book.setPrice(Side.Bid, 6, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID8:
                    book.setPrice(Side.Bid, 7, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID9:
                    book.setPrice(Side.Bid, 8, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_BID10:
                    book.setPrice(Side.Bid, 9, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK:
                    book.setPrice(Side.Ask, 0, field.getDoubleValue());
                    bestPriceChanged = true;
                    break;
                case BEST_ASK2:
                    book.setPrice(Side.Ask, 1, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK3:
                    book.setPrice(Side.Ask, 2, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK4:
                    book.setPrice(Side.Ask, 3, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK5:
                    book.setPrice(Side.Ask, 4, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK6:
                    book.setPrice(Side.Ask, 5, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK7:
                    book.setPrice(Side.Ask, 6, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK8:
                    book.setPrice(Side.Ask, 7, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK9:
                    book.setPrice(Side.Ask, 8, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BEST_ASK10:
                    book.setPrice(Side.Ask, 9, field.getDoubleValue());
                    depthChanged = true;
                    break;
                case BID_SIZE:
                    book.setSize(Side.Bid, 0, field.getLongValue());
                    bestSizeChanged = true;
                    break;
                case BID2_SIZE:
                    book.setSize(Side.Bid, 1, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID3_SIZE:
                    book.setSize(Side.Bid, 2, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID4_SIZE:
                    book.setSize(Side.Bid, 3, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID5_SIZE:
                    book.setSize(Side.Bid, 4, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID6_SIZE:
                    book.setSize(Side.Bid, 5, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID7_SIZE:
                    book.setSize(Side.Bid, 6, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID8_SIZE:
                    book.setSize(Side.Bid, 7, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID9_SIZE:
                    book.setSize(Side.Bid, 8, field.getLongValue());
                    depthChanged = true;
                    break;
                case BID10_SIZE:
                    book.setSize(Side.Bid, 9, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK_SIZE:
                    book.setSize(Side.Ask, 0, field.getLongValue());
                    bestSizeChanged = true;
                    break;
                case ASK2_SIZE:
                    book.setSize(Side.Ask, 1, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK3_SIZE:
                    book.setSize(Side.Ask, 2, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK4_SIZE:
                    book.setSize(Side.Ask, 3, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK5_SIZE:
                    book.setSize(Side.Ask, 4, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK6_SIZE:
                    book.setSize(Side.Ask, 5, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK7_SIZE:
                    book.setSize(Side.Ask, 6, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK8_SIZE:
                    book.setSize(Side.Ask, 7, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK9_SIZE:
                    book.setSize(Side.Ask, 8, field.getLongValue());
                    depthChanged = true;
                    break;
                case ASK10_SIZE:
                    book.setSize(Side.Ask, 9, field.getLongValue());
                    depthChanged = true;
                    break;
                case TRADE_PRICE:
                    book.getLastTrade().setPrice(field.getDoubleValue());
                    lastTradeUpdated = true;
                    break;
                case TRADE_SIZE:
                    book.getLastTrade().setSize(field.getLongValue());
                    lastTradeUpdated = true;
                    break;
                case PREV_CLOSE:
                    book.setPrevClose(field.getDoubleValue());
                    ohlcChanged = true;
                    break;
                case OPEN:
                    book.setOpen(field.getDoubleValue());
                    ohlcChanged = true;
                    break;
                case HIGH:
                    book.setHigh(field.getDoubleValue());
                    ohlcChanged = true;
                    break;
                case LOW:
                    book.setLow(field.getDoubleValue());
                    ohlcChanged = true;
                    break;
            }
        }

        return MarketBookUpdate.builder()
                .instrument(book.getInstrumentId())
                .book(book)
                .bestPrice(bestPriceChanged)
                .bestSize(bestSizeChanged)
                .depth(depthChanged)
                .lastTrade(lastTradeUpdated)
                .ohlc(ohlcChanged)
                .build();
    }
}
