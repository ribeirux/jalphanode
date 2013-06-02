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
 * $Id: JAlphaNodeType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import java.nio.charset.Charset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Encapsulates the configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name = "jalphanodeConfiguration", propOrder = {"tasks", "taskScheduler", "asyncNotificationExecutor", "membership"}
)
@XmlRootElement(name = "jalphanode")
public class JAlphaNodeType implements JAlphaNodeConfig {

    /**
     * Configuration file charset.
     */
    public static final Charset CONFIG_CHARSET = Charset.forName("utf8");

    /**
     * Supported file extensions.
     */
    public static final String[] FILE_EXTENSIONS = {"xml"};

    @XmlElement
    private TasksType tasks;

    @XmlElement
    private TaskSchedulerType taskScheduler;

    @XmlElement
    private AsyncNotificationExecutorType asyncNotificationExecutor;

    @XmlElement
    private MembershipType membership;

    /**
     * Creates default jalphanode configuration.
     */
    public JAlphaNodeType() {
        this.tasks = new TasksType();
        this.taskScheduler = new TaskSchedulerType();
        this.asyncNotificationExecutor = new AsyncNotificationExecutorType();
        this.membership = new MembershipType();
    }

    /**
     * Gets the tasks property.
     *
     * @return  the tasks property
     */
    @Override
    public TasksType getTasks() {
        return this.tasks;
    }

    /**
     * Sets the tasks property.
     *
     * @param  tasks  the tasks to set
     */
    public void setTasks(final TasksType tasks) {
        this.tasks = tasks;
    }

    /**
     * Gets the taskScheduler property.
     *
     * @return  the taskScheduler property
     */
    @Override
    public TaskSchedulerType getTaskScheduler() {
        return this.taskScheduler;
    }

    /**
     * Sets the taskScheduler property.
     *
     * @param  taskScheduler  the taskScheduler to set
     */
    public void setTaskScheduler(final TaskSchedulerType taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    /**
     * Gets the asyncNotificationExecutor property.
     *
     * @return  the asyncNotificationExecutor property
     */
    @Override
    public AsyncNotificationExecutorType getAsyncNotificationExecutor() {
        return this.asyncNotificationExecutor;
    }

    /**
     * Sets the asyncNotificationExecutor property.
     *
     * @param  asyncNotificationExecutor  the asyncNotificationExecutor to set
     */
    public void setAsyncNotificationExecutor(final AsyncNotificationExecutorType asyncNotificationExecutor) {
        this.asyncNotificationExecutor = asyncNotificationExecutor;
    }

    /**
     * Gets the membership property.
     *
     * @return  the membership property
     */
    @Override
    public MembershipType getMembership() {
        return this.membership;
    }

    /**
     * Sets the membership property.
     *
     * @param  membership  the membership to set
     */
    public void setMembership(final MembershipType membership) {
        this.membership = membership;
    }
}
