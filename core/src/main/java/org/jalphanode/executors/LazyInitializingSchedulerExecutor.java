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
package org.jalphanode.executors;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TaskSchedulerConfig;
import org.jalphanode.config.TypedPropertiesConfig;

import org.jalphanode.util.DaemonThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

public class LazyInitializingSchedulerExecutor extends LazyInitializingThreadPoolExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(LazyInitializingNotifierExecutor.class);

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

    private final JAlphaNodeConfig config;

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
        LOG.info("Shutting down scheduler executor");

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
