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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.jalphanode.AbstractConfigHolder;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.MembershipConfig;
import org.jalphanode.config.TaskConfig;
import org.jalphanode.notification.Notifier;
import org.jalphanode.scheduler.TaskScheduler;

import com.google.common.base.Preconditions;

/**
 * Abstract membership manager.
 * 
 * @author ribeirux
 * @version $Revision$
 */
public abstract class AbstractMembershipManager extends AbstractConfigHolder {

    private final Notifier notifier;
    private final MasterNodeElectionPolicy masNodeElectionPolicy;
    private final TaskScheduler taskScheduler;
    private final List<ScheduledFuture<?>> runningTasks;

    /**
     * Initializes internal instance variables.
     * 
     * @param config the configuration
     * @param masNodeElectionPolicy the master node election policy
     * @param notifier the notifier
     * @param taskScheduler task scheduler
     */
    public AbstractMembershipManager(final JAlphaNodeConfig config,
            final MasterNodeElectionPolicy masNodeElectionPolicy, final Notifier notifier,
            final TaskScheduler taskScheduler) {
        super(config);
        this.notifier = Preconditions.checkNotNull(notifier, "notifier");
        this.masNodeElectionPolicy = Preconditions.checkNotNull(masNodeElectionPolicy, "masNodeElectionPolicy");
        this.taskScheduler = Preconditions.checkNotNull(taskScheduler, "taskExecutor");
        this.runningTasks = new ArrayList<ScheduledFuture<?>>();
    }

    /**
     * Gets the masNodeElectionPolicy property.
     * 
     * @return the masNodeElectionPolicy property
     */
    protected MasterNodeElectionPolicy getMasNodeElectionPolicy() {
        return this.masNodeElectionPolicy;
    }

    /**
     * This method should be called when the topology changes. Accesses to this method should be synchronized.
     * 
     * @param members new members of the group
     * @param oldMembers old members of the group
     * @param nodeAddress address of the current node
     * @param masterAddress address of the master node
     * @param isMasterNode true if the current node is the master, otherwise false
     */
    protected void topologyChanged(final List<NodeAddress> members, final List<NodeAddress> oldMembers,
            final NodeAddress nodeAddress, final NodeAddress masterAddress, final boolean isMasterNode) {

        if (isMasterNode) {
            if (this.runningTasks.isEmpty()) {
                final List<TaskConfig> tasks = this.getConfig().getTasks().getTask();
                for (final TaskConfig taskConfig : tasks) {
                    final Runnable decoratedTask = this.decorateTask(taskConfig);
                    final ScheduledFuture<?> scheduledTask = this.taskScheduler.schedule(decoratedTask,
                            taskConfig.getScheduleIterator());

                    if (scheduledTask != null) {
                        this.runningTasks.add(scheduledTask);
                    }
                }
            }
        } else {
            for (final ScheduledFuture<?> runningTask : this.runningTasks) {
                runningTask.cancel(false);
            }

            this.runningTasks.clear();
        }

        final MembershipConfig membershipConfs = this.getConfig().getMembership();

        this.notifier.notifyViewChange(membershipConfs.getNodeName(), membershipConfs.getClusterName(), members,
                oldMembers, nodeAddress, masterAddress, isMasterNode);
    }

    private Runnable decorateTask(final TaskConfig taskConfig) {

        final Notifier decorateNotifier = this.notifier;

        return new Runnable() {

            @Override
            public void run() {
                decorateNotifier.beforeTask(taskConfig.getTaskName());
                taskConfig.getTask().onTimeout(taskConfig);
                decorateNotifier.afterTask(taskConfig.getTaskName());
            }
        };
    }

    /**
     * Gets the cluster name.
     * 
     * @return the name of the cluster. Null if running in local mode.
     */
    public String getClusterName() {
        return this.getConfig().getMembership().getClusterName();
    }

    /**
     * Shutdown the membership manager.
     */
    public abstract void shutdown();

    /**
     * Connects to a group. The client is now able to receive views.
     */
    public abstract void connect();

    /**
     * Gets the node address.
     * 
     * @return then node address
     */
    public abstract NodeAddress getNodeAddress();

    /**
     * Gets the master node address.
     * 
     * @return the master node address
     */
    public abstract NodeAddress getMasterNodeAddress();

    /**
     * Checks if this node is a master node.
     * 
     * @return true if the node is a master node, otherwise false
     */
    public abstract boolean isMasterNode();

    /**
     * Gets the members of the cluster.
     * 
     * @return the members of the cluster
     */
    public abstract List<NodeAddress> getMembers();

}
