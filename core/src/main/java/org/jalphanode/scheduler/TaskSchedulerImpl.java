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

public class TaskSchedulerImpl implements TaskScheduler {

    private static final Log LOG = LogFactory.getLog(TaskSchedulerImpl.class);

    private volatile boolean running = true;

    private final Executor executor;
    private final MembershipManager membershipManager;
    private final Notifier notifier;

    private final BlockingQueue<RecurrentTask> queue;
    private final Thread runner;

    @Inject
    public TaskSchedulerImpl(@SchedulerExecutor final Executor executor, final MembershipManager membershipManager,
            final Notifier notifier) {
        this.executor = Preconditions.checkNotNull(executor, "executor");
        this.membershipManager = Preconditions.checkNotNull(membershipManager, "membershipManager");
        this.notifier = Preconditions.checkNotNull(notifier, "notifier");
        this.queue = new DelayQueue<RecurrentTask>();
        this.runner = new Thread(getRunner(), "Runner thread");
        runner.start();
    }

    @Override
    public void schedule(final TaskConfig task) {
        Preconditions.checkNotNull(task, "task");

        Date nextTimeout = task.getScheduleIterator().next(new Date());
        if (nextTimeout != null) {
            addTask(new RecurrentTask(task, nextTimeout));
        }
    }

    protected void addTask(final RecurrentTask task) {
        if (running) {
            queue.add(task);
        } else {
            LOG.error("Failed adding task to queue.Timer is not running; task: " + task);
        }
    }

    @Override
    public void stop() {
        running = false;
        runner.interrupt();
    }

    private Runnable getRunner() {
        return new Runnable() {

            @Override
            public void run() {

                while (running) {
                    try {
                        RecurrentTask task = queue.take();
                        try {
                            if (membershipManager.isMasterNode()) {
                                executor.execute(task);
                            }
                        } catch (Throwable t) {
                            LOG.error("failed submitting task to thread pool", t);
                        }
                    } catch (InterruptedException interrupted) {
                        /* Allow thread to exit */
                    }
                }
            }
        };
    }

    private class RecurrentTask implements Runnable, Delayed {

        private final TaskConfig taskConfig;
        private volatile Date executionTime;

        public RecurrentTask(final TaskConfig taskConfig, final Date executionTime) {
            this.taskConfig = Preconditions.checkNotNull(taskConfig, "taskConfig");
            this.executionTime = Preconditions.checkNotNull(executionTime, "executionTime");
        }

        /**
         * Note: this class has a natural ordering that is inconsistent with equals."
         */
        @Override
        public int compareTo(final Delayed o) {
            RecurrentTask other = (RecurrentTask) o;
            return executionTime.compareTo(other.executionTime);
        }

        @Override
        public long getDelay(final TimeUnit unit) {
            return unit.convert(executionTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public void run() {
            notifier.beforeTask(taskConfig.getTaskName());

            try {
                taskConfig.getTask().onTimeout(taskConfig);
            } catch (Throwable t) {
                LOG.error(MessageFormat.format("Task execution failed: {0}", t.getMessage()), t);
            }

            notifier.afterTask(taskConfig.getTaskName());

            Date nextTimeout = this.taskConfig.getScheduleIterator().next(executionTime);
            if (nextTimeout != null) {
                executionTime = nextTimeout;
                addTask(this);
            }
        }
    }

}
