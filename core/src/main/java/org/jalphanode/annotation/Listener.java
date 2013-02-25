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
 * $Id: Listener.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level annotation used to annotate an object as being a valid listener.
 * <p/>
 * Note that even if a class is annotated with this annotation, it still needs method-level annotation to actually
 * receive notifications.
 * <p/>
 * <h4>Delivery Semantics</h4>
 * <p/>
 * An event is delivered immediately after the respective operation, but before the underlying call returns. For this
 * reason it is important to keep listener processing logic short-lived. If a long running task needs to be performed,
 * it's recommended to use option sync = false, to execute the task asynchronously.
 * <p/>
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Listener {

    /**
     * Specifies whether callbacks on any class annotated with this annotation happens synchronously (in the caller's
     * thread) or asynchronously (using a separate thread). Defaults to <tt>true</tt>.
     * 
     * @return true if the expectation is that callbacks are called using the caller's thread; false if they are to be
     *         made in a separate thread.
     */
    boolean sync() default true;
}
