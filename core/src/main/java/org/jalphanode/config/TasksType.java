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
 * $Id: TasksType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
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
