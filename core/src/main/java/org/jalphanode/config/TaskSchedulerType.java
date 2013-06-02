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
package org.jalphanode.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Task scheduler configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 148 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskSchedulerType")
public class TaskSchedulerType extends AbstractPropertiesContainer implements TaskSchedulerConfig {

    /**
     * Default pool size.
     */
    public static final int DEFAULT_POOL_SIZE = 10;

    @XmlAttribute
    private Integer poolSize;

    /**
     * Creates a new async executor with default configuration.
     */
    public TaskSchedulerType() {
        super();
        this.poolSize = TaskSchedulerType.DEFAULT_POOL_SIZE;
    }

    /**
     * Gets the poolSize property.
     *
     * @return  the poolSize property
     */
    @Override
    public Integer getPoolSize() {
        return this.poolSize;
    }

    /**
     * Sets the poolSize property.
     *
     * @param  poolSize  the poolSize to set
     */
    public void setPoolSize(final Integer poolSize) {
        this.poolSize = poolSize;
    }
}
