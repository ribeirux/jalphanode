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
 * Event implementation.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
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
     * @param  componentName  the name of the node
     * @param  clusterName    the name of the cluster
     * @param  localAddress   local address
     * @param  masterAddress  the address of the master node
     * @param  newMembers     the current members of the group
     * @param  oldMembers     the old members of the group
     * @param  master         true if the local node is the master otherwise false
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
     * @return  the componentName property
     */
    @Override
    public String getComponentName() {
        return this.componentName;
    }

    /**
     * Gets the clusterName property.
     *
     * @return  the clusterName property
     */
    @Override
    public String getClusterName() {
        return this.clusterName;
    }

    /**
     * Gets the localAddress property.
     *
     * @return  the localAddress property
     */
    @Override
    public NodeAddress getLocalAddress() {
        return this.localAddress;
    }

    /**
     * Gets the masterAddress property.
     *
     * @return  the masterAddress property
     */
    @Override
    public NodeAddress getMasterAddress() {
        return this.masterAddress;
    }

    /**
     * Gets the newMembers property.
     *
     * @return  the newMembers property
     */
    @Override
    public List<NodeAddress> getNewMembers() {
        return this.newMembers;
    }

    /**
     * Gets the oldMemebers property.
     *
     * @return  the oldMemebers property
     */
    @Override
    public List<NodeAddress> getOldMembers() {
        return this.oldMembers;
    }

    /**
     * Gets the master property.
     *
     * @return  the master property
     */
    @Override
    public Boolean isMaster() {
        return this.master;
    }
}
