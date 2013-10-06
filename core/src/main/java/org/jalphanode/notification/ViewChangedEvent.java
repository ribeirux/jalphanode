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
package org.jalphanode.notification;

import java.util.List;

import org.jalphanode.cluster.NodeAddress;

/**
 * This event is passed in to any method annotated with {@link org.jalphanode.annotation.ViewChanged}.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface ViewChangedEvent extends Event {

    /**
     * Gets the cluster name.
     *
     * @return  the cluster name
     */
    String getClusterName();

    /**
     * Gets the local address.
     *
     * @return  the local address
     */
    NodeAddress getLocalAddress();

    /**
     * Gets the master address.
     *
     * @return  the master address
     */
    NodeAddress getMasterAddress();

    /**
     * Gets the new members of the view.
     *
     * @return  the new view associated with this view change.
     */
    List<NodeAddress> getNewMembers();

    /**
     * Gets the old members of the view.
     *
     * @return  the old members of the view
     */
    List<NodeAddress> getOldMembers();

    /**
     * Checks if the current node is the master node.
     *
     * @return  true if the current node is the master node, otherwise false
     */
    Boolean isMaster();
}
