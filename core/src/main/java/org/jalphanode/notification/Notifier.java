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
