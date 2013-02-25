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
 * $Id: Status.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

/**
 * Contains the different states of jalphanode.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public enum Status {

    /**
     * Object has been instantiated, but start() has not been called.
     */
    INSTANTIATED,
    /**
     * The <code>start()</code> method has been completed and the component is running.
     */
    RUNNING,
    /**
     * The <code>shutdown()</code> method has completed and the component has terminated.
     */
    TERMINATED;

    /**
     * Checks if task execution process is running.
     * 
     * @return true if the task execution process is reunning, otherwise false
     */
    public boolean isRunning() {
        return this.equals(RUNNING);
    }

    /**
     * Checks if task execution process was terminated.
     * 
     * @return true if the task execution process was terminated, otherwise false
     */
    public boolean isTerminated() {
        return this.equals(TERMINATED);
    }

    /**
     * Checks if the start is allowed.
     * 
     * @return true if the start is allowed, otherwise false
     */
    public boolean isStartAllowed() {
        return this.equals(INSTANTIATED);
    }

    /**
     * Checks if stop is allowed.
     * 
     * @return true if the start is allowed, otherwise false
     */
    public boolean isShutdownAllowed() {
        return (this.equals(RUNNING));
    }
}
