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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Property config.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property")
public class PropertyType {

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private String value;

    /**
     * Gets the value of the name property.
     *
     * @return  possible object is {@link String }
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value of the value property.
     *
     * @return  possible object is {@link String }
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of the name property.
     *
     * @param  name  allowed object is {@link String }
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the value of the value property.
     *
     * @param  value  allowed object is {@link String }
     */
    public void setValue(final String value) {
        this.value = value;
    }

}
