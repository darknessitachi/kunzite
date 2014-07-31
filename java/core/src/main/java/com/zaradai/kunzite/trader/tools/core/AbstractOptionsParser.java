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
package com.zaradai.kunzite.trader.tools.core;

import org.apache.commons.cli.*;

public abstract class AbstractOptionsParser {
    private static final String HELP_OPTION = "help";
    private static final String DEBUG_OPTION  = "debug";

    private final CommandLineParser parser;
    private final Options options;

    public AbstractOptionsParser() {
        options = getCmdLineOptions();
        parser = new GnuParser();
    }

    private Options getCmdLineOptions() {
        Options options = new Options();

        options.addOption("h", HELP_OPTION, false, "display this help message");
        options.addOption("d", DEBUG_OPTION, false, "display stack traces");

        addOptions(options);

        return options;
    }

    public BaseOptions parse(String[] args) {
        BaseOptions ret = createOptions();

        try {
            CommandLine cmd = parser.parse(options, args, false);

            ret.setHelp(cmd.hasOption(HELP_OPTION));
            ret.setDebug(cmd.hasOption(DEBUG_OPTION));
            readOptions(cmd, ret);

            if (ret.isHelp()) {
                printUsage();
                return null;
            }

            return ret;
        } catch (Exception e) {
            invalidOptions(e.getMessage(), args, options);
            return null;
        }
    }

    private void invalidOptions(String message, String[] args, Options options) {
        System.err.println("Unable to parse arguments: " + prettyPrint(args));
        System.err.println(message);
        printUsage();
    }

    private String prettyPrint(String[] args) {
        StringBuilder sb = new StringBuilder();

        for (String arg : args) {
            sb.append(arg);
            sb.append(" ");
        }

        return sb.toString();
    }

    public void printUsage() {
        String usage = String.format("%s [options]", getToolName());
        StringBuilder header = new StringBuilder();
        header.append("--\n");
        header.append(getUsageHeader());
        header.append("\n--\n");
        header.append("Options are:");
        new HelpFormatter().printHelp(usage, header.toString(), options, "");
    }

    protected abstract void readOptions(CommandLine cmd, BaseOptions options) throws OptionReadException;
    protected abstract String getUsageHeader();
    protected abstract String getToolName();
    protected abstract void addOptions(Options options);
    protected abstract BaseOptions createOptions();
}
