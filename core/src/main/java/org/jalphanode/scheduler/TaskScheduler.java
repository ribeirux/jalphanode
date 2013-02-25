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
import java.util.concurrent.ScheduledFuture;

/**
 * Task scheduler.
 * 
 * @author ribeirux
 * @version $Revision$
 */
public interface TaskScheduler {

    /**
     * Creates and executes periodic task.
     * 
     * @param command the task to execute
     * @param scheduleIterator iterator which calcultes the next timeout
     * @return a ScheduledFuture representing pending completion of the task, and whose <tt>get()</tt> method will throw
     *         an exception upon cancellation
     */
    ScheduledFuture<?> schedule(Runnable command, ScheduleIterator scheduleIterator);

    /**
     * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be
     * accepted. Invocation has no additional effect if already shut down.
     */
    void shutdown();

    /**
     * Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the
     * tasks that were awaiting execution. These tasks are drained (removed) from the task queue upon return from this
     * method.
     * <p>
     * There are no guarantees beyond best-effort attempts to stop processing actively executing tasks. This
     * implementation cancels tasks via {@link Thread#interrupt}, so any task that fails to respond to interrupts may
     * never terminate.
     * 
     * @return list of tasks that never commenced execution
     */
    List<Runnable> shutdownNow();

}
