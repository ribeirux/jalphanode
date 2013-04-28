/*******************************************************************************
 * JAlphaNode: Java Clustered Timer
 * Copyright (C) 2011 Pedro Ribeiro
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * $Id: ResourceKeys.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
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

    private String key;

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
