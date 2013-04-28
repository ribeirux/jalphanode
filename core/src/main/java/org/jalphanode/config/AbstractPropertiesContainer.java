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
 * $Id: AbstractPropertiesContainer.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Properties container configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlSeeAlso({ TaskType.class, TaskSchedulerType.class, AsyncExecutorType.class, MembershipType.class })
public abstract class AbstractPropertiesContainer implements PropertiesContainerConfig {

    @XmlElement
    @XmlJavaTypeAdapter(TypedPropertiesAdapter.class)
    private final TypedProperties properties;

    /**
     * Initializes internal fields.
     */
    public AbstractPropertiesContainer() {
        this.properties = new TypedProperties();
    }

    /**
     * Gets the properties property.
     *
     * @return  the properties property
     */
    @Override
    public TypedProperties getProperties() {
        return this.properties;
    }
}
