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
 * $Id: MembershipType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Membership configuration.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
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
     * @return the nodeName property
     */
    @Override
    public String getNodeName() {
        return this.nodeName;
    }

    /**
     * Sets the nodeName property.
     * 
     * @param nodeName the nodeName to set
     */
    public void setNodeName(final String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * Gets the value of the clusterName property.
     * 
     * @return possible object is {@link String }
     */
    @Override
    public String getClusterName() {
        return this.clusterName;
    }

    /**
     * Sets the value of the clusterName property.
     * 
     * @param clusterName allowed object is {@link String }
     */
    public void setClusterName(final String clusterName) {
        this.clusterName = clusterName;
    }

}
