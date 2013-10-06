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
 * Async executor configuration.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asyncNotificationExecutorType")
public class AsyncNotificationExecutorType extends AbstractPropertiesContainer
    implements AsyncNotificationExecutorConfig {

    /**
     * Default pool size.
     */
    public static final int DEFAULT_POOL_SIZE = 10;

    @XmlAttribute
    private Integer poolSize;

    /**
     * Creates a new async executor with default configuration.
     */
    public AsyncNotificationExecutorType() {
        super();
        this.poolSize = AsyncNotificationExecutorType.DEFAULT_POOL_SIZE;
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
