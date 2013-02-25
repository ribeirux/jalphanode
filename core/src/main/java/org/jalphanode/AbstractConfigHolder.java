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
 * $Id$
 *******************************************************************************/
package org.jalphanode;

import org.jalphanode.config.JAlphaNodeConfig;

import com.google.common.base.Preconditions;

/**
 * Configuration holder. All components which can be configured must extend this class.
 * 
 * @author ribeirux
 * @version $Revision$
 */
public abstract class AbstractConfigHolder {

    private final JAlphaNodeConfig config;

    /**
     * Initializes internal configuration.
     * 
     * @param config the configuration
     */
    public AbstractConfigHolder(final JAlphaNodeConfig config) {
        this.config = Preconditions.checkNotNull(config, "config");
    }

    /**
     * Gets the config property.
     * 
     * @return the config property
     */
    public JAlphaNodeConfig getConfig() {
        return this.config;
    }
}
