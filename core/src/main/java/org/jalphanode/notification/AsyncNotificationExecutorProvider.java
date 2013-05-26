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
package org.jalphanode.notification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jalphanode.config.AsyncExecutorConfig;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TypedPropertiesConfig;

import org.jalphanode.util.DaemonThreadFactory;

import com.google.common.base.Preconditions;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Default asynchronous executor.
 *
 * @author   ribeirux
 * @version  $Revision: 149 $
 */
public class AsyncNotificationExecutorProvider implements Provider<ExecutorService> {

    private static final String KEEP_ALIVE_PROPERTY = "keepAlive";
    private static final String DEFAULT_KEEP_ALIVE = "60000";

    private static final String THREAD_PRIORITY_PROPERTY = "threadPriority";
    private static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;

    private static final String THREAD_PREFIX_PROPERTY = "threadPrefix";
    private static final String DEFAULT_THREAD_PREFIX = "async-pool";

    private final JAlphaNodeConfig config;

    /**
     * Creates a new asynchronous notification executor provider.
     *
     * @param  config  the configuration
     */
    @Inject
    public AsyncNotificationExecutorProvider(final JAlphaNodeConfig config) {
        this.config = Preconditions.checkNotNull(config, "config");
    }

    @Override
    public ExecutorService get() {

        // core pool size
        // max pool size
        // keep_alive_time millis
        final AsyncExecutorConfig asyncConfig = this.config.getAsyncExecutor();
        final Integer corePoolSize = asyncConfig.getCorePoolSize();
        final Integer maxPoolSize = asyncConfig.getMaxPoolSize();

        final TypedPropertiesConfig props = asyncConfig.getProperties();

        final Integer keepAlive = Integer.valueOf(props.getProperty(
                    AsyncNotificationExecutorProvider.KEEP_ALIVE_PROPERTY,
                    AsyncNotificationExecutorProvider.DEFAULT_KEEP_ALIVE));

        final String threadPrefix = props.getProperty(AsyncNotificationExecutorProvider.THREAD_PREFIX_PROPERTY,
                AsyncNotificationExecutorProvider.DEFAULT_THREAD_PREFIX);

        final int priority = props.getIntProperty(AsyncNotificationExecutorProvider.THREAD_PRIORITY_PROPERTY,
                AsyncNotificationExecutorProvider.DEFAULT_THREAD_PRIORITY);

        final ThreadFactory factory = DaemonThreadFactory.newInstance(threadPrefix, priority);

        return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAlive, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), factory, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
