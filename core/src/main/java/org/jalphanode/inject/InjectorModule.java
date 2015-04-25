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
package org.jalphanode.inject;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
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
import org.jalphanode.jmx.DefaultMBeanRegistry;
import org.jalphanode.jmx.MBeanAnnotationScanner;
import org.jalphanode.jmx.MBeanMetadata;
import org.jalphanode.jmx.MBeanRegistry;
import org.jalphanode.jmx.ResourceDynamicMBean;
import org.jalphanode.notification.Notifier;
import org.jalphanode.notification.NotifierImpl;
import org.jalphanode.scheduler.TaskScheduler;
import org.jalphanode.scheduler.TaskSchedulerImpl;
import org.jgroups.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Binds interfaces to the implementation.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class InjectorModule extends AbstractModule {

    private static final Logger LOG = LoggerFactory.getLogger(InjectorModule.class);

    private final JAlphaNodeConfig config;
    private final MBeanRegistry mBeanRegistry;

    /**
     * Creates a new binder instance.
     *
     * @param  config  the configuration
     */
    public InjectorModule(final JAlphaNodeConfig config) {
        this(config, new DefaultMBeanRegistry());
    }

    public InjectorModule(final JAlphaNodeConfig config, final MBeanRegistry mBeanRegistry) {
        this.config = Preconditions.checkNotNull(config, "config");
        this.mBeanRegistry = Preconditions.checkNotNull(mBeanRegistry, "mBeanRegistry");
    }

    public JAlphaNodeConfig getConfig() {
        return config;
    }

    @Override
    protected void configure() {

        // bind config
        this.bindConfig();

        // bind MBean registry
        this.bindMBeanRegistry();

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

    protected void bindMBeanRegistry() {
        this.bind(MBeanRegistry.class).toInstance(this.mBeanRegistry);
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
                        encounter.register((InjectionListener<I>) injectee -> {
                            try {
                                final ResourceDynamicMBean dynamicMBean = new ResourceDynamicMBean(injectee,
                                        metadata);

                                mBeanRegistry.register(dynamicMBean, metadata.getObjectName());
                            } catch (Exception e) {
                                LOG.error("Could not register MBean {}", metadata.getObjectName(), e);
                            }
                        });
                    }
                }
            });
    }
}
