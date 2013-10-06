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
