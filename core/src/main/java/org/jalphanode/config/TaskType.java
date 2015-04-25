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

import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.task.NoTask;
import org.jalphanode.task.Task;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Task configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskType", propOrder = "scheduleIterator")
public class TaskType extends AbstractPropertiesContainer implements TaskConfig {

    private static final String DEFAULT_TASK_NAME = "NoTask";

    /**
     * Default task.
     */
    public static final Task DEFAULT_TASK = new NoTask();

    @XmlAttribute(required = true)
    private String taskName;

    @XmlAttribute(name = "class", required = true)
    @XmlJavaTypeAdapter(TaskClassAdapter.class)
    private Task task;

    @XmlElement(name = "trigger", required = true)
    @XmlJavaTypeAdapter(CronTriggerAdapter.class)
    private ScheduleIterator scheduleIterator;

    /**
     * Creates a new TaskType.
     */
    public TaskType() {
        super();
        this.taskName = TaskType.DEFAULT_TASK_NAME;
        this.task = TaskType.DEFAULT_TASK;
        this.scheduleIterator = CronTriggerAdapter.buildDefaultScheduleIterator();
    }

    /**
     * Gets the taskName property.
     *
     * @return  the taskName property
     */
    @Override
    public String getTaskName() {
        return this.taskName;
    }

    /**
     * Sets the taskName property.
     *
     * @param  taskName  the taskName to set
     */
    public void setTaskName(final String taskName) {
        this.taskName = taskName;
    }

    /**
     * Gets the task property.
     *
     * @return  the task property
     */
    @Override
    public Task getTask() {
        return this.task;
    }

    /**
     * Sets the task property.
     *
     * @param  task  the task to set
     */
    public void setTask(final Task task) {
        this.task = task;
    }

    /**
     * Gets the scheduleIterator property.
     *
     * @return  the scheduleIterator property
     */
    @Override
    public ScheduleIterator getScheduleIterator() {
        return this.scheduleIterator;
    }

    /**
     * Sets the scheduleIterator property.
     *
     * @param  scheduleIterator  the scheduleIterator to set
     */
    public void setScheduleIterator(final ScheduleIterator scheduleIterator) {
        this.scheduleIterator = scheduleIterator;
    }

    static class Adapter extends XmlAdapter<TaskType, TaskConfig> {

        @Override
        public TaskConfig unmarshal(final TaskType v) {
            return v;
        }

        @Override
        public TaskType marshal(final TaskConfig v) {
            return (TaskType) v;
        }
    }

}
