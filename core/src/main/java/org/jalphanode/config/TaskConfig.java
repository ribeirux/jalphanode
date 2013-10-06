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
package org.jalphanode.config;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.task.Task;

/**
 * Task configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlJavaTypeAdapter(TaskType.Adapter.class)
public interface TaskConfig extends PropertiesContainerConfig {

    /**
     * Gets the task name.
     *
     * @return  the task name
     */
    String getTaskName();

    /**
     * Gets the task to run.
     *
     * @return  the task to run
     */
    Task getTask();

    /**
     * Gets the schedule iterator.
     *
     * @return  the schedule iterator
     */
    ScheduleIterator getScheduleIterator();
}
