/**
 * Copyright 2011 Pedro Ribeiro
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jalphanode;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import org.jalphanode.annotation.Start;
import org.jalphanode.annotation.Stop;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeType;
import org.jalphanode.inject.InjectorModule;
import org.jalphanode.inject.IsSingletonBindingScopingVisitor;
import org.jalphanode.inject.LifecycleInvocation;
import org.jalphanode.jmx.MBeanRegistry;
import org.jalphanode.notification.Notifier;
import org.jalphanode.scheduler.TaskScheduler;
import org.jalphanode.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Default task manager.
 *
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class DefaultTaskManager implements TaskManager {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTaskManager.class);

    private static final Comparator<LifecycleInvocation> PRIORITY_COMPARATOR = Ordering
            .natural().onResultOf(LifecycleInvocation::getPriority);

    private final AtomicReference<Status> status = new AtomicReference<>(Status.INSTANTIATED);

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
        LOG.info("Starting task manager");

        if (!status.compareAndSet(Status.INSTANTIATED, Status.STARTING)) {
            throw new IllegalStateException("Start not allowed");
        }

        try {

            // start all registered components
            invoke(method -> method.getAnnotation(Start.class).priority(), Start.class);

            // schedule all tasks
            final JAlphaNodeConfig config = this.injector.getInstance(JAlphaNodeConfig.class);
            final TaskScheduler scheduler = this.injector.getInstance(TaskScheduler.class);
            config.getTasks().getTask().forEach(scheduler::schedule);

            status.set(Status.RUNNING);

            LOG.info("Task manager started");
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
        LOG.info("Shutting down task manager");

        if (!status.compareAndSet(Status.RUNNING, Status.STOPPING)) {

            // Trying to stop() from FAILED is valid, but may not work
            if (!status.compareAndSet(Status.FAILED, Status.STOPPING)) {
                throw new IllegalStateException("Shutdown not allowed");
            }
        }

        try {

            // unregister all beans
            this.injector.getInstance(MBeanRegistry.class).unregisterAll();

            invoke(method -> method.getAnnotation(Stop.class).priority(), Stop.class);

            status.set(Status.STOPPED);

            LOG.info("Shutdown complete");
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
            final Queue<LifecycleInvocation> toInvoke = new PriorityQueue<>(bindings.size(),
                    PRIORITY_COMPARATOR);

            // collect all methods to start
            bindings.entrySet().stream()
                    // check if binding is singleton
                    .filter(entry -> entry.getValue().acceptScopingVisitor(isSingletonVisitor))
                    .forEach(entry -> {
                        final Object instance = injector.getInstance(entry.getKey());
                        final List<Method> methods = ReflectionUtils.getAllMethods(instance.getClass(), annotation);
                        toInvoke.addAll(methods.stream()
                                .map(method -> new LifecycleInvocation(
                                        priorityExtractor.apply(method), instance, method))
                                .collect(Collectors.toList()));
                    });

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
