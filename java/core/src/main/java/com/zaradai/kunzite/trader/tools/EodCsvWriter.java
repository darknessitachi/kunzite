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
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.zaradai.kunzite.logging.ContextLogger;
import com.zaradai.kunzite.logging.simple.DefaultContextLogger;
import com.zaradai.kunzite.trader.services.md.eod.*;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class EodCsvWriter {
    private static final String TOOL_NAME = "eodcsvwriter";
    private static final String HELP_OPTION = "help";
    private static final String SOURCE_OPTION = "source";
    private static final String TARGET_OPTION = "target";
    private static final String DEBUG_OPTION  = "debug";
    private static final String DEFAULT_FILE_NAME = "marketdata.csv";

    private static final Logger log = LoggerFactory.getLogger(TOOL_NAME);
    private final Injector injector;

    EodCsvWriterOptions options;

    public EodCsvWriter() {
        injector = Guice.createInjector(getEodCsvWriterModule());
    }

    private Module getEodCsvWriterModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(Logger.class).toInstance(log);
                bind(ContextLogger.class).to(DefaultContextLogger.class);
                bind(EodWriter.class).to(CsvFileEodWriter.class);
                bind(EodReader.class).to(FileEodReader.class);
                bind(EodEncoder.class).to(CompactEodEncoder.class);
            }
        };
    }

    public static void main(String[] args) {
        new EodCsvWriter().run(args);
    }

    private void run(String[] args) {
        EodCsvWriterOptions options = new OptionsParser().parse(args);

        try {
            if (options.debug) {
                printOptions(options);
            }

            EodReader reader = injector.getInstance(EodReader.class);
            EodWriter writer = injector.getInstance(EodWriter.class);

            reader.open(options.source);
            writer.open(options.target);

            EodDayData dayData = null;

            while ((dayData = reader.getNext()) != null) {
                writer.write(dayData);
            }
            reader.close();
            writer.close();
        } catch (Exception e) {
            printError(options, e);
        }
    }

    private void printError(EodCsvWriterOptions options, Exception e) {
        System.err.println("Unable to write csv data");
        System.err.println(e);
        if (options.debug) {
            e.printStackTrace(System.err);
        }
        System.exit(1);
    }

    private void printOptions(EodCsvWriterOptions options) {
        System.out.println("Debug: " + ((options.debug) ? "true" : "false"));
        System.out.println("Sorce: " + options.source);
        System.out.println("Target: " + options.target);
    }

    static class EodCsvWriterOptions {
        public String source;
        public String target;
        public boolean debug;
    }

    private class OptionsParser {
        private final CommandLineParser parser;
        private final Options options;

        private OptionsParser() {
            options = getCmdLineOptions();
            parser = new GnuParser();
        }

        private EodCsvWriterOptions parse(String[] args) {
            EodCsvWriterOptions eodCsvWriterOptions = new EodCsvWriterOptions();
            try {
                CommandLine cmd= parser.parse(options, args, false);

                if (cmd.hasOption(HELP_OPTION)) {
                    printUsage(options);
                    System.exit(1);
                }
                eodCsvWriterOptions.debug = cmd.hasOption(DEBUG_OPTION);
                eodCsvWriterOptions.source = getSource(cmd);
                eodCsvWriterOptions.target = getTarget(cmd);

                return eodCsvWriterOptions;
            } catch (Exception e) {
                invalidOptions(e.getMessage(), options);
                return null;
            }
        }

        private String getSource(CommandLine cmd) {
            if (cmd.hasOption(SOURCE_OPTION)) {
                String fileName = cmd.getOptionValue(SOURCE_OPTION);
                File sourceFile = new File(fileName);

                if (sourceFile.exists()) {
                    return fileName;
                } else {
                    throw new RuntimeException("Source file does not exist");
                }
            } else {
                throw new RuntimeException("Source is required");
            }
        }

        private String getTarget(CommandLine cmd) {
            if (cmd.hasOption(TARGET_OPTION) && !Strings.isNullOrEmpty(cmd.getOptionValue(TARGET_OPTION))) {
                return cmd.getOptionValue(TARGET_OPTION);
            } else {
                return DEFAULT_FILE_NAME;
            }
        }

        private Options getCmdLineOptions() {
            Options options = new Options();

            options.addOption("h", HELP_OPTION, false, "display this help message");
            options.addOption("d", DEBUG_OPTION, false, "display stack traces");
            Option fromOption = new Option("s",  SOURCE_OPTION, true,
                    "the filename of the binary encoded MD file to read from");
            fromOption.setRequired(true);
            options.addOption(fromOption);
            options.addOption("t", TARGET_OPTION, true, "the filename of the csv file to create.");

            return options;
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
            header.append("Convert market data stored in a compact sorted data file to a plain text csv file.");
            header.append("\n--\n");
            header.append("Options are:");
            new HelpFormatter().printHelp(usage, header.toString(), options, "");
        }
    }
}
