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
package org.jalphanode.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.jalphanode.annotation.Stop;

public abstract class LazyInitializingThreadPoolExecutor implements Executor {

    private volatile boolean running = true;
    private volatile ThreadPoolExecutor pool;

    @Override
    public void execute(final Runnable command) {

        if (running) {
            if (pool == null) {
                synchronized (this) {
                    if (running && pool == null) {
                        pool = createThreadPool();
                    }
                }
            }

            // reject tasks if the executor was shutdown
            if (pool != null) {
                pool.execute(command);
            }
        }
    }

    // last thing to stop
    @Stop(priority = 999)
    public void stop() {
        synchronized (this) {
            running = false;

            if (pool != null) {
                cleanup(pool);
            }
        }
    }

    protected abstract ThreadPoolExecutor createThreadPool();

    protected abstract void cleanup(ThreadPoolExecutor pool);

}
