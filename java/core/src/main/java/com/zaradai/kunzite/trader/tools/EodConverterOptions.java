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

import com.zaradai.kunzite.trader.services.md.eod.SupportedEodIO;
import com.zaradai.kunzite.trader.tools.core.BaseOptions;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Properties;

public class EodConverterOptions  extends BaseOptions {
    private SupportedEodIO sourceType;
    private String sourceFolder;
    private SupportedEodIO targetType;
    private String targetFolder;
    private List<String> symbols;
    private Properties properties;
    private int threads;

    public SupportedEodIO getSourceType() {
        return sourceType;
    }

    public void setSourceType(SupportedEodIO sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public SupportedEodIO getTargetType() {
        return targetType;
    }

    public void setTargetType(SupportedEodIO targetType) {
        this.targetType = targetType;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("sourceType", sourceType)
                .append("sourceFolder", sourceFolder)
                .append("targetType", targetType)
                .append("targetFolder", targetFolder)
                .append("symbols", symbols)
                .append("properties", properties)
                .append("threads", threads)
                .toString();
    }
}
