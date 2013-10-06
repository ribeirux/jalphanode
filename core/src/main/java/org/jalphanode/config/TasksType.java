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

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.Lists;

/**
 * Scheduler configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tasksType")
public class TasksType implements TasksConfig {

    @XmlElement(required = true)
    private final List<TaskConfig> task;

    /**
     * Initializes internal fields.
     */
    public TasksType() {
        this.task = Lists.newArrayList();
    }

    /**
     * Gets the {@link List} of properties.
     *
     * @return  the {@link List} of properties
     */
    @Override
    public List<TaskConfig> getTask() {
        return Collections.unmodifiableList(this.task);
    }

    /**
     * Adds a new task.
     *
     * @param  taskConfig  task to add
     */
    public void addTask(final TaskConfig taskConfig) {
        this.task.add(taskConfig);
    }

    /**
     * Removes the specified task.
     *
     * @param  taskConfig  task to remove
     */
    public void removeTask(final TaskConfig taskConfig) {
        this.task.remove(taskConfig);
    }

    /**
     * Removes the task with the specified index.
     *
     * @param  index  index of the task to remove.
     */
    public void removeTask(final int index) {
        this.task.remove(index);
    }
}
