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
package org.jalphanode.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.task.NoTask;
import org.jalphanode.task.Task;

/**
 * Task configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskType", propOrder = {"scheduleIterator"})
public class TaskType extends AbstractPropertiesContainer implements TaskConfig {

    private static final String DEFAULT_TASK_NAME = "NoTask";

    /**
     * Default task.
     */
    public static final Task DEFAULT_TASK = new NoTask();

    @XmlAttribute
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
        public TaskConfig unmarshal(final TaskType v) throws Exception {
            return v;
        }

        @Override
        public TaskType marshal(final TaskConfig v) throws Exception {
            return (TaskType) v;
        }
    }

}
