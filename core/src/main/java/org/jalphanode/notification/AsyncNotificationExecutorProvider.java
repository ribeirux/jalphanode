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
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.jalphanode.AbstractConfigHolder;
import org.jalphanode.config.JAlphaNodeConfig;
import org.jalphanode.config.TypedPropertiesConfig;
import org.jalphanode.util.DaemonThreadFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Default asynchronous executor.
 * 
 * @author ribeirux
 * @version $Revision: 149 $
 */
public class AsyncNotificationExecutorProvider extends AbstractConfigHolder implements Provider<ExecutorService> {

    private static final String THREAD_PRIORITY_PROPERTY = "threadPriority";

    private static final String THREAD_PREFIX_PROPERTY = "threadPrefix";

    private static final String DEFAULT_THREAD_PREFIX = "async-pool";

    private static final int DEFAULT_THREAD_PRIORITY = Thread.NORM_PRIORITY;

    private ThreadFactory createThreadFactory(final JAlphaNodeConfig config) {
        final TypedPropertiesConfig props = config.getAsyncExecutor().getProperties();

        final String threadPrefix = props.getProperty(AsyncNotificationExecutorProvider.THREAD_PREFIX_PROPERTY,
                AsyncNotificationExecutorProvider.DEFAULT_THREAD_PREFIX);

        final int priority = props.getIntProperty(AsyncNotificationExecutorProvider.THREAD_PRIORITY_PROPERTY,
                AsyncNotificationExecutorProvider.DEFAULT_THREAD_PRIORITY);

        return DaemonThreadFactory.newInstance(threadPrefix, priority);
    }

    /**
     * Creates a new asynchronous notification executor provider.
     * 
     * @param config the configuration
     */
    @Inject
    public AsyncNotificationExecutorProvider(final JAlphaNodeConfig config) {
        super(config);
    }

    @Override
    public ExecutorService get() {

        final JAlphaNodeConfig config = this.getConfig();
        final Integer poolSize = config.getAsyncExecutor().getCorePoolSize();

        return Executors.newScheduledThreadPool(poolSize, this.createThreadFactory(config));
    }
}
