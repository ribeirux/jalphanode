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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 * Basic reflection utilities to enhance what the JDK provides.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        // utilities class
    }

    /**
     * Inspects the class passed in for the class level annotation specified. If the annotation is not available on the
     * specified class, this method recursively inspects super classes and interfaces until it finds the required
     * annotation.
     *
     * <p/>Returns false if the annotation cannot be found.
     *
     * @param   <T>         annotation type
     * @param   clazz       class to inspect
     * @param   annotation  annotation to search for. Must be a class-level annotation.
     *
     * @return  true if the annotation is present, otherwise false
     */
    public static <T extends Annotation> boolean isAnnotationPresent(final Class<?> clazz, final Class<T> annotation) {
        return getAnnotation(clazz, annotation) != null;
    }

    /**
     * Inspects the class passed in for the class level annotation specified. If the annotation is not available, this
     * method recursively inspects super classes and interfaces until it finds the required annotation.
     *
     * @param   <T>         annotation type
     * @param   clazz       class to inspect
     * @param   annotation  annotation to search for
     *
     * @return  the annotation instance, or null if the annotation cannot be found.
     */
    public static <T extends Annotation> T getAnnotation(final Class<?> clazz, final Class<T> annotation) {
        Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(annotation, "annotation");

        Class<?> tmpClazz = clazz;

        T classAnnotation = null;

        while (classAnnotation == null && tmpClazz != null && !tmpClazz.equals(Object.class)) {

            // first check class
            classAnnotation = tmpClazz.getAnnotation(annotation);

            if (classAnnotation == null) {

                // check interfaces
                if (!tmpClazz.isInterface()) {
                    final Class<?>[] interfaces = tmpClazz.getInterfaces();
                    for (int i = 0; i < interfaces.length && classAnnotation == null; i++) {
                        classAnnotation = ReflectionUtils.getAnnotation(interfaces[i], annotation);
                    }
                }

                // If the annotation was not found, keep searching on super class
                if (classAnnotation == null) {
                    tmpClazz = tmpClazz.getSuperclass();
                }
            }
        }

        return classAnnotation;
    }

    /**
     * Inspects the parameter for the specified annotation.
     *
     * @param   method           method
     * @param   index            parameter index
     * @param   annotationClass  annotation to search for
     *
     * @return  the annotation instance, or null if the annotation cannot be found.
     */
    public static <A extends Annotation> A getParameterAnnotation(final Method method, final int index,
            final Class<A> annotationClass) {
        Preconditions.checkNotNull(method, "method");
        Preconditions.checkNotNull(annotationClass, "annotationClass");

        Annotation[][] annotations = method.getParameterAnnotations();
        Preconditions.checkPositionIndex(index, annotations.length, "index");

        A annotation = null;

        for (Annotation a : method.getParameterAnnotations()[index]) {
            if (annotationClass.isInstance(a)) {
                annotation = annotationClass.cast(a);
                break;
            }
        }

        return annotation;
    }

    /**
     * Inspects the class passed in for methods annotated with the specified class. This method recursively inspects
     * super classes and interfaces.
     *
     * @param   clazz       class to inspect
     * @param   annotation  annotation to search for
     *
     * @return  A {@link List} of methods with the specified annotation
     */
    public static List<Method> getAllMethods(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(annotation, "annotation");

        List<Method> annotated = new LinkedList<Method>();
        getAllMethodsRecursively(clazz, annotated, annotation);

        return annotated;
    }

    private static void getAllMethodsRecursively(final Class<?> clazz, final List<Method> annotated,
            final Class<? extends Annotation> annotation) {

        if (clazz != null && !clazz.equals(Object.class)) {
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(annotation)) {

                    // don't bother if this method has already been overridden by a subclass
                    boolean exists = false;
                    for (Iterator<Method> it = annotated.iterator(); it.hasNext() && !exists;) {
                        Method found = it.next();
                        exists = m.getName().equals(found.getName())
                                && Arrays.equals(m.getParameterTypes(), found.getParameterAnnotations());
                    }

                    if (!exists) {
                        annotated.add(m);
                    }
                }
            }

            getAllMethodsRecursively(clazz.getSuperclass(), annotated, annotation);
            for (Class<?> ifc : clazz.getInterfaces()) {
                getAllMethodsRecursively(ifc, annotated, annotation);
            }
        }
    }

    /**
     * Inspects the class passed in for fields annotated with the specified class. This method recursively inspects
     * super classes and interfaces.
     *
     * @param   clazz       class to inspect
     * @param   annotation  annotation to search for
     *
     * @return  A {@link List} of fields with the specified annotation
     */
    public static List<Field> getAnnotatedFields(final Class<?> clazz,
            final Class<? extends Annotation> annotation) {
        Preconditions.checkNotNull(clazz, "clazz");
        Preconditions.checkNotNull(annotation, "annotation");

        List<Field> annotated = new LinkedList<Field>();

        getAnnotatedFieldsRecursively(clazz, annotated, annotation);

        return annotated;

    }

    private static void getAnnotatedFieldsRecursively(final Class<?> clazz, final List<Field> fields,
            final Class<? extends Annotation> annotationType) {

        if (clazz != null && !clazz.equals(Object.class)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationType)) {
                    fields.add(field);
                }
            }

            getAnnotatedFieldsRecursively(clazz.getSuperclass(), fields, annotationType);
        }
    }

}
