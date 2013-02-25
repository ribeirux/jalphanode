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
 * @author ribeirux
 * @version $Revision: 148 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskSchedulerType")
public class TaskSchedulerType extends AbstractPropertiesContainer implements TaskSchedulerConfig {

    /**
     * Default core pool size.
     */
    public static final int DEFAULT_CORE_POOL_SIZE = 10;

    @XmlAttribute
    private Integer corePoolSize;

    /**
     * Creates a new async executor with default configuration.
     */
    public TaskSchedulerType() {
        super();
        this.corePoolSize = TaskSchedulerType.DEFAULT_CORE_POOL_SIZE;
    }

    /**
     * Gets the corePoolSize property.
     * 
     * @return the corePoolSize property
     */
    @Override
    public Integer getCorePoolSize() {
        return this.corePoolSize;
    }

    /**
     * Sets the corePoolSize property.
     * 
     * @param corePoolSize the corePoolSize to set
     */
    public void setCorePoolSize(final Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
}
