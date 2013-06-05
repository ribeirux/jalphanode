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
 * $Id: ListenerMethodException.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.jmx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Public classes annotated with this annotation will be exposed as MBeans. Take a look at
 * {@link org.jalphanode.jmx.annotation.ManagedAttribute}, {@link org.jalphanode.jmx.annotation.ManagedOperation} and
 * {@link org.jalphanode.jmx.annotation.ManagedParameter} for more details.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface MBean {

    /**
     * MBean object name. Refer to {@link javax.management.ObjectName} for more details.
     *
     * @return  the MBean object name
     */
    String objectName() default "";

    /**
     * Mbean description.
     *
     * @return  the MBean description
     */
    String description() default "";
}
