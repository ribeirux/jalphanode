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
 * $Id: ViewChangedEvent.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.notification;

import java.util.List;

import org.jalphanode.cluster.NodeAddress;

/**
 * This event is passed in to any method annotated with {@link org.jalphanode.annotation.ViewChanged}.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public interface ViewChangedEvent extends Event {

    /**
     * Gets the cluster name.
     * 
     * @return the cluster name
     */
    String getClusterName();

    /**
     * Gets the local address.
     * 
     * @return the local address
     */
    NodeAddress getLocalAddress();

    /**
     * Gets the master address.
     * 
     * @return the master address
     */
    NodeAddress getMasterAddress();

    /**
     * Gets the new members of the view.
     * 
     * @return the new view associated with this view change.
     */
    List<NodeAddress> getNewMembers();

    /**
     * Gets the old members of the view.
     * 
     * @return the old members of the view
     */
    List<NodeAddress> getOldMembers();

    /**
     * Checks if the current node is the master node.
     * 
     * @return true if the current node is the master node, otherwise false
     */
    Boolean isMaster();
}
