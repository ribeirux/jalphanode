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
 * $Id: ViewChanged.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used on methods that need to be notified when the topology changes (i.e., a member joins or
 * leaves the cluster).
 *
 * <p/>Methods annotated with this annotation should accept a single parameter, a
 * {@link org.jalphanode.notification.ViewChangedEvent} otherwise a
 * {@link org.jalphanode.notification.ListenerMethodException} will be thrown when registering your listener.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 * @see      org.jalphanode.annotation.Listener
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ViewChanged {
    // no fields
}
