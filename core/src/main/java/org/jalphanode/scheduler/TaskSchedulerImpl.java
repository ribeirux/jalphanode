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
package org.jalphanode.scheduler;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.jalphanode.annotation.SchedulerExecutor;

import org.jalphanode.cluster.MembershipManager;

import org.jalphanode.config.TaskConfig;

import org.jalphanode.notification.Notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;

public class TaskSchedulerImpl implements TaskScheduler, Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(TaskSchedulerImpl.class);

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
                LOG.error("Task execution failed: {}", t.getMessage(), t);
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
