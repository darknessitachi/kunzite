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

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.zaradai.kunzite.trader.services.md.eod.SupportedEodIO;
import com.zaradai.kunzite.trader.tools.core.AbstractOptionsParser;
import com.zaradai.kunzite.trader.tools.core.BaseOptions;
import com.zaradai.kunzite.trader.tools.core.OptionReadException;
import com.zaradai.kunzite.utils.FileIO;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;

public class EodDownloadOptionsParser extends AbstractOptionsParser {
    private static final String PROPERTY_OPTION = "property";
    private static final String TARGET_OPTION = "target";
    private static final String TARGET_TYPE_OPTION = "output";
    private static final String SYMBOLS_OPTION = "symbols";
    private static final String THREADS_OPTION = "threads";
    private static final String FROM_OPTION = "from";
    private static final String UNTIL_OPTION = "until";

    private final FileIO fileIO;
    private final DateTimeFormatter dateTimeFormatter;

    @Inject
    EodDownloadOptionsParser(FileIO fileIO) {
        this.fileIO = fileIO;
        dateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd");
    }

    @Override
    protected void readOptions(CommandLine cmd, BaseOptions options) throws OptionReadException {
        EodDownloadOptions eodDownloadOptions = (EodDownloadOptions) options;

        eodDownloadOptions.setProperties(cmd.getOptionProperties(PROPERTY_OPTION));
        eodDownloadOptions.setTargetFolder(getTargetFolder(cmd));
        eodDownloadOptions.setTargetType(getTargetType(cmd));
        eodDownloadOptions.setSymbols(getSymbols(cmd));
        eodDownloadOptions.setThreads(getThreads(cmd));
        eodDownloadOptions.setFrom(getFrom(cmd));
        eodDownloadOptions.setUntil(getUntil(cmd));
    }

    private SupportedEodIO getTargetType(CommandLine cmd) throws OptionReadException {
        return getType(cmd.getOptionValue(TARGET_TYPE_OPTION));
    }

    private SupportedEodIO getType(String type) throws OptionReadException {
        if (!Strings.isNullOrEmpty(type)) {
            return SupportedEodIO.valueOf(toCamelCase(type));
        } else {
            throw new OptionReadException("Invalid EOD IO Type");
        }
    }

    private String getTargetFolder(CommandLine cmd) throws OptionReadException {
        if (cmd.hasOption(TARGET_OPTION) && !Strings.isNullOrEmpty(cmd.getOptionValue(TARGET_OPTION))) {
            String fileName = cmd.getOptionValue(TARGET_OPTION);
            validateFolder(fileName);

            return fileName;
        }

        return null;
    }

    private void validateFolder(String fileName) throws OptionReadException {
        if (!fileIO.exists(fileName)) {
            throw new OptionReadException("folder does not exist");
        }
        if (!fileIO.isDirectory(fileName)) {
            throw new OptionReadException("Specified folder is not a directory");
        }
    }

    // Attempts to convert any string to a valid enum string, e.g MONgo would become valid Mongo
    private String toCamelCase(String type) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, type.toLowerCase());
    }

    private List<String> getSymbols(CommandLine cmd) throws OptionReadException {
        if (cmd.hasOption(SYMBOLS_OPTION)) {
            return Arrays.asList(cmd.getOptionValues(SYMBOLS_OPTION));
        } else {
            throw new OptionReadException("Symbols are required");
        }
    }

    private int getThreads(CommandLine cmd) {
        if (cmd.hasOption(THREADS_OPTION)) {
            return Integer.parseInt(cmd.getOptionValue(THREADS_OPTION));
        }

        return 1;   // default is single threaded execution.
    }

    private DateTime getFrom(CommandLine cmd) throws OptionReadException {
        if (cmd.hasOption(FROM_OPTION)) {
            return parseDate(cmd.getOptionValue(FROM_OPTION));
        } else {
            throw new OptionReadException("From date is required");
        }
    }

    private DateTime getUntil(CommandLine cmd) throws OptionReadException {
        if (cmd.hasOption(UNTIL_OPTION)) {
            return parseDate(cmd.getOptionValue(UNTIL_OPTION));
        } else {
            throw new OptionReadException("Until date is required");
        }
    }

    private DateTime parseDate(String optionValue) {
        return dateTimeFormatter.parseDateTime(optionValue);
    }

    @Override
    protected String getUsageHeader() {
        return EodDownload.USAGE_HEADER;
    }

    @Override
    protected String getToolName() {
        return EodDownload.TOOL_NAME;
    }

    @Override
    protected void addOptions(Options options) {
        options.addOption(OptionBuilder.withLongOpt(PROPERTY_OPTION)
                .withArgName( "key=value" )
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("Generic key value pair properties")
                .create('p'));
        options.addOption(OptionBuilder.withLongOpt(TARGET_OPTION)
                .withArgName("target folder")
                .hasArgs(1)
                .withDescription("Target folder to contain encoded files for output types that " +
                    "store their data in folders such as CSV.  If specified the folder must exist")
                .create("t"));
        options.addOption(OptionBuilder.withLongOpt(TARGET_TYPE_OPTION)
                .withArgName("type")
                .hasArgs(1)
                .withDescription("Target output type, e.g. CSV, COMPACT, MONGO")
                .create("o"));
        options.addOption(OptionBuilder.withLongOpt(SYMBOLS_OPTION)
                .isRequired()
                .withArgName("values")
                .withValueSeparator(' ')
                .hasArgs(Option.UNLIMITED_VALUES)
                .withDescription("A list of space separated symbols to download data for, e.g. INTC AAPL F AMAT")
                .create("sm"));
        options.addOption(OptionBuilder.withLongOpt(THREADS_OPTION)
                .withArgName("num threads")
                .hasArgs(1)
                .withDescription("Specify number of conversion threads, default is 1")
                .create("th"));
        options.addOption(OptionBuilder.withLongOpt(FROM_OPTION)
                .isRequired()
                .hasArgs(1)
                .withArgName("date")
                .withDescription("Date to start the download from, format is YYYY-MM-dd, e.g. 2013-06-14")
                .create("f"));
        options.addOption(OptionBuilder.withLongOpt(UNTIL_OPTION)
                .withArgName("date")
                .hasArgs(1)
                .withDescription("Date to end the download range, format is YYYY-MM-dd, e.g. 2013-06-14.  Default is" +
                        " today if not specified")
                .create("u"));
    }

    @Override
    protected BaseOptions createOptions() {
        return new EodDownloadOptions();
    }
}
