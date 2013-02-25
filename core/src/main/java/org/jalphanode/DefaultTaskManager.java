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
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class DefaultTaskManager extends AbstractConfigHolder implements TaskManager {

    private static final Log LOG = LogFactory.getLog(DefaultTaskManager.class);

    private final Injector injector;

    private DateTime startDateTime;

    private Status status;

    /**
     * Constructs a new instance using default configuration. See {@link JAlphaNodeConfig} for more details.
     */
    public DefaultTaskManager() {
        this(null);
    }

    /**
     * Constructs a new instance with specified configuration.
     * 
     * @param config configuration to use. If null, a default instance is created
     */
    public DefaultTaskManager(final JAlphaNodeConfig config) {
        this(config, null);
    }

    /**
     * Constructs a new instance with specified configuration and guice injector.
     * 
     * @param config configuration to use. If null, a default instance is created. See {@link JAlphaNodeConfig} for
     *            details of these defaults.
     * @param injector guice injector
     */
    public DefaultTaskManager(final JAlphaNodeConfig config, final Injector injector) {
        super(config == null ? new JAlphaNodeType() : config);
        this.injector = (injector == null ? this.createDefaultInjector() : injector);
        this.status = Status.INSTANTIATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Period getRunningTime() {
        if (this.status.isRunning()) {
            return new PeriodImpl(this.startDateTime, new DateTime());
        } else {
            throw new IllegalStateException("JAlphaNode is not running");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getStartDate() {
        if (this.status.isRunning()) {
            return this.startDateTime.toDate();
        } else {
            throw new IllegalStateException("JAlphaNode is not running");
        }
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
    public Set<Object> getListeners() {
        return this.injector.getInstance(Notifier.class).getListeners();
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
    public void start() {
        if (this.status.isStartAllowed()) {
            DefaultTaskManager.LOG.info("Starting task manager...");
            this.injector.getInstance(AbstractMembershipManager.class).connect();
            this.startDateTime = new DateTime();
            this.status = Status.RUNNING;
            DefaultTaskManager.LOG.info("Task manager started!");
        } else {
            throw new IllegalStateException("Start not allowed");
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        if (this.status.isShutdownAllowed()) {
            DefaultTaskManager.LOG.info("Shutting down task manager...");
            this.injector.getInstance(AbstractMembershipManager.class).shutdown();
            this.injector.getInstance(TaskScheduler.class).shutdown();
            this.injector.getInstance(ExecutorService.class).shutdown();
            this.status = Status.TERMINATED;
            DefaultTaskManager.LOG.info("Shutdown complete!");
        } else {
            throw new IllegalStateException("Shutdown not allowed");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Status getStatus() {
        return this.status;
    }

    private Injector createDefaultInjector() {
        return Guice.createInjector(Stage.PRODUCTION, new BinderModule(this.getConfig()));
    }
}
