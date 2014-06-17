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
package com.zaradai.kunzite.optimizer;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ehcache.InstrumentedEhcache;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.zaradai.kunzite.config.ConfigurationSource;
import com.zaradai.kunzite.config.InMemoryConfigurationSource;
import com.zaradai.kunzite.optimizer.config.OptimizerConfiguration;
import com.zaradai.kunzite.optimizer.config.OptimizerConfigurationImpl;
import com.zaradai.kunzite.optimizer.control.DefaultOptimizerController;
import com.zaradai.kunzite.optimizer.control.OptimizeController;
import com.zaradai.kunzite.optimizer.control.OptimizeControllerFactory;
import com.zaradai.kunzite.optimizer.data.DataManager;
import com.zaradai.kunzite.optimizer.data.DataManagerFactory;
import com.zaradai.kunzite.optimizer.data.DataRequestManager;
import com.zaradai.kunzite.optimizer.data.DataRequester;
import com.zaradai.kunzite.optimizer.data.DataRequesterFactory;
import com.zaradai.kunzite.optimizer.data.DefaultDataManager;
import com.zaradai.kunzite.optimizer.data.DisruptorDataRequestManager;
import com.zaradai.kunzite.optimizer.data.DisruptorDataRequester;
import com.zaradai.kunzite.optimizer.data.dataset.DataSet;
import com.zaradai.kunzite.optimizer.data.dataset.DataSetFactory;
import com.zaradai.kunzite.optimizer.data.dataset.DefaultDataSet;
import com.zaradai.kunzite.optimizer.data.dataset.cache.DataCache;
import com.zaradai.kunzite.optimizer.data.dataset.cache.dummy.DummyDataCache;
import com.zaradai.kunzite.optimizer.data.dataset.cache.ehcache.EhcacheDataCache;
import com.zaradai.kunzite.optimizer.data.dataset.cache.memory.InMemoryDataCache;
import com.zaradai.kunzite.optimizer.data.dataset.driver.DataSetDatabase;
import com.zaradai.kunzite.optimizer.data.dataset.driver.dummy.DummyDataSetDatabase;
import com.zaradai.kunzite.optimizer.data.dataset.driver.memory.InMemoryDataSetDatabase;
import com.zaradai.kunzite.optimizer.data.matrix.CachingMatrixManager;
import com.zaradai.kunzite.optimizer.data.matrix.MatrixManager;
import com.zaradai.kunzite.optimizer.data.matrix.MatrixManagerFactory;
import com.zaradai.kunzite.optimizer.eval.CalcEngine;
import com.zaradai.kunzite.optimizer.eval.CalcEngineFactory;
import com.zaradai.kunzite.optimizer.eval.DynamicEvaluatorFactory;
import com.zaradai.kunzite.optimizer.eval.EvaluatorFactory;
import com.zaradai.kunzite.optimizer.eval.local.MultiThreadedCalcEngine;
import com.zaradai.kunzite.optimizer.eval.local.SimpleCalcEngine;
import com.zaradai.kunzite.optimizer.tactic.DynamicTacticFactory;
import com.zaradai.kunzite.optimizer.tactic.OptimizerTacticFactory;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

public class OptimizerModule extends AbstractModule {
    private static MetricRegistry metrics = new MetricRegistry();

    private final CacheStrategy cacheStrategy;
    private final DataStrategy dataStrategy;
    private final EvaluationStrategy evaluationStrategy;

    public OptimizerModule(CacheStrategy cacheStrategy, DataStrategy dataStrategy,
                           EvaluationStrategy evaluationStrategy) {
        this.cacheStrategy = cacheStrategy;
        this.dataStrategy = dataStrategy;
        this.evaluationStrategy = evaluationStrategy;
    }

    @Override
    protected void configure() {
        bindConfiguration();
        bindMetrics();
        bindDataStore();
        bindDataFlow();
        bindEvaluations();
        bindCore();
    }

    private void bindCore() {
        bind(OptimizerTacticFactory.class).to(DynamicTacticFactory.class);
        bind(OptimizerService.class).to(Optimizer.class).in(Singleton.class);
        install(new FactoryModuleBuilder().implement(OptimizeController.class,
                DefaultOptimizerController.class).build(OptimizeControllerFactory.class));
    }

    private void bindEvaluations() {
        bind(EvaluatorFactory.class).to(DynamicEvaluatorFactory.class);

        switch (evaluationStrategy) {
            case LocalSingleThreaded:
            case Remote:
            default:
                install(new FactoryModuleBuilder().implement(CalcEngine.class, SimpleCalcEngine.class)
                        .build(CalcEngineFactory.class));
                break;
            case LocalMultiThreaded:
                install(new FactoryModuleBuilder().implement(CalcEngine.class, MultiThreadedCalcEngine.class)
                        .build(CalcEngineFactory.class));
                break;
        }
    }

    private void bindDataFlow() {
        install(new FactoryModuleBuilder().implement(DataManager.class, DefaultDataManager.class)
                .build(DataManagerFactory.class));
        install(new FactoryModuleBuilder().implement(DataRequester.class, DisruptorDataRequester.class)
                .build(DataRequesterFactory.class));
        install(new FactoryModuleBuilder().implement(MatrixManager.class, CachingMatrixManager.class)
                .build(MatrixManagerFactory.class));
        bind(DataRequestManager.class).to(DisruptorDataRequestManager.class).in(Singleton.class);
    }

    private void bindDataStore() {
        install(new FactoryModuleBuilder().implement(DataSet.class, DefaultDataSet.class).build(DataSetFactory.class));

        bindCache();
        bindDataBase();
    }

    private void bindDataBase() {
        switch (dataStrategy) {
            case None:
            case Mongo:
            default:
                bind(DataSetDatabase.class).to(DummyDataSetDatabase.class).in(Singleton.class);
                //bind(DataSetDriver.class).to(DummyDataSetDriver.class);
                break;
            case Mem:
                bind(DataSetDatabase.class).to(InMemoryDataSetDatabase.class).in(Singleton.class);
                //bind(DataSetDriver.class).to(InMemoryDataSetDriver.class);
                break;
        }
    }

    private void bindCache() {
        switch (cacheStrategy) {
            case None:
            default:
                bind(DataCache.class).to(DummyDataCache.class);
                break;
            case Mem:
                bind(DataCache.class).to(InMemoryDataCache.class);
                break;
            case Ehcache:
                bindEhcache();
                break;
        }
    }

    private void bindEhcache() {
        Ehcache c = CacheManager.getInstance().addCacheIfAbsent("Optimizer");
        Ehcache cache = InstrumentedEhcache.instrument(metrics, c);
        bind(DataCache.class).to(EhcacheDataCache.class);
        bind(Ehcache.class).toInstance(cache);
    }

    private void bindMetrics() {
        bind(MetricRegistry.class).toInstance(metrics);
    }

    private void bindConfiguration() {
        bind(ConfigurationSource.class).to(InMemoryConfigurationSource.class).asEagerSingleton();
        bind(OptimizerConfiguration.class).to(OptimizerConfigurationImpl.class);
    }
}
