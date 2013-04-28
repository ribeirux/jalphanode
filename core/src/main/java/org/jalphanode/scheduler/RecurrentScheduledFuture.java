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

import java.text.MessageFormat;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;

/**
 * Reschedules every time each task n task is executed.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
class RecurrentScheduledFuture implements ScheduledFuture<Object>, Runnable {

    private static final Log LOG = LogFactory.getLog(RecurrentScheduledFuture.class);

    private final Object schedulerLock = new Object();

    private final Runnable command;

    private final ScheduleIterator scheduleIterator;

    private final ScheduledExecutorService executor;

    private volatile ScheduledFuture<?> currentFuture;

    private volatile Date scheduledExecutionTime;

    /**
     * Creates a new instance with specified fields.
     *
     * @param  command           task to run
     * @param  scheduleIterator  schedule iterator
     * @param  executor          executor
     */
    public RecurrentScheduledFuture(final Runnable command, final ScheduleIterator scheduleIterator,
            final ScheduledExecutorService executor) {
        this.command = Preconditions.checkNotNull(command, "command");
        this.scheduleIterator = Preconditions.checkNotNull(scheduleIterator, "scheduleIterator");
        this.executor = Preconditions.checkNotNull(executor, "executor");
    }

    /**
     * Starts the scheduler.
     *
     * @return  the scheduled future
     */
    public ScheduledFuture<Object> schedule() {
        return this.scheduleInternal(new Date());
    }

    private ScheduledFuture<Object> scheduleInternal(final Date completionDate) {

        ScheduledFuture<Object> result = null;

        Date date = completionDate;

        if ((this.scheduledExecutionTime != null) && date.before(this.scheduledExecutionTime)) {
            date = this.scheduledExecutionTime;
        }

        this.scheduledExecutionTime = this.scheduleIterator.next(date);

        if (this.scheduledExecutionTime != null) {
            final long initialDelay = this.scheduledExecutionTime.getTime() - System.currentTimeMillis();

            synchronized (this.schedulerLock) {
                if ((this.currentFuture == null) || !this.currentFuture.isCancelled()) {
                    this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
                    result = this;
                }
            }
        } else {
            RecurrentScheduledFuture.LOG.warn("Scheduler stopped because there is no next date for execution.");
        }

        return result;
    }

    @Override
    public void run() {

        Date completionDate;
        try {
            this.command.run();
            completionDate = new Date();
        } catch (final Exception e) {
            completionDate = new Date();
            RecurrentScheduledFuture.LOG.error(MessageFormat.format("Task execution failed: {0}", e.getMessage()), e);
        }

        this.scheduleInternal(completionDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDelay(final TimeUnit unit) {
        return this.currentFuture.getDelay(unit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(final Delayed other) {
        int result;
        if (this == other) {
            result = 0;
        } else {
            final long delay = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MICROSECONDS);
            result = (delay < 0 ? -1 : (delay > 0 ? 1 : 0));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        synchronized (this.schedulerLock) {
            return this.currentFuture.cancel(mayInterruptIfRunning);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCancelled() {
        return this.currentFuture.isCancelled();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return this.currentFuture.isDone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get() throws InterruptedException, ExecutionException {
        return this.currentFuture.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException,
        TimeoutException {
        return this.currentFuture.get(timeout, unit);
    }

}
