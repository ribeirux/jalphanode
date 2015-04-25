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

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collections;
import java.util.List;

/**
 * Properties configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "properties", propOrder = "property")
public class PropertiesType {

    @XmlElement(required = true)
    private final List<PropertyType> property;

    /**
     * Initializes internal structure.
     */
    public PropertiesType() {
        this.property = Lists.newLinkedList();
    }

    /**
     * Gets the {@link List} of properties.
     *
     * @return  the {@link List} of properties
     */
    public List<PropertyType> getProperty() {
        return Collections.unmodifiableList(this.property);
    }

    /**
     * Adds a new property.
     *
     * @param  propertyType  property to add
     */
    public void addProperty(final PropertyType propertyType) {
        this.property.add(propertyType);
    }

    /**
     * Removes the specified property.
     *
     * @param  propertyType  property to remove
     */
    public void removeProperty(final PropertyType propertyType) {
        this.property.remove(propertyType);
    }

    /**
     * Removes the property with the specified index.
     *
     * @param  index  index of the property to remove.
     */
    public void removeProperty(final int index) {
        this.property.remove(index);
    }
}
