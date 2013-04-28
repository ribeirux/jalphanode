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
 * $Id: Notifier.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.notification;

import java.util.List;

import org.jalphanode.cluster.NodeAddress;

/**
 * Notifications for the task executor.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface Notifier extends Listenable {

    /**
     * Notifies all registered listeners of a viewChange event. Note that viewChange notifications are ALWAYS sent
     * immediately.
     *
     * @param  nodeName       node name
     * @param  clusterName    cluster name
     * @param  newMembers     the new members of the view
     * @param  oldMembers     the old members of the view
     * @param  myAddress      node address
     * @param  masterAddress  master address
     * @param  isMaster       master status
     */
    void notifyViewChange(String nodeName, String clusterName, List<NodeAddress> newMembers,
            List<NodeAddress> oldMembers, NodeAddress myAddress, NodeAddress masterAddress, boolean isMaster);

    /**
     * Listener called before running the task.
     *
     * @param  taskName  task name
     */
    void beforeTask(String taskName);

    /**
     * Listener called after running the task.
     *
     * @param  taskName  task name
     */
    void afterTask(String taskName);

}
