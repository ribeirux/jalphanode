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
 * Membership configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "membershipType")
public class MembershipType extends AbstractPropertiesContainer implements MembershipConfig {

    /**
     * Default node name.
     */
    public static final String DEFAULT_NODE_NAME = "jalphanode-node";

    /**
     * Default cluster name.
     */
    public static final String DEFAULT_CLUSTER_NAME = "jalphanode-cluster";

    @XmlAttribute
    private String nodeName;

    @XmlAttribute
    private String clusterName;

    /**
     * Creates a new membership manager config type.
     */
    public MembershipType() {
        super();
        this.nodeName = MembershipType.DEFAULT_NODE_NAME;
        this.clusterName = MembershipType.DEFAULT_CLUSTER_NAME;
    }

    /**
     * Gets the nodeName property.
     *
     * @return  the nodeName property
     */
    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    /**
     * Sets the nodeName property.
     *
     * @param  nodeName  the nodeName to set
     */
    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Gets the value of the clusterName property.
     *
     * @return  possible object is {@link String }
     */
    @Override
    public String getClusterName() {
        return this.clusterName;
    }

    /**
     * Sets the value of the clusterName property.
     *
     * @param  clusterName  allowed object is {@link String }
     */
    public void setClusterName(final String clusterName) {
        this.clusterName = clusterName;
    }

}
