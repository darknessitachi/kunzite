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
package com.zaradai.kunzite.trader.services.md;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.zaradai.kunzite.trader.config.md.MappingConfig;
import com.zaradai.kunzite.trader.config.md.MappingValue;

import java.util.Map;

public class MappingManager {
    private final Map<String, Map<String, String>> mapper;
    private final Map<String, String> idBySid;

    public MappingManager(Iterable<MappingConfig> mappings) {
        mapper = Maps.newHashMap();
        idBySid = Maps.newHashMap();

        for (MappingConfig mapping : mappings) {
            Map<String, String> idToSid = Maps.newHashMap();
            mapper.put(mapping.getName(), idToSid);
            // populate the map
            for (MappingValue mappingValue : mapping.getMappings()) {
                idToSid.put(mappingValue.getId(), mappingValue.getSid());
            }
        }
    }

    public String getSid(String mapName, String id) {
        Map<String, String> map = getMap(mapName);

        if (map != null) {
            String sid = map.get(id);

            if (!Strings.isNullOrEmpty(sid)) {
                // remember the reverse map
                idBySid.put(sid, id);
                // and return valid mapping
                return sid;
            }
        }

        return null;
    }

    public String getId(String sid) {
        return idBySid.get(sid);
    }

    private Map<String, String> getMap(String mapName) {
        return  mapper.get(mapName);
    }
}
