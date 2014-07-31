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

import java.util.Arrays;
import java.util.List;

public class EodConverterOptionsParser extends AbstractOptionsParser {
    private static final String PROPERTY_OPTION = "property";
    private static final String SOURCE_OPTION = "source";
    private static final String TARGET_OPTION = "target";
    private static final String SOURCE_TYPE_OPTION = "input";
    private static final String TARGET_TYPE_OPTION = "output";
    private static final String SYMBOLS_OPTION = "symbols";
    private static final String THREADS_OPTION = "threads";
    private final FileIO fileIO;

    @Inject
    EodConverterOptionsParser(FileIO fileIO) {
        this.fileIO = fileIO;
    }

    @Override
    protected void readOptions(CommandLine cmd, BaseOptions options) throws OptionReadException {
        EodConverterOptions eodConverterOptions = (EodConverterOptions) options;

        eodConverterOptions.setProperties(cmd.getOptionProperties(PROPERTY_OPTION));
        eodConverterOptions.setSourceFolder(getSourceFolder(cmd));
        eodConverterOptions.setTargetFolder(getTargetFolder(cmd));
        eodConverterOptions.setSourceType(getSourceType(cmd));
        eodConverterOptions.setTargetType(getTargetType(cmd));
        eodConverterOptions.setSymbols(getSymbols(cmd));
        eodConverterOptions.setThreads(getThreads(cmd));
    }

    private SupportedEodIO getSourceType(CommandLine cmd) throws OptionReadException {
        return getType(cmd.getOptionValue(SOURCE_TYPE_OPTION));
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

    // Attempts to convert any string to a valid enum string, e.g MONgo would become valid Mongo
    private String toCamelCase(String type) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, type.toLowerCase());
    }

    private String getTargetFolder(CommandLine cmd) throws OptionReadException {
        if (cmd.hasOption(TARGET_OPTION) && !Strings.isNullOrEmpty(cmd.getOptionValue(TARGET_OPTION))) {
            String fileName = cmd.getOptionValue(TARGET_OPTION);
            validateFolder(fileName);

            return fileName;
        }

        return null;
    }

    private String getSourceFolder(CommandLine cmd) throws OptionReadException {
        if (cmd.hasOption(SOURCE_OPTION)) {
            String fileName = cmd.getOptionValue(SOURCE_OPTION);
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

    @Override
    protected String getUsageHeader() {
        return EodConverter.USAGE_HEADER;
    }

    @Override
    protected String getToolName() {
        return EodConverter.TOOL_NAME;
    }

    @Override
    protected void addOptions(Options options) {
        Option property = OptionBuilder.withLongOpt(PROPERTY_OPTION)
                .withArgName( "key=value" )
                .hasArgs(2)
                .withValueSeparator()
                .withDescription("Generic key value pair properties")
                .create('p');
        options.addOption(property);
        options.addOption("s", SOURCE_OPTION, true, "Source folder containing encoded files for input types that " +
                "store their data in folders such as CSV.  If specified the folder must exist");
        Option sourceType = new Option("i", SOURCE_TYPE_OPTION, true, "Source input type, e.g. CSV, COMPACT, MONGO");
        sourceType.setRequired(true);
        options.addOption(sourceType);
        options.addOption("t", TARGET_OPTION, true, "Target folder containing encoded files for output types that " +
                "store their data in folders such as CSV.  If specified the folder must exist");
        Option targetType = new Option("o", TARGET_TYPE_OPTION, true, "Target output type, e.g. CSV, COMPACT, MONGO");
        targetType.setRequired(true);
        options.addOption(targetType);
        Option symbolsOption = OptionBuilder.withLongOpt(SYMBOLS_OPTION)
                .isRequired()
                .withArgName("values")
                .withValueSeparator(' ')
                .hasArgs(Option.UNLIMITED_VALUES)
                .withDescription("A list of space separated symbols to download data for, e.g. INTC AAPL F AMAT")
                .create("sm");
        options.addOption(symbolsOption);
        options.addOption("th", THREADS_OPTION, true, "Specify number of conversion threads, default is 1");
    }

    @Override
    protected BaseOptions createOptions() {
        return new EodConverterOptions();
    }
}
