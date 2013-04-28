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
 * $Id: BinderModule.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

import java.util.concurrent.ExecutorService;

import org.jalphanode.cluster.AbstractMembershipManager;
import org.jalphanode.cluster.MasterNodeElectionPolicy;
import org.jalphanode.cluster.SimpleMasterNodeElectionPolicy;
import org.jalphanode.cluster.jgroups.ChannelProvider;
import org.jalphanode.cluster.jgroups.JGroupsMembershipManager;

import org.jalphanode.config.JAlphaNodeConfig;

import org.jalphanode.notification.AsyncNotificationExecutorProvider;
import org.jalphanode.notification.Notifier;
import org.jalphanode.notification.NotifierImpl;

import org.jalphanode.scheduler.RecurrentTaskScheduler;
import org.jalphanode.scheduler.TaskScheduler;

import org.jgroups.Channel;

import com.google.common.base.Preconditions;

import com.google.inject.AbstractModule;

/**
 * Binds interfaces to the implementation.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class InjectorModule extends AbstractModule {

    private final JAlphaNodeConfig config;

    /**
     * Creates a new binder instance.
     *
     * @param  config  the configuration
     */
    public InjectorModule(final JAlphaNodeConfig config) {
        this.config = Preconditions.checkNotNull(config, "config");
    }

    @Override
    protected void configure() {

        // bind config
        this.bindConfig();

        // bind notifier
        this.bindNotifier();

        // bind task scheduler
        this.bindTaskScheduler();

        // Bind async executor
        this.bindAsyncExecutor();

        // Bind membership manager
        this.bindMembershipManager();

        // bind membership channel
        this.bindMembershipChannel();

        // Bind master node election policy
        this.bindMasterNodeElectionPolicy();

    }

    protected void bindConfig() {
        this.bind(JAlphaNodeConfig.class).toInstance(this.config);
    }

    protected void bindNotifier() {
        this.bind(Notifier.class).to(NotifierImpl.class).asEagerSingleton();
    }

    protected void bindTaskScheduler() {
        this.bind(TaskScheduler.class).to(RecurrentTaskScheduler.class).asEagerSingleton();
    }

    protected void bindAsyncExecutor() {
        this.bind(ExecutorService.class).toProvider(AsyncNotificationExecutorProvider.class).asEagerSingleton();
    }

    protected void bindMembershipManager() {
        this.bind(AbstractMembershipManager.class).to(JGroupsMembershipManager.class).asEagerSingleton();
    }

    protected void bindMembershipChannel() {
        this.bind(Channel.class).toProvider(ChannelProvider.class).asEagerSingleton();
    }

    protected void bindMasterNodeElectionPolicy() {
        this.bind(MasterNodeElectionPolicy.class).to(SimpleMasterNodeElectionPolicy.class);
    }

}
