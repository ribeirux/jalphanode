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
 * $Id: DaemonThreadFactory.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.util;

import java.text.MessageFormat;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;

/**
 * Default thread factory.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class DaemonThreadFactory implements ThreadFactory {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final String threadNamePrefix;
    private final int threadPrio;

    /**
     * Creates a new thread factory.
     * 
     * @param threadNamePrefix thread name prefix
     * @param threadPrio thread priority
     */
    public DaemonThreadFactory(final String threadNamePrefix, final int threadPrio) {
        this.threadNamePrefix = Preconditions.checkNotNull(threadNamePrefix, "threadNamePrefix");
        this.threadPrio = Preconditions.checkNotNull(threadPrio, "threadPrio");
    }

    @Override
    public Thread newThread(final Runnable target) {
        final Thread thread = new Thread(target, MessageFormat.format("{0}-{1}", this.threadNamePrefix,
                DaemonThreadFactory.COUNTER.getAndIncrement()));

        thread.setDaemon(true);
        thread.setPriority(this.threadPrio);

        return thread;
    }

    /**
     * Creates a new thread factory.
     * 
     * @param threadNamePrefix thread name prefix
     * @param threadPrio thread priority
     * @return a new thread factory
     */
    public static ThreadFactory newInstance(final String threadNamePrefix, final int threadPrio) {
        return new DaemonThreadFactory(threadNamePrefix, threadPrio);
    }

}
