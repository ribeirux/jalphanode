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
 * $Id$
 *******************************************************************************/
package org.jalphanode.config;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jalphanode.task.Task;
import org.jalphanode.util.ConfigurationUtils;

import com.google.common.base.Preconditions;

/**
 * Converts a class name into a java Class.
 * 
 * @author ribeirux
 * @version $Revision: 118 $
 */
public class TaskClassAdapter extends XmlAdapter<String, Task> {

    @Override
    public String marshal(final Task task) throws Exception {
        return Preconditions.checkNotNull(task, "task").getClass().getName();
    }

    @Override
    public Task unmarshal(final String taskClass) throws Exception {
        return ConfigurationUtils.getClass(Preconditions.checkNotNull(taskClass, "taskClass"), Task.class)
                .newInstance();
    }
}
