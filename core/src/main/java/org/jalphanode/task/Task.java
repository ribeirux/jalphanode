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
package org.jalphanode.task;

import org.jalphanode.config.TaskConfig;

/**
 * Task interface. The task that needs high availability should implement this interface.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public interface Task {

    /**
     * Method executed on each timeout.
     *
     * @param  config  the configuration
     */
    void onTimeout(TaskConfig config);
}
