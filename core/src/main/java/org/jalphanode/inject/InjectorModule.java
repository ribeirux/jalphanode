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
package org.jalphanode.inject;

import java.lang.management.ManagementFactory;

import java.util.concurrent.Executor;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jalphanode.annotation.NotifierExecutor;
import org.jalphanode.annotation.SchedulerExecutor;

import org.jalphanode.cluster.MasterNodeElectionPolicy;
import org.jalphanode.cluster.MembershipManager;
import org.jalphanode.cluster.SimpleMasterNodeElectionPolicy;
import org.jalphanode.cluster.jgroups.ChannelProvider;
import org.jalphanode.cluster.jgroups.JGroupsMembershipManager;

import org.jalphanode.config.JAlphaNodeConfig;

import org.jalphanode.executors.LazyInitializingNotifierExecutor;
import org.jalphanode.executors.LazyInitializingSchedulerExecutor;

import org.jalphanode.jmx.MBeanAnnotationScanner;
import org.jalphanode.jmx.MBeanMetadata;
import org.jalphanode.jmx.ResourceDynamicMBean;

import org.jalphanode.notification.Notifier;
import org.jalphanode.notification.NotifierImpl;

import org.jalphanode.scheduler.TaskScheduler;
import org.jalphanode.scheduler.TaskSchedulerImpl;

import org.jgroups.Channel;

import com.google.common.base.Preconditions;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * Binds interfaces to the implementation.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class InjectorModule extends AbstractModule {

    private final JAlphaNodeConfig config;
    private final MBeanServer mBeanServer;

    /**
     * Creates a new binder instance.
     *
     * @param  config  the configuration
     */
    public InjectorModule(final JAlphaNodeConfig config) {
        this(config, ManagementFactory.getPlatformMBeanServer());
    }

    /**
     * Creates a new binder instance.
     *
     * @param  config  the configuration
     */
    public InjectorModule(final JAlphaNodeConfig config, final MBeanServer mBeanServer) {
        this.config = Preconditions.checkNotNull(config, "config");
        this.mBeanServer = Preconditions.checkNotNull(mBeanServer, "mBeanServer");
    }

    public JAlphaNodeConfig getConfig() {
        return config;
    }

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    @Override
    protected void configure() {

        // bind config
        this.bindConfig();

        // bind MBean server
        this.bindMBeanServer();

        // bind notifier
        this.bindNotifier();

        // Bind async executor
        this.bindAsyncNotifierExecutor();

        // task scheduler
        this.bindTaskScheduler();

        // Bind scheduler executor
        this.bindSchedulerExecutor();

        // Bind membership manager
        this.bindMembershipManager();

        // bind membership channel
        this.bindMembershipChannel();

        // Bind master node election policy
        this.bindMasterNodeElectionPolicy();

        // Bind MBean annotation listener
        this.bindMBeanListener();
    }

    protected void bindConfig() {
        this.bind(JAlphaNodeConfig.class).toInstance(this.config);
    }

    protected void bindMBeanServer() {
        this.bind(MBeanServer.class).toInstance(this.mBeanServer);
    }

    protected void bindNotifier() {
        this.bind(Notifier.class).to(NotifierImpl.class).asEagerSingleton();
    }

    protected void bindAsyncNotifierExecutor() {
        this.bind(Executor.class).annotatedWith(NotifierExecutor.class).to(LazyInitializingNotifierExecutor.class)
            .asEagerSingleton();
    }

    protected void bindTaskScheduler() {
        this.bind(TaskScheduler.class).to(TaskSchedulerImpl.class).asEagerSingleton();
    }

    protected void bindSchedulerExecutor() {
        this.bind(Executor.class).annotatedWith(SchedulerExecutor.class).to(LazyInitializingSchedulerExecutor.class)
            .asEagerSingleton();
    }

    protected void bindMembershipManager() {
        this.bind(MembershipManager.class).to(JGroupsMembershipManager.class).asEagerSingleton();
    }

    protected void bindMembershipChannel() {
        this.bind(Channel.class).toProvider(ChannelProvider.class).asEagerSingleton();
    }

    protected void bindMasterNodeElectionPolicy() {
        this.bind(MasterNodeElectionPolicy.class).to(SimpleMasterNodeElectionPolicy.class);
    }

    protected void bindMBeanListener() {
        final MBeanAnnotationScanner scanner = new MBeanAnnotationScanner();
        this.bindListener(Matchers.any(), new TypeListener() {

                @Override
                public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
                    final MBeanMetadata metadata = scanner.scan(type.getRawType());

                    if (metadata != null) {
                        encounter.register(new InjectionListener<I>() {

                                @Override
                                public void afterInjection(final I injectee) {
                                    try {
                                        final ResourceDynamicMBean dynamicMBean = new ResourceDynamicMBean(injectee,
                                                metadata);

                                        mBeanServer.registerMBean(dynamicMBean,
                                            new ObjectName(metadata.getObjectName()));
                                    } catch (Exception e) {
                                        encounter.addError(e);
                                    }
                                }
                            });
                    }
                }
            });
    }
}
