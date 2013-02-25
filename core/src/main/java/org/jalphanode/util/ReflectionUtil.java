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
 * $Id: ReflectionUtil.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.util;

import java.lang.annotation.Annotation;

import com.google.common.base.Preconditions;

/**
 * Basic reflection utilities to enhance what the JDK provides.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * Inspects the class passed in for the class level annotation specified. If the annotation is not available, this
     * method recursively inspects super classes and interfaces until it finds the required annotation.
     * <p/>
     * Returns null if the annotation cannot be found.
     * 
     * @param <T> annotation type
     * @param clazz class to inspect
     * @param annotation annotation to search for. Must be a class-level annotation.
     * @return the annotation instance, or null
     */
    public static <T extends Annotation> T getAnnotation(final Class<?> clazz, final Class<T> annotation) {
        Class<?> tmpClazz = Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(annotation, "annotation");

        while (true) {
            // first check class
            T a = tmpClazz.getAnnotation(annotation);
            if (a != null) {
                return a;
            }

            // check interfaces
            if (!tmpClazz.isInterface()) {
                final Class<?>[] interfaces = tmpClazz.getInterfaces();
                for (final Class<?> inter : interfaces) {
                    a = ReflectionUtil.getAnnotation(inter, annotation);
                    if (a != null) {
                        return a;
                    }
                }
            }

            // check super classes
            final Class<?> superclass = tmpClazz.getSuperclass();
            if (superclass == null) {
                return null; // no where else to look
            }

            tmpClazz = superclass;
        }
    }
}
