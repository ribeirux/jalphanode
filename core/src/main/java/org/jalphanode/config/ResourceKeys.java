/**
 *    Copyright 2011 Pedro Ribeiro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.jalphanode.config;

import org.jalphanode.util.ConfigurationUtils;

/**
 * Contains all resource keys.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public enum ResourceKeys {

    /**
     * Schema location.
     */
    SCHEMA_LOCATION("configuration.schema.location");

    private final String key;

    private ResourceKeys(final String key) {
        this.key = key;
    }

    /**
     * Gets the resource key.
     *
     * @return  the resource key
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Gets the resource value.
     *
     * @return  the resource value
     */
    public String getValue() {
        return ConfigurationUtils.getResourceProperty(this.key);
    }

}
