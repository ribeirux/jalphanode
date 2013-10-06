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
package org.jalphanode;

import org.jalphanode.config.JAlphaNodeConfig;

import org.jalphanode.notification.Listenable;

/**
 * Executes a task.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface TaskManager extends Listenable {

    enum Status {
        INSTANTIATED,
        STARTING,
        RUNNING,
        STOPPING,
        STOPPED,
        FAILED;
    }

    /**
     * Starts the task manager.
     */
    void start();

    /**
     * Shutdown the task manager.
     */
    void shutdown();

    /**
     * Gets the current configuration.
     *
     * @return  the current configuration
     */
    JAlphaNodeConfig getConfig();

    /**
     * Gets the current state;
     *
     * @return  the current state
     */
    Status getStatus();

}
