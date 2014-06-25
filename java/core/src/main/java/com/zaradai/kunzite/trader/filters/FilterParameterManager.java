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
package com.zaradai.kunzite.trader.filters;

import java.util.Set;

/**
 * Provides configured limits to the trading engine
 */
public interface FilterParameterManager {
    long getMaxLong(FilterRequest filterRequest);
    double getMaxNotional(FilterRequest filterRequest);
    long getMaxQuantity(FilterRequest filterRequest);
    long getMaxShort(FilterRequest filterRequest);
    double getMaxSpread(FilterRequest filterRequest);
    double getMaxPrice(FilterRequest filterRequest);
    double getMinPrice(FilterRequest filterRequest);
    boolean allowShort(FilterRequest filterRequest);
    Set<String> getRestrictedList(FilterRequest filterRequest);
}
