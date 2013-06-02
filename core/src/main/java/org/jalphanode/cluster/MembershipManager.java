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
 * $Id$
 *******************************************************************************/
package org.jalphanode.cluster;

import java.util.List;

import org.jalphanode.annotation.Start;
import org.jalphanode.annotation.Stop;

/**
 * Membership manager.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public interface MembershipManager {

    /**
     * Gets the cluster name.
     *
     * @return  the name of the cluster. Null if running in local mode.
     */
    String getClusterName();

    /**
     * Gets the node address.
     *
     * @return  then node address
     */
    NodeAddress getNodeAddress();

    /**
     * Gets the master node address.
     *
     * @return  the master node address
     */
    NodeAddress getMasterNodeAddress();

    /**
     * Checks if this node is a master node.
     *
     * @return  true if the node is a master node, otherwise false
     */
    boolean isMasterNode();

    /**
     * Gets the members of the cluster.
     *
     * @return  the members of the cluster
     */
    List<NodeAddress> getMembers();

    /**
     * Connects to a group. The client is now able to receive views.
     */
    @Start
    void connect();

    /**
     * Shutdown the membership manager.
     */
    @Stop(priority = 150)
    void shutdown();
}
