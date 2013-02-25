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
package org.jalphanode.scheduler;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.jalphanode.AbstractConfigHolder;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TaskSchedulerConfig;
import org.jalphanode.config.TypedPropertiesConfig;
import org.jalphanode.util.DaemonThreadFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * Recurrent task scheduler.
 * 
 * @author ribeirux
 * @version $Revision$
 */
public class RecurrentTaskScheduler extends AbstractConfigHolder implements TaskScheduler {

    private static final String DEFAULT_THREAD_PREFIX = "task-pool";
    private static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;

    private final ScheduledExecutorService scheduler;

    /**
     * Creates a new schedule.
     * 
     * @param config the configuration
     */
    @Inject
    public RecurrentTaskScheduler(final JAlphaNodeConfig config) {
        super(config);
        this.scheduler = this.buildScheduledExecutor(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduledFuture<?> schedule(final Runnable command, final ScheduleIterator scheduleIterator) {

        Preconditions.checkNotNull(command, "command");
        Preconditions.checkNotNull(scheduleIterator, "scheduleIterator");

        return new RecurrentScheduledFuture(command, scheduleIterator, this.scheduler).schedule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        this.scheduler.shutdown();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Runnable> shutdownNow() {
        return this.scheduler.shutdownNow();
    }

    private ScheduledExecutorService buildScheduledExecutor(final JAlphaNodeConfig config) {

        final TaskSchedulerConfig taskSchedulerConfig = config.getTaskScheduler();
        final TypedPropertiesConfig props = taskSchedulerConfig.getProperties();

        return new ScheduledThreadPoolExecutor(taskSchedulerConfig.getCorePoolSize(), DaemonThreadFactory.newInstance(
                props.getProperty("threadNamePrefix", RecurrentTaskScheduler.DEFAULT_THREAD_PREFIX),
                props.getIntProperty("threadPriority", RecurrentTaskScheduler.DEFAULT_THREAD_PRIORITY)));
    }
}
