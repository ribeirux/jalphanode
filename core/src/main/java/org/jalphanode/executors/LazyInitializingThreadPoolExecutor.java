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
