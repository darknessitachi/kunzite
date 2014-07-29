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

import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.simple.DefaultContextLogger;
import com.zaradai.kunzite.trader.services.md.eod.*;
import com.zaradai.kunzite.trader.services.md.eod.yahoo.QuoteUrlFormatter;
import com.zaradai.kunzite.trader.services.md.eod.yahoo.YahooHistoricalDownloader;
import com.zaradai.kunzite.trader.services.md.eod.yahoo.YahooQuoteUrlFormatter;
import org.apache.commons.cli.*;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.*;

public class EodDownload {
    private static final String TOOL_NAME = "eoddownloader";
    private static final String HELP_OPTION = "help";
    private static final String FROM_OPTION = "from";
    private static final String UNTIL_OPTION = "until";
    private static final String NAME_OPTION = "name";
    private static final String APPEND_DATE_OPTION = "append";
    private static final String SYMBOLS_OPTION = "symbols";
    private static final String DEBUG_OPTION  = "debug";
    private static final String THREADS_OPTION  = "threads";
    private static final String DEFAULT_FILE_NAME = "marketdata";

    private static final Logger log = LoggerFactory.getLogger(TOOL_NAME);
    private final Injector injector;
    private final Multimap<DateTime, EodData> data;

    public EodDownload() {
        injector = Guice.createInjector(getEodDownloadModule());
        data = createMap();
    }

    private Multimap<DateTime, EodData> createMap() {
        // need a multi-map sorted by key with arbitrary ordered values
        return Multimaps.newMultimap(new TreeMap<DateTime, Collection<EodData>>(),
                new Supplier<Collection<EodData>>() {
                    @Override
                    public Collection<EodData> get() {
                        return Lists.newArrayList();
                    }
                });
    }

    private Module getEodDownloadModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(Logger.class).toInstance(log);
                bind(ContextLogger.class).to(DefaultContextLogger.class);
                bind(QuoteUrlFormatter.class).to(YahooQuoteUrlFormatter.class);
                bind(EodDataDownloader.class).to(YahooHistoricalDownloader.class);
                bind(EodWriter.class).to(FileEodWriter.class);
                bind(EodEncoder.class).to(CompactEodEncoder.class);
            }
        };
    }

    public static void main(String[] args) {
        EodDownload downloader = new EodDownload();

        downloader.run(args);
    }

    private void run(String[] args) {
        DownloadOptions options = new OptionsParser().parse(args);
        ExecutorService executorService = createExecutorService(options.threads);
        CompletionService<Map<DateTime, EodData>> completionService = new ExecutorCompletionService(executorService);
        List<Future<Map<DateTime, EodData>>> futures = Lists.newArrayList();

        try {
            if (options.debug) {
                printOptions(options);
            }
            for (String symbol : options.symbols) {
                futures.add(completionService.submit(scheduleDownload(symbol, options.from, options.until)));
            }

            for (int i = 0; i < options.symbols.size(); ++i) {
                try {
                    addData(completionService.take().get());
                } catch (Exception e) {
                    System.err.println(e);
                    if (options.debug) {
                        e.printStackTrace(System.err);
                    }
                }
            }
            String filename = options.getFilename();
            long written = writeDataFile(filename);

            File where = new File(filename);
            System.out.println(String.format("Wrote %d items to %s", written, where.getAbsolutePath()));
            // close the executor
            executorService.shutdown();
        } catch (Exception e) {
            System.err.println("Unable to download data");
            System.err.println(e);
            if (options.debug) {
                e.printStackTrace(System.err);
            }
            System.exit(1);
        }
    }

    private long writeDataFile(String fileName) throws Exception {
        long res = 0;
        EodWriter eodWriter = getEodWriter();

        eodWriter.open(fileName);

        for (Map.Entry<DateTime, Collection<EodData>> entry : data.asMap().entrySet()) {
            EodDayData eodDayData = new EodDayData(entry.getKey(), entry.getValue());
            eodWriter.write(eodDayData);
            res++;
        }
        eodWriter.close();

        return res;
    }

    private EodWriter getEodWriter() {
        return injector.getInstance(EodWriter.class);
    }

    private void addData(Map<DateTime, EodData> dataMap) {
        for (Map.Entry<DateTime, EodData> entry : dataMap.entrySet()) {
            data.put(entry.getKey(), entry.getValue());
        }
    }

    private Callable<Map<DateTime, EodData>> scheduleDownload(final String symbol, final DateTime from, final DateTime until) {
        return new Callable<Map<DateTime, EodData>>() {
            @Override
            public Map<DateTime, EodData> call() throws Exception {
                EodDataDownloader downloader = injector.getInstance(EodDataDownloader.class);

                return downloader.download(symbol, from, until);
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

    private void printOptions(DownloadOptions options) {
        System.out.println("Debug: " + ((options.debug) ? "true" : "false"));
        System.out.println("Filename: " + options.filename);
        System.out.println("Symbols: " + options.symbols);
        System.out.println("Append Date: " + ((options.appendDate) ? "true" : "false"));
        System.out.println("From: " + options.from);
        System.out.println("Until: " + options.until);
        System.out.println("Threads: " + options.threads);
    }

    static class DownloadOptions {
        public DateTime from;
        public DateTime until;
        public List<String> symbols;
        public String filename;
        public boolean appendDate;
        public boolean debug;
        public int threads;
        private final DateTimeFormatter dateTimeFormatter;

        DownloadOptions() {
            dateTimeFormatter = DateTimeFormat.forPattern("YYYYMMdd");
        }

        public String getFilename() {
            if (appendDate) {
                return FilenameUtils.getFullPath(filename)
                        + FilenameUtils.getBaseName(filename)
                        + "-"
                        + DateTime.now().toString(dateTimeFormatter)
                        + FilenameUtils.EXTENSION_SEPARATOR_STR
                        + FilenameUtils.getExtension(filename);
            } else {
                return filename;
            }
        }
    }

    private class OptionsParser {
        private final CommandLineParser parser;
        private final Options options;
        private final DateTimeFormatter dateTimeFormatter;

        OptionsParser() {
            parser = new GnuParser();
            options = getCmdLineOptions();
            dateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd");
        }


        public DownloadOptions parse(String[] args) {
            DownloadOptions downloadOptions = new DownloadOptions();
            try
            {
                CommandLine cmd = parser.parse(options, args, false);

                if (cmd.hasOption(HELP_OPTION)) {
                    printUsage(options);
                    System.exit(1);
                }

                downloadOptions.from = getFromDate(cmd);
                downloadOptions.until = getUntilDate(cmd);
                downloadOptions.symbols = Lists.newArrayList(getSymbols(cmd));
                downloadOptions.appendDate = cmd.hasOption(APPEND_DATE_OPTION);
                downloadOptions.debug = cmd.hasOption(DEBUG_OPTION);
                downloadOptions.filename = getFileName(cmd);
                downloadOptions.threads = getThreads(cmd);

                return downloadOptions;
            } catch (Exception e) {
                invalidOptions(e.getMessage(), options);
                return null;
            }
        }

        private int getThreads(CommandLine cmd) {
            if (cmd.hasOption(THREADS_OPTION)) {
                return Integer.parseInt(cmd.getOptionValue(THREADS_OPTION));
            }

            return 1;   // default is single threaded execution.
        }

        private String getFileName(CommandLine cmd) {
            if (cmd.hasOption(NAME_OPTION) && !Strings.isNullOrEmpty(cmd.getOptionValue(NAME_OPTION))) {
                return cmd.getOptionValue(NAME_OPTION);
            } else {
                return DEFAULT_FILE_NAME;
            }
        }

        private String[] getSymbols(CommandLine cmd) {
            if (cmd.hasOption(SYMBOLS_OPTION)) {
                return cmd.getOptionValues(SYMBOLS_OPTION);
            } else {
                throw new RuntimeException("Symbols are required");
            }
        }

        private DateTime getFromDate(CommandLine cmd) {
            if (cmd.hasOption(FROM_OPTION)) {
                try {
                    return dateTimeFormatter.parseDateTime(cmd.getOptionValue(FROM_OPTION));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("From date illegally formed");
                }
            } else {
                throw new RuntimeException("From date is required");
            }
        }

        private DateTime getUntilDate(CommandLine cmd) {
            if (cmd.hasOption(UNTIL_OPTION)) {
                try {
                    return dateTimeFormatter.parseDateTime(cmd.getOptionValue(UNTIL_OPTION));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Until date illegally formed");
                }
            } else {
                throw new RuntimeException("Until date is required");
            }
        }

        private void invalidOptions(String msg, Options options) {
            System.err.println(msg);
            printUsage(options);
            System.exit(1);
        }

        private void printUsage(Options options) {
            String usage = String.format("%s [options]", TOOL_NAME);
            StringBuilder header = new StringBuilder();
            header.append("--\n");
            header.append("Download EOD market data and write to a compact sorted data file");
            header.append("\n--\n");
            header.append("Options are:");
            new HelpFormatter().printHelp(usage, header.toString(), options, "");
        }

        private Options getCmdLineOptions() {
            Options options = new Options();

            options.addOption("h",  HELP_OPTION, false, "display this help message");
            Option fromOption = new Option("f",  FROM_OPTION, true,
                    "date to load data FROM in [YYYY-MM-dd] format, i.e. 2003-06-18");
            fromOption.setRequired(true);
            options.addOption(fromOption);
            Option untilOption = new Option("u",  UNTIL_OPTION, true,
                    "date to load data UNTIL in [YYYY-MM-dd] format, i.e. 2003-06-18");
            untilOption.setRequired(true);
            options.addOption(untilOption);
            options.addOption("n",  NAME_OPTION, true, "name for the created file with EOD data");
            options.addOption("ad",  APPEND_DATE_OPTION, false, "will append current date to filename");
            options.addOption("d", DEBUG_OPTION, false, "display stack traces");
            Option symbolsOption = new Option("s", SYMBOLS_OPTION, true,
                    "A list of space separated symbols to download data for, e.g. INTC AAPL F AMAT");
            symbolsOption.setRequired(true);
            symbolsOption.setArgs(Option.UNLIMITED_VALUES);
            options.addOption(symbolsOption);
            options.addOption("t", THREADS_OPTION, true, "Specify number of download threads to utilize, default is 1");

            return options;
        }
    }

}
