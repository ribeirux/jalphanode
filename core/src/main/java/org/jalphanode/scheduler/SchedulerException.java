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

import org.jalphanode.JAlphaNodeException;

/**
 * Scheduler exception. Thrown if something wrong happens in the scheduler. All specific scheduler exception must extend
 * this exception.
 *
 * @author   ribeirux
 * @version  $Revision: 93 $
 */
public class SchedulerException extends JAlphaNodeException {

    private static final long serialVersionUID = -7349283339726975180L;

    /**
     * Constructs a new exception with <code>null</code> as its detail message.
     */
    public SchedulerException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param  message  the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                  method.
     */
    public SchedulerException(final String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param  message  the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
     * @param  cause    the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>
     *                  null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public SchedulerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param  cause  the cause (which is saved for later retrieval by the {@link #getCause()} method). (A <tt>null</tt>
     *                value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public SchedulerException(final Throwable cause) {
        super(cause);
    }

}
