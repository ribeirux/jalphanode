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
 * $Id: TaskType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.executors;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TaskSchedulerConfig;
import org.jalphanode.config.TypedPropertiesConfig;

import org.jalphanode.util.DaemonThreadFactory;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

public class LazyInitializingSchedulerExecutor extends LazyInitializingThreadPoolExecutor {

    private static final Log LOG = LogFactory.getLog(LazyInitializingNotifierExecutor.class);

    protected static final String KEEP_ALIVE_PROPERTY = "keepAlive";
    protected static final int DEFAULT_KEEP_ALIVE = 600000;

    protected static final String THREAD_PRIORITY_PROPERTY = "threadPriority";
    protected static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;

    protected static final String THREAD_PREFIX_PROPERTY = "threadPrefix";
    protected static final String DEFAULT_THREAD_PREFIX = "task-pool";

    protected static final String QUEUE_SIZE_PROPERTY = "queueSize";
    protected static final int DEFAULT_QUEUE_SIZE = 5000;

    protected static final String SHUTDOWN_TIMEOUT_PROPERTY = "shutdownTimeout";
    protected static final long DEFAULT_SHUTDOWN_TIMEOUT = 60000;

    protected static final long RUNNING_TASKS_TIMEOUT = 60000;

    private JAlphaNodeConfig config;

    @Inject
    public LazyInitializingSchedulerExecutor(final JAlphaNodeConfig config) {
        this.config = Preconditions.checkNotNull(config, "config");
    }

    @Override
    protected ThreadPoolExecutor createThreadPool() {
        final TaskSchedulerConfig asyncConfig = this.config.getTaskScheduler();
        final Integer poolSize = asyncConfig.getPoolSize();

        final TypedPropertiesConfig props = asyncConfig.getProperties();
        final int keepAlive = props.getIntProperty(KEEP_ALIVE_PROPERTY, DEFAULT_KEEP_ALIVE);
        final String threadPrefix = props.getProperty(THREAD_PREFIX_PROPERTY, DEFAULT_THREAD_PREFIX);
        final int priority = props.getIntProperty(THREAD_PRIORITY_PROPERTY, DEFAULT_THREAD_PRIORITY);
        final int queueSize = props.getIntProperty(QUEUE_SIZE_PROPERTY, DEFAULT_QUEUE_SIZE);

        final ThreadFactory factory = DaemonThreadFactory.newInstance(threadPrefix, priority);

        final ThreadPoolExecutor pool = new ThreadPoolExecutor(poolSize, poolSize, keepAlive, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize), factory, new ThreadPoolExecutor.CallerRunsPolicy());
        pool.allowCoreThreadTimeOut(true);

        return pool;
    }

    @Override
    protected void cleanup(final ThreadPoolExecutor pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            final TypedPropertiesConfig props = this.config.getTaskScheduler().getProperties();
            final long timeout = props.getLongProperty(SHUTDOWN_TIMEOUT_PROPERTY, DEFAULT_SHUTDOWN_TIMEOUT);

            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks

                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(RUNNING_TASKS_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    LOG.error("Notifier pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            LOG.warn("Shutdown interrupted", ie);

            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();

            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }

    }

}
