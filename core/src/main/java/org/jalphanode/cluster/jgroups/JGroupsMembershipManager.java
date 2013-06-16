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
 * $Id: JGroupsMembershipManager.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.cluster.jgroups;

import java.io.InputStream;
import java.io.OutputStream;

import java.text.MessageFormat;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.cluster.MasterNodeElectionPolicy;
import org.jalphanode.cluster.MembershipException;
import org.jalphanode.cluster.MembershipManager;
import org.jalphanode.cluster.NodeAddress;

import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.MembershipConfig;

import org.jalphanode.jmx.annotation.MBean;
import org.jalphanode.jmx.annotation.ManagedAttribute;

import org.jalphanode.notification.Notifier;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import com.google.inject.Inject;

/**
 * JGroups membership manager.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@MBean(objectName = JGroupsMembershipManager.OBJECT_NAME, description = "Component that manages group members")
public class JGroupsMembershipManager implements MembershipManager, Receiver {

    private static final Log LOG = LogFactory.getLog(JGroupsMembershipManager.class);

    public static final String OBJECT_NAME = "MembershipManager";

    private final CountDownLatch connectedLatch = new CountDownLatch(1);
    private final ReentrantLock lock = new ReentrantLock();

    private final JAlphaNodeConfig config;
    private final Channel channel;
    private final MasterNodeElectionPolicy masterNodeElectionPolicy;
    private final Notifier notifier;

    private volatile boolean master = false;
    private volatile NodeAddress address;
    private volatile NodeAddress masterAddress;
    private volatile List<NodeAddress> members = Collections.emptyList();

    /**
     * Creates a new membership manager.
     *
     * @param  config                    configuration
     * @param  channel                   jgroups channel
     * @param  masterNodeElectionPolicy  master node election policy
     * @param  notifier                  notifier instance
     */
    @Inject
    public JGroupsMembershipManager(final JAlphaNodeConfig config, final Channel channel,
            final MasterNodeElectionPolicy masterNodeElectionPolicy, final Notifier notifier) {
        this.config = Preconditions.checkNotNull(config, "config");
        this.channel = Preconditions.checkNotNull(channel);
        this.masterNodeElectionPolicy = Preconditions.checkNotNull(masterNodeElectionPolicy,
                "masterNodeElectionPolicy");
        this.notifier = Preconditions.checkNotNull(notifier, "notifier");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ManagedAttribute(name = "Cluster name", description = "Returns the name of the cluster")
    public String getClusterName() {
        return this.config.getMembership().getClusterName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ManagedAttribute(name = "Node address", description = "Returns the node address")
    public NodeAddress getNodeAddress() {
        return this.address;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeAddress getMasterNodeAddress() {
        return this.masterAddress;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ManagedAttribute(name = "Master node", description = "Checks whether the node is the master node of the group")
    public boolean isMasterNode() {
        return this.master;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @ManagedAttribute(name = "Group members", description = "Returns the group members")
    public List<NodeAddress> getMembers() {
        return this.members;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() {
        lock.lock();
        try {
            if (this.channel.isConnected()) {
                throw new IllegalStateException("Channel is already connected.");
            }

            LOG.info("Starting JGroups Channel...");

            final int randomInRange = new Random().nextInt();

            this.channel.setName(this.config.getMembership().getNodeName() + "-" + randomInRange);
            this.channel.setReceiver(this);

            try {
                this.channel.connect(this.getClusterName());
                this.connectedLatch.await();
            } catch (final InterruptedException e) {
                LOG.error("Connection thread interrupted while waiting for members to be set", e);

                // Preserve interrupt status
                Thread.currentThread().interrupt();
            } catch (final Exception e) {
                throw new MembershipException("Unable to start JGroups Channel", e);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        lock.lock();
        try {
            if (this.channel.isOpen()) {
                LOG.info("Disconnecting and closing JGroups Channel");
                this.channel.disconnect();
                this.channel.close();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void viewAccepted(final View newView) {
        LOG.info(MessageFormat.format("Received new cluster view: {0}", newView));

        final List<Address> newMembers = newView.getMembers();

        if (!newMembers.isEmpty()) {

            // no need for synchronization because this method is invoked sequentially.
            // Instance variable "members" cannot change concurrently
            final List<NodeAddress> oldMembers = this.members;

            // Load the address when the first non empty view is installed, to cache NodeAddress instead of instantiate
            // a new NodeAddress every time we need the address.
            // The connect method is synchronized and is waiting for count down, so, no one can shutdown this
            // because shutdown is also synchronized. (safe to initialized addresses from channel address)
            // We need to fill the address in a safety way. If we make a lazy load of the address, we need
            // synchronization because the address can be initialized by several threads.
            // If this address were initialized on connect method, there is no guarantees that this method will see
            // addresses on an safety manner, so I think this is the right place to initialize this instance variable.

            if (oldMembers.isEmpty()) {
                this.address = new JGroupsAddress(this.channel.getAddress());
                LOG.info(MessageFormat.format("Local address is {0}.", this.address));
            }

            this.members = this.fromJGroupsAddressList(newMembers);

            // the list of members is immutable, thus, thread safe
            this.masterAddress = this.masterNodeElectionPolicy.elect(this.members);

            this.master = this.masterAddress.equals(this.address);

            // wake up the start thread
            if (oldMembers.isEmpty()) {
                this.connectedLatch.countDown();
            }

            // notify listeners
            final MembershipConfig membershipConfs = this.config.getMembership();
            this.notifier.notifyViewChange(membershipConfs.getNodeName(), membershipConfs.getClusterName(), members,
                oldMembers, this.getNodeAddress(), masterAddress, this.master);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void suspect(final Address addr) {
        LOG.warn(MessageFormat.format("Member {0} is suspected of having crashed.", addr));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(final Message msg) {
        // this functionality is not used.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void block() {
        // this functionality is not used.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unblock() {
        // this functionality is not used.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getState(final OutputStream output) throws Exception {
        throw new UnsupportedOperationException("This functionality is not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(final InputStream input) throws Exception {
        throw new UnsupportedOperationException("This functionality is not supported");
    }

    private List<NodeAddress> fromJGroupsAddressList(final List<Address> list) {
        final ImmutableList.Builder<NodeAddress> builder = ImmutableList.builder();
        for (final Address addr : list) {
            builder.add(new JGroupsAddress(addr));
        }

        return builder.build();
    }

}
