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
 * $Id: TaskConfig.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
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
