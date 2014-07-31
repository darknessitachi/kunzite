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
package com.zaradai.kunzite.trader.tools;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.zaradai.kunzite.config.ConfigurationSource;
import com.zaradai.kunzite.config.InMemoryConfigurationSource;
import com.zaradai.kunzite.trader.services.md.eod.*;
import com.zaradai.kunzite.trader.services.md.eod.compact.CompactEodEncoder;
import com.zaradai.kunzite.trader.services.md.eod.compact.CompactEodReader;
import com.zaradai.kunzite.trader.services.md.eod.compact.CompactEodWriter;
import com.zaradai.kunzite.trader.services.md.eod.compact.EodEncoder;
import com.zaradai.kunzite.trader.services.md.eod.csv.CsvEodReader;
import com.zaradai.kunzite.trader.services.md.eod.csv.CsvEodWriter;
import com.zaradai.kunzite.trader.services.md.eod.mongo.MongoEodReader;
import com.zaradai.kunzite.trader.services.md.eod.mongo.MongoEodWriter;
import com.zaradai.kunzite.utils.FileIO;
import com.zaradai.kunzite.utils.SystemFileIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * A tool to support the conversion of one MD storage type to another.
 */
public class EodConverter {
    static final String TOOL_NAME = "eodconverter";
    static final String USAGE_HEADER = "Convert market data stored in a one format to another.";

    private static final Logger log = LoggerFactory.getLogger(TOOL_NAME);
    private Injector injector;

    public static void main(String[] args) {
        new EodConverter().run(args);
    }

    private void run(String[] args) {
        EodConverterOptionsParser parser = new EodConverterOptionsParser(new SystemFileIO());
        EodConverterOptions options = (EodConverterOptions) parser.parse(args);

        showHelpIfSpecified(parser, options);
        printOptions(options);

        if (options != null) {
            try {
                injector = Guice.createInjector(getModule(options));
                // create the completion service
                ExecutorService executorService = createExecutorService(options.getThreads());
                CompletionService<Void> completionService = new ExecutorCompletionService(executorService);
                // schedule all symbols to be converted
                scheduleConversions(options, completionService);
                // wait for each conversion to complete
                processConversions(options, completionService);
                // shutdown the executor
                executorService.shutdown();
            } catch (Exception e) {
                System.err.println("Unable to convert data");
                System.err.println(e);
                if (options.isDebug()) {
                    e.printStackTrace(System.err);
                }
                System.exit(1);
            }
        }
    }

    private void printOptions(EodConverterOptions options) {
        if (options != null && options.isDebug()) {
            System.out.println(options);
        }
    }

    private void showHelpIfSpecified(EodConverterOptionsParser parser, EodConverterOptions options) {
        // print usage and bail out if help specified
        if (options != null && options.isHelp()) {
            parser.printUsage();
            System.exit(1);
        };
    }

    private void processConversions(EodConverterOptions options, CompletionService<Void> completionService) {
        long processed = 0;
        for (int i = 0; i < options.getSymbols().size(); ++i) {
            try {
                completionService.take();
                processed++;
            } catch (Exception e) {
                System.err.println(e);
                if (options.isDebug()) {
                    e.printStackTrace(System.err);
                }
            }
        }
        System.out.println(String.format("Process %d conversions", processed));
    }

    private void scheduleConversions(EodConverterOptions options, CompletionService<Void> completionService) {
        // get the factories
        EodReaderFactory readerFactory = injector.getInstance(EodReaderFactory.class);
        EodWriterFactory writerFactory = injector.getInstance(EodWriterFactory.class);

        for (String symbol : options.getSymbols()) {
            completionService.submit(scheduleConversion(
                    readerFactory.create(options.getSourceFolder()),
                    writerFactory.create(options.getTargetFolder()),
                    symbol));
        }
    }

    private Callable<Void> scheduleConversion(final EodReader reader, final EodWriter writer, final String symbol) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                reader.open(symbol);
                writer.open(symbol);
                EodData data;

                while ((data = reader.getNext()) != null) {
                    writer.write(data);
                }

                reader.close();
                writer.close();

                return null;
            }
        };
    }

    private ExecutorService createExecutorService(int threads) {
        if (threads > 1) {
            return Executors.newFixedThreadPool(threads);
        } else {
            return Executors.newSingleThreadExecutor();
        }
    }

    private Module getModule(final EodConverterOptions options) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bindConfiguration();
                bindSourceReader(options.getSourceType());
                bindTargetWriter(options.getTargetType());
                bind(FileIO.class).to(SystemFileIO.class);
                bind(EodConverterOptionsParser.class);
            }

            private void bindConfiguration() {
                bind(ConfigurationSource.class).to(InMemoryConfigurationSource.class).asEagerSingleton();
            }

            private void bindTargetWriter(SupportedEodIO target) {
                switch (target) {
                    case Compact:
                        bindCompactTarget();
                        break;
                    case Csv:
                        bindCsvTarget();
                        break;
                    case Mongo:
                        bindMongoTarget();
                        break;
                }
            }

            private void bindSourceReader(SupportedEodIO source) {
                switch (source) {
                    case Compact:
                        bindCompactSource();
                        break;
                    case Csv:
                        bindCsvSource();
                        break;
                    case Mongo:
                        bindMongoSource();
                        break;
                }
            }

            private void bindCompactSource() {
                install(new FactoryModuleBuilder()
                        .implement(EodReader.class, CompactEodReader.class)
                        .build(EodReaderFactory.class));
                bind(EodEncoder.class).to(CompactEodEncoder.class);
            }

            private void bindCompactTarget() {
                install(new FactoryModuleBuilder()
                        .implement(EodWriter.class, CompactEodWriter.class)
                        .build(EodWriterFactory.class));
                bind(EodEncoder.class).to(CompactEodEncoder.class);
            }

            private void bindCsvSource() {
                install(new FactoryModuleBuilder()
                        .implement(EodReader.class, CsvEodReader.class)
                        .build(EodReaderFactory.class));
            }

            private void bindCsvTarget() {
                install(new FactoryModuleBuilder()
                        .implement(EodWriter.class, CsvEodWriter.class)
                        .build(EodWriterFactory.class));
            }

            private void bindMongoSource() {
                install(new FactoryModuleBuilder()
                        .implement(EodReader.class, MongoEodReader.class)
                        .build(EodReaderFactory.class));
            }

            private void bindMongoTarget() {
                install(new FactoryModuleBuilder()
                        .implement(EodWriter.class, MongoEodWriter.class)
                        .build(EodWriterFactory.class));
            }
        };
    }
}
