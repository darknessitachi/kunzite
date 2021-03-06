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
import com.zaradai.kunzite.trader.services.md.eod.EodData;
import com.zaradai.kunzite.trader.services.md.eod.EodWriter;
import com.zaradai.kunzite.trader.services.md.eod.EodWriterFactory;
import com.zaradai.kunzite.trader.services.md.eod.SupportedEodIO;
import com.zaradai.kunzite.trader.services.md.eod.compact.CompactEodEncoder;
import com.zaradai.kunzite.trader.services.md.eod.compact.CompactEodWriter;
import com.zaradai.kunzite.trader.services.md.eod.compact.EodEncoder;
import com.zaradai.kunzite.trader.services.md.eod.csv.CsvEodWriter;
import com.zaradai.kunzite.trader.services.md.eod.download.EodDataDownloader;
import com.zaradai.kunzite.trader.services.md.eod.download.yahoo.QuoteUrlFormatter;
import com.zaradai.kunzite.trader.services.md.eod.download.yahoo.YahooHistoricalDownloader;
import com.zaradai.kunzite.trader.services.md.eod.download.yahoo.YahooQuoteUrlFormatter;
import com.zaradai.kunzite.trader.services.md.eod.mongo.MongoEodWriter;
import com.zaradai.kunzite.utils.FileIO;
import com.zaradai.kunzite.utils.SystemFileIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class EodDownload {
    public static final String TOOL_NAME = "eoddownload";
    public static final String USAGE_HEADER = "Download market data and store";

    private static final Logger log = LoggerFactory.getLogger(TOOL_NAME);

    private Injector injector;

    public static void main(String[] args) {
        new EodDownload().run(args);
    }

    private void run(String[] args) {
        EodDownloadOptionsParser parser = new EodDownloadOptionsParser(new SystemFileIO());
        EodDownloadOptions options = (EodDownloadOptions) parser.parse(args);

        showHelpIfSpecified(parser, options);
        printOptions(options);

        if (options != null) {
            try {
                injector = Guice.createInjector(getModule(options));
                // create the completion service
                ExecutorService executorService = createExecutorService(options.getThreads());
                CompletionService<Void> completionService = new ExecutorCompletionService(executorService);
                // schedule all symbols to be downloaded
                scheduleDownloads(options, completionService);
                // wait for the downloads to complete
                processDownloads(options, completionService);
                // shutdown the executor
                executorService.shutdown();
            } catch (Exception e) {
                System.err.println("Unable to download data");
                printError(options, e);
                System.exit(1);
            }
        }
    }

    private void processDownloads(EodDownloadOptions options, CompletionService<Void> completionService) {
        long processed = 0;
        for (int i = 0; i < options.getSymbols().size(); ++i) {
            try {
                completionService.take();
                processed++;
            } catch (Exception e) {
                printError(options, e);
            }
        }
        System.out.println(String.format("Process %d downloads", processed));
    }

    private void scheduleDownloads(EodDownloadOptions options, CompletionService<Void> completionService) {
        EodWriterFactory writerFactory = injector.getInstance(EodWriterFactory.class);

        try {
            for (String symbol : options.getSymbols()) {
                completionService.submit(scheduleDownload(
                        writerFactory.create(options.getTargetFolder()),
                        symbol,
                        options)
                );
            }
        } catch (Exception e) {
            printError(options, e);
        }
    }

    private void printError(EodDownloadOptions options, Exception e) {
        System.err.println(e);
        if (options.isDebug()) {
            e.printStackTrace(System.err);
        }
    }

    private Callable<Void> scheduleDownload(final EodWriter writer, final String symbol,
                                            final EodDownloadOptions options) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    long start = System.nanoTime();
                    EodDataDownloader downloader = injector.getInstance(EodDataDownloader.class);
                    // download the data
                    List<EodData> data = downloader.download(symbol, options.getFrom(), options.getUntil());
                    // sort data by date
                    Collections.sort(data);
                    // Open a writer
                    writer.open(symbol);
                    // encode and write to requested output
                    for (EodData eodData : data) {
                        writer.write(eodData);
                    }
                    // close and flush
                    writer.close();
                    long end = System.nanoTime();

                    if (options.isDebug()) {
                        System.out.println(String.format("%s: Download %d items in %d mS", symbol, data.size(),
                                TimeUnit.NANOSECONDS.toMillis(end-start)));
                    }
                } catch (Exception e) {
                    printError(options, e);
                }

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

    private void printOptions(EodDownloadOptions options) {
        if (options != null && options.isDebug()) {
            System.out.println(options);
        }
    }

    private void showHelpIfSpecified(EodDownloadOptionsParser parser, EodDownloadOptions options) {
        // print usage and bail out if help specified
        if (options != null && options.isHelp()) {
            parser.printUsage();
            System.exit(1);
        };
    }

    private Module getModule(final EodDownloadOptions options) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bindConfiguration();
                bindTargetWriter(options.getTargetType());
                bind(FileIO.class).to(SystemFileIO.class);
                bind(EodDownloadOptionsParser.class);
                bind(EodDataDownloader.class).to(YahooHistoricalDownloader.class);
                bind(QuoteUrlFormatter.class).to(YahooQuoteUrlFormatter.class);
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

            private void bindCompactTarget() {
                install(new FactoryModuleBuilder()
                        .implement(EodWriter.class, CompactEodWriter.class)
                        .build(EodWriterFactory.class));
                bind(EodEncoder.class).to(CompactEodEncoder.class);
            }

            private void bindCsvTarget() {
                install(new FactoryModuleBuilder()
                        .implement(EodWriter.class, CsvEodWriter.class)
                        .build(EodWriterFactory.class));
            }

            private void bindMongoTarget() {
                install(new FactoryModuleBuilder()
                        .implement(EodWriter.class, MongoEodWriter.class)
                        .build(EodWriterFactory.class));
            }
        };
    }
}