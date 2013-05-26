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

import java.util.Date;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.cluster.AbstractMembershipManager;

import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.JAlphaNodeType;

import org.jalphanode.notification.Notifier;

import org.jalphanode.scheduler.TaskScheduler;

import org.joda.time.DateTime;

import com.google.common.base.Preconditions;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

/**
 * Default task manager.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class DefaultTaskManager implements TaskManager {

    private static final Log LOG = LogFactory.getLog(DefaultTaskManager.class);

    private final Injector injector;

    private DateTime startDateTime;
    private Status status;

    // services
    private final AbstractMembershipManager membershipManager;
    private final Notifier notifier;

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
     * Constructs a new instance with specified moudule injector.
     *
     * @param  module  injector module
     */
    public DefaultTaskManager(final InjectorModule module) {
        this.injector = Guice.createInjector(Stage.PRODUCTION, Preconditions.checkNotNull(module, "module"));
        this.membershipManager = this.injector.getInstance(AbstractMembershipManager.class);
        this.notifier = this.injector.getInstance(Notifier.class);
        this.status = Status.INSTANTIATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Period getRunningTime() {
        if (!this.status.isRunning()) {
            throw new IllegalStateException("JAlphaNode is not running");
        }

        return new PeriodImpl(this.startDateTime, new DateTime());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getStartDate() {
        if (!this.status.isRunning()) {
            throw new IllegalStateException("JAlphaNode is not running");
        }

        return this.startDateTime.toDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(final Object listener) {
        this.notifier.addListener(Preconditions.checkNotNull(listener, "listener"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Object> getListeners() {
        return this.notifier.getListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListener(final Object listener) {
        this.notifier.removeListener(Preconditions.checkNotNull(listener, "listener"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        if (!this.status.isStartAllowed()) {
            throw new IllegalStateException("Start not allowed");
        }

        DefaultTaskManager.LOG.info("Starting task manager...");
        this.membershipManager.connect();

        this.startDateTime = new DateTime();
        this.status = Status.RUNNING;
        DefaultTaskManager.LOG.info("Task manager started!");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        if (!this.status.isShutdownAllowed()) {
            throw new IllegalStateException("Shutdown not allowed");
        }

        DefaultTaskManager.LOG.info("Shutting down task manager...");
        membershipManager.shutdown();

        // TODO shutdown thread pool
        this.injector.getInstance(TaskScheduler.class).shutdown();
        this.injector.getInstance(ExecutorService.class).shutdown();
        this.status = Status.TERMINATED;
        DefaultTaskManager.LOG.info("Shutdown complete!");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return this.status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JAlphaNodeConfig getConfig() {
        return this.injector.getInstance(JAlphaNodeConfig.class);
    }

    // TODO generate toString
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DefaultTaskManager [injector=");
        builder.append(injector);
        builder.append(", startDateTime=");
        builder.append(startDateTime);
        builder.append(", status=");
        builder.append(status);
        builder.append("]");

        return builder.toString();
    }

}
