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
package com.zaradai.kunzite.optimizer.data.dataset.driver;

import com.zaradai.kunzite.optimizer.data.dataset.DataSetContext;

import java.util.List;

public interface DataSetDatabase {
    /**
     * Open a connection to the data store client
     * @throws DriverException
     */
    void connect() throws DriverException;

    /**
     * Close all active connections
     */
    void close();

    /**
     * Manage the dataset contexts
     * @param context
     */
    void addOrUpdate(DataSetContext context);
    List<DataSetContext> getAllContexts();

    /**
     * Opens a connection to the data set specified by the context
     * @param context
     * @return
     */
    DataSetDriver open(DataSetContext context);
}
