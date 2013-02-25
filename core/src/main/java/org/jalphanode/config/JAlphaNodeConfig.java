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
 * $Id: JAlphaNodeConfig.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

/**
 * Configuration root.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public interface JAlphaNodeConfig {

    /**
     * Gets tasks configuration.
     * 
     * @return tasks configuration
     */
    TasksConfig getTasks();

    /**
     * Gets task scheduler configuration.
     * 
     * @return task scheduler configuration
     */
    TaskSchedulerConfig getTaskScheduler();

    /**
     * Gets async executor configuration.
     * 
     * @return async executor configuration
     */
    AsyncExecutorConfig getAsyncExecutor();

    /**
     * Gets membership configuration.
     * 
     * @return membership configuration
     */
    MembershipConfig getMembership();
}
