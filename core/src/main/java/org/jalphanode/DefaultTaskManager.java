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
 * $Id: DefaultTaskManager.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.annotation.Start;
import org.jalphanode.annotation.Stop;

import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeType;
import org.jalphanode.config.TaskConfig;

import org.jalphanode.inject.InjectorModule;
import org.jalphanode.inject.IsSingletonBindingScopingVisitor;
import org.jalphanode.inject.LifecycleInvocation;

import org.jalphanode.jmx.MBeanRegistry;

import org.jalphanode.notification.Notifier;

import org.jalphanode.scheduler.TaskScheduler;

import org.jalphanode.util.ReflectionUtils;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;

import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;

/**
 * Default task manager.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class DefaultTaskManager implements TaskManager {

    private static final Log LOG = LogFactory.getLog(DefaultTaskManager.class);

    private static final Comparator<LifecycleInvocation> PRIORITY_COMPARATOR = Ordering.natural().onResultOf(
            new Function<LifecycleInvocation, Integer>() {

                @Override
                public Integer apply(final LifecycleInvocation obj) {
                    return obj.getPriority();
                }
            });

    private final AtomicReference<Status> status = new AtomicReference<Status>(Status.INSTANTIATED);

    private final Injector injector;

    /**
     * Constructs a new instance using default configuration. See {@link JAlphaNodeConfig} for more details.
     */
    public DefaultTaskManager() {
        this(new JAlphaNodeType());
    }

    /**
     * Constructs a new instance with specified configuration.
     *
     * @param  config  configuration to use. If null, a default instance is created
     */
    public DefaultTaskManager(final JAlphaNodeConfig config) {
        this(new InjectorModule(Preconditions.checkNotNull(config, "config")));
    }

    /**
     * Constructs a new instance with specified module injector.
     *
     * @param  module  injector module
     */
    public DefaultTaskManager(final InjectorModule module) {
        this.injector = Guice.createInjector(Stage.PRODUCTION, Preconditions.checkNotNull(module, "module"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(final Object listener) {
        this.injector.getInstance(Notifier.class).addListener(Preconditions.checkNotNull(listener, "listener"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(final Object listener) {
        this.injector.getInstance(Notifier.class).removeListener(Preconditions.checkNotNull(listener, "listener"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> getListeners() {
        return this.injector.getInstance(Notifier.class).getListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        DefaultTaskManager.LOG.info("Starting task manager...");

        if (!status.compareAndSet(Status.INSTANTIATED, Status.STARTING)) {
            throw new IllegalStateException("Start not allowed");
        }

        try {

            // start all registered components
            invoke(new Function<Method, Integer>() {
                    public Integer apply(final Method method) {
                        return method.getAnnotation(Start.class).priority();
                    }
                }, Start.class);

            // schedule all tasks
            final JAlphaNodeConfig config = this.injector.getInstance(JAlphaNodeConfig.class);
            final TaskScheduler scheduler = this.injector.getInstance(TaskScheduler.class);
            for (TaskConfig task : config.getTasks().getTask()) {
                scheduler.schedule(task);
            }

            status.set(Status.RUNNING);

            DefaultTaskManager.LOG.info("Task manager started!");
        } catch (RuntimeException r) {
            status.set(Status.FAILED);
            throw r;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        DefaultTaskManager.LOG.info("Shutting down task manager...");

        if (!status.compareAndSet(Status.RUNNING, Status.STOPPING)) {

            // Trying to stop() from FAILED is valid, but may not work
            if (!status.compareAndSet(Status.FAILED, Status.STOPPING)) {
                throw new IllegalStateException("Shutdown not allowed");
            }
        }

        try {

            // unregister all beans
            this.injector.getInstance(MBeanRegistry.class).unregisterAll();

            invoke(new Function<Method, Integer>() {

                    public Integer apply(final Method method) {
                        return method.getAnnotation(Stop.class).priority();
                    }

                }, Stop.class);

            status.set(Status.STOPPED);

            DefaultTaskManager.LOG.info("Shutdown complete!");
        } catch (RuntimeException r) {
            status.set(Status.FAILED);
            throw r;
        }

    }

    private void invoke(final Function<Method, Integer> priorityExtractor,
            final Class<? extends Annotation> annotation) {
        final Map<Key<?>, Binding<?>> bindings = injector.getAllBindings();
        if (!bindings.isEmpty()) {
            final IsSingletonBindingScopingVisitor isSingletonVisitor = new IsSingletonBindingScopingVisitor();
            final PriorityQueue<LifecycleInvocation> toInvoke = new PriorityQueue<LifecycleInvocation>(bindings.size(),
                    PRIORITY_COMPARATOR);

            // collect all methods to start
            for (Entry<Key<?>, Binding<?>> entry : bindings.entrySet()) {

                // check if binding is singleton
                if (entry.getValue().acceptScopingVisitor(isSingletonVisitor)) {
                    Object instance = injector.getInstance(entry.getKey());
                    List<Method> methods = ReflectionUtils.getAllMethods(instance.getClass(), annotation);
                    for (Method method : methods) {
                        toInvoke.add(new LifecycleInvocation(priorityExtractor.apply(method), instance, method));
                    }
                }
            }

            LifecycleInvocation invocation = toInvoke.poll();
            while (invocation != null) {
                invocation.invoke();
                invocation = toInvoke.poll();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAlphaNodeConfig getConfig() {
        return this.injector.getInstance(JAlphaNodeConfig.class);
    }

    @Override
    public Status getStatus() {
        return status.get();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DefaultTaskManager [injector=");
        builder.append(injector);
        builder.append(", state=");
        builder.append(status);
        builder.append(']');
        return builder.toString();
    }
}
