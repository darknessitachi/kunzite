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
package com.zaradai.kunzite.optimizer.data.dataset;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.joda.time.DateTime;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

public final class DataSetContext {
    private String name;
    private String description;
    private UUID id;
    private DateTime createdOn;
    private String createdBy;
    private String createdHost;
    private String version;

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    private void setId(UUID id) {
        this.id = id;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    private void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedHost() {
        return createdHost;
    }

    private void setCreatedHost(String createdHost) {
        this.createdHost = createdHost;
    }

    public String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DataSetContext other = (DataSetContext) obj;

        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static ContextBuilder builder() {
        return new ContextBuilder();
    }

    public static final class ContextBuilder {
        private static final String DEFAULT_VERSION = "1.0";

        private DataSetContext context;

        private ContextBuilder() {
            context = new DataSetContext();
        }

        public ContextBuilder name(String name) {
            context.setName(name);
            return this;
        }

        public ContextBuilder id(UUID uuid) {
            context.setId(uuid);
            return this;
        }

        public ContextBuilder description(String description) {
            context.setDescription(description);
            return this;
        }

        public ContextBuilder version(String version) {
            context.setVersion(version);
            return this;
        }

        public ContextBuilder createdBy(String user) {
            context.setCreatedBy(user);
            return this;
        }

        public ContextBuilder createdOn(DateTime dateTime) {
            context.setCreatedOn(dateTime);
            return this;
        }

        public ContextBuilder host(String host) {
            context.setCreatedHost(host);
            return this;
        }

        public DataSetContext build() {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(context.getName()), "Missing name");

            if (context.getId() == null) {
                useDefaultId();
            }

            if (Strings.isNullOrEmpty(context.getVersion())) {
                useDefaultVersion();
            }
            if (context.getCreatedOn() == null) {
                context.setCreatedOn(DateTime.now());
            }
            if (Strings.isNullOrEmpty(context.getCreatedBy())) {
                useDefaultCreatedBy();
            }
            if (Strings.isNullOrEmpty(context.getCreatedHost())) {
                useDefaultHost();
            }

            return context;
        }

        private void useDefaultId() {
            context.setId(UUID.randomUUID());
        }

        private void useDefaultHost() {
            context.setCreatedHost(getDefaultHost());
        }

        private String getDefaultHost() {
            try {
                return InetAddress.getLocalHost().toString();
            } catch (UnknownHostException e) {
                return "localhost";
            }
        }

        private void useDefaultCreatedBy() {
            context.setCreatedBy(getDefaultUser());
        }

        private String getDefaultUser() {
            return System.getProperty("user.name");
        }

        private void useDefaultVersion() {
            context.setVersion(DEFAULT_VERSION);
        }
    }
}
