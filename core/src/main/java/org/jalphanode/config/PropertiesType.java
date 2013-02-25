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
 * $Id: PropertiesType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Properties configuration.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "properties", propOrder = { "property" })
public class PropertiesType {

    @XmlElement(required = true)
    private final List<PropertyType> property;

    /**
     * Initializes internal structure.
     */
    public PropertiesType() {
        this.property = new ArrayList<PropertyType>();
    }

    /**
     * Gets the {@link List} of properties.
     * 
     * @return the {@link List} of properties
     */
    public List<PropertyType> getProperty() {
        return Collections.unmodifiableList(this.property);
    }

    /**
     * Adds a new property.
     * 
     * @param propertyType property to add
     */
    public void addProperty(final PropertyType propertyType) {
        this.property.add(propertyType);
    }

    /**
     * Removes the specified property.
     * 
     * @param propertyType property to remove
     */
    public void removeProperty(final PropertyType propertyType) {
        this.property.remove(propertyType);
    }

    /**
     * Removes the property with the specified index.
     * 
     * @param index index of the property to remove.
     */
    public void removeProperty(final int index) {
        this.property.remove(index);
    }
}
