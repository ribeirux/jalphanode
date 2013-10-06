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
package org.jalphanode.util;

import java.text.MessageFormat;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Preconditions;

/**
 * Default thread factory.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class DaemonThreadFactory implements ThreadFactory {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    private final String threadNamePrefix;
    private final int threadPrio;

    /**
     * Creates a new thread factory.
     *
     * @param  threadNamePrefix  thread name prefix
     * @param  threadPrio        thread priority
     */
    public DaemonThreadFactory(final String threadNamePrefix, final int threadPrio) {
        this.threadNamePrefix = Preconditions.checkNotNull(threadNamePrefix, "threadNamePrefix");
        this.threadPrio = Preconditions.checkNotNull(threadPrio, "threadPrio");
    }

    @Override
    public Thread newThread(final Runnable target) {
        final Thread thread = new Thread(target,
                MessageFormat.format("{0}-{1}", this.threadNamePrefix, DaemonThreadFactory.COUNTER.getAndIncrement()));

        thread.setDaemon(true);
        thread.setPriority(this.threadPrio);

        return thread;
    }

    /**
     * Creates a new thread factory.
     *
     * @param   threadNamePrefix  thread name prefix
     * @param   threadPrio        thread priority
     *
     * @return  a new thread factory
     */
    public static ThreadFactory newInstance(final String threadNamePrefix, final int threadPrio) {
        return new DaemonThreadFactory(threadNamePrefix, threadPrio);
    }

}
