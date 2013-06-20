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
package org.jalphanode.scheduler;

import java.text.MessageFormat;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.annotation.SchedulerExecutor;

import org.jalphanode.cluster.MembershipManager;

import org.jalphanode.config.TaskConfig;

import org.jalphanode.notification.Notifier;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

public class TaskSchedulerImpl implements TaskScheduler, Runnable {

    private static final Log LOG = LogFactory.getLog(TaskSchedulerImpl.class);

    private final BlockingQueue<RecurrentTask> queue = new DelayQueue<RecurrentTask>();

    private volatile boolean running = true;

    private final Executor executor;
    private final MembershipManager membershipManager;
    private final Notifier notifier;

    private final Thread runner;

    @Inject
    public TaskSchedulerImpl(@SchedulerExecutor final Executor executor, final MembershipManager membershipManager,
            final Notifier notifier) {
        this.executor = Preconditions.checkNotNull(executor, "executor");
        this.membershipManager = Preconditions.checkNotNull(membershipManager, "membershipManager");
        this.notifier = Preconditions.checkNotNull(notifier, "notifier");
        this.runner = new Thread(this, "Runner thread");
        runner.start();
    }

    @Override
    public void schedule(final TaskConfig task) {
        Preconditions.checkNotNull(task, "task");

        // TODO return future
        new RecurrentTask(task).schedule();
    }

    @Override
    public void stop() {
        running = false;
        runner.interrupt();
    }

    public void run() {
        try {
            while (running) {
                RecurrentTask task = queue.take();
                try {
                    if (membershipManager.isMasterNode()) {
                        executor.execute(task);
                    } else {

                        // if it'not the master, just schedule the task
                        task.schedule();
                    }
                } catch (Throwable t) {
                    LOG.error("failed submitting task to thread pool", t);
                }
            }
        } catch (InterruptedException interrupted) {
            LOG.info("Task scheduler interrupted");

            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }
    }

    private class RecurrentTask implements Runnable, Delayed {

        private final TaskConfig taskConfig;
        private volatile Date executionTime;

        public RecurrentTask(final TaskConfig taskConfig) {
            this.taskConfig = Preconditions.checkNotNull(taskConfig, "taskConfig");
            this.executionTime = new Date();
        }

        /**
         * Note: this class has a natural ordering that is inconsistent with equals."
         */
        @Override
        public int compareTo(final Delayed o) {
            final RecurrentTask other = (RecurrentTask) o;
            return executionTime.compareTo(other.executionTime);
        }

        @Override
        public long getDelay(final TimeUnit unit) {
            return unit.convert(executionTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public void run() {
            String taskName = taskConfig.getTaskName();

            notifier.beforeTask(taskName);

            try {
                taskConfig.getTask().onTimeout(taskConfig);
            } catch (Throwable t) {
                LOG.error(MessageFormat.format("Task execution failed: {0}", t.getMessage()), t);
            }

            notifier.afterTask(taskConfig.getTaskName());

            schedule();
        }

        public void schedule() {
            final Date nextTimeout = this.taskConfig.getScheduleIterator().next(executionTime);
            if (nextTimeout != null && running) {
                executionTime = nextTimeout;
                queue.add(this);
            }
        }
    }
}
