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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Properties container configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlSeeAlso({ TaskType.class, TaskSchedulerType.class, AsyncNotificationExecutorType.class, MembershipType.class })
public abstract class AbstractPropertiesContainer implements PropertiesContainerConfig {

    @XmlElement
    @XmlJavaTypeAdapter(TypedPropertiesAdapter.class)
    private final TypedProperties properties;

    /**
     * Initializes internal fields.
     */
    protected AbstractPropertiesContainer() {
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
