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
 * $Id: TaskManager.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

import java.util.Date;

import org.jalphanode.config.JAlphaNodeConfig;

import org.jalphanode.notification.Listenable;

/**
 * Executes a task.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface TaskManager extends Listenable {

    /**
     * Starts the task manager.
     */
    void start();

    /**
     * Shutdown the task manager.
     */
    void shutdown();

    /**
     * Gets the period of running time.
     *
     * @return  the running time period
     */
    Period getRunningTime();

    /**
     * Gets the start running date.
     *
     * @return  the start running date
     */
    Date getStartDate();

    /**
     * Gets the status of the node.
     *
     * @return  the current status
     */
    Status getStatus();

    /**
     * Gets the current configuration.
     *
     * @return  the current configuration
     */
    JAlphaNodeConfig getConfig();

}
