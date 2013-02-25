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
 * $Id: EventImpl.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.notification;

import java.util.List;

import org.jalphanode.cluster.NodeAddress;

/**
 * Event implementation.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class EventImpl implements ViewChangedEvent {

    private final String componentName;

    private final String clusterName;

    private final NodeAddress localAddress;

    private final NodeAddress masterAddress;

    private final List<NodeAddress> newMembers;

    private final List<NodeAddress> oldMembers;

    private final Boolean master;

    /**
     * Creates a new immutable event.
     * 
     * @param componentName the name of the node
     * @param clusterName the name of the cluster
     * @param localAddress local address
     * @param masterAddress the address of the master node
     * @param newMembers the current members of the group
     * @param oldMembers the old members of the group
     * @param master true if the local node is the master otherwise false
     */
    public EventImpl(final String componentName, final String clusterName, final NodeAddress localAddress,
            final NodeAddress masterAddress, final List<NodeAddress> newMembers, final List<NodeAddress> oldMembers,
            final Boolean master) {
        this.componentName = componentName;
        this.clusterName = clusterName;
        this.localAddress = localAddress;
        this.masterAddress = masterAddress;
        this.newMembers = newMembers;
        this.oldMembers = oldMembers;
        this.master = master;
    }

    /**
     * Gets the componentName property.
     * 
     * @return the componentName property
     */
    @Override
    public String getComponentName() {
        return this.componentName;
    }

    /**
     * Gets the clusterName property.
     * 
     * @return the clusterName property
     */
    @Override
    public String getClusterName() {
        return this.clusterName;
    }

    /**
     * Gets the localAddress property.
     * 
     * @return the localAddress property
     */
    @Override
    public NodeAddress getLocalAddress() {
        return this.localAddress;
    }

    /**
     * Gets the masterAddress property.
     * 
     * @return the masterAddress property
     */
    @Override
    public NodeAddress getMasterAddress() {
        return this.masterAddress;
    }

    /**
     * Gets the newMembers property.
     * 
     * @return the newMembers property
     */
    @Override
    public List<NodeAddress> getNewMembers() {
        return this.newMembers;
    }

    /**
     * Gets the oldMemebers property.
     * 
     * @return the oldMemebers property
     */
    @Override
    public List<NodeAddress> getOldMembers() {
        return this.oldMembers;
    }

    /**
     * Gets the master property.
     * 
     * @return the master property
     */
    @Override
    public Boolean isMaster() {
        return this.master;
    }
}
