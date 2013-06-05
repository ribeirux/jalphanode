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
package org.jalphanode.jmx;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.text.MessageFormat;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jalphanode.jmx.annotation.MBean;
import org.jalphanode.jmx.annotation.ManagedAttribute;
import org.jalphanode.jmx.annotation.ManagedOperation;
import org.jalphanode.jmx.annotation.ManagedParameter;

import org.jalphanode.util.ReflectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Extracts all MBean metadata from a class.
 *
 * @author  pribeiro
 */
public class MBeanAnnotationScanner {

    public MBeanMetadata scan(final Class<?> klass) {
        Preconditions.checkNotNull(klass, "klass");

        MBeanMetadata metadata = null;
        final MBean mBean = ReflectionUtils.getAnnotation(klass, MBean.class);

        if (mBean != null) {

            if (!Modifier.isPublic(klass.getModifiers())) {
                throw new MalformedMBeanException("MBean modifiers should be public!");
            }

            final MBeanMetadata.Builder builder = new MBeanMetadata.Builder(klass);
            if (!mBean.objectName().isEmpty()) {
                builder.withObjectName(mBean.objectName());
            }

            if (!mBean.description().isEmpty()) {
                builder.withDescription(mBean.description());
            }

            try {
                final BeanInfo beanInfo = Introspector.getBeanInfo(klass, Object.class);

                buildAttributeMetadata(klass, beanInfo, builder);
                buildOperationMetadata(beanInfo, builder);

                metadata = builder.build();
            } catch (java.beans.IntrospectionException e) {
                throw new MalformedMBeanException(e);
            }
        }

        return metadata;
    }

    protected void buildAttributeMetadata(final Class<?> klass, final BeanInfo beanInfo,
            final MBeanMetadata.Builder builder) {

        final List<Field> fields = ReflectionUtils.getAnnotatedAttributes(klass, ManagedAttribute.class);
        final Map<String, Field> indexFields = Maps.newHashMapWithExpectedSize(fields.size());
        for (Field field : fields) {
            indexFields.put(field.getName(), field);
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        final Set<String> attributes = Sets.newHashSetWithExpectedSize(propertyDescriptors.length);
        for (PropertyDescriptor property : propertyDescriptors) {

            final Field annotatedField = indexFields.remove(property.getName());
            final Method readMethod = property.getReadMethod();
            final Method writeMethod = property.getWriteMethod();

            ManagedAttribute annotation = getManagedAttribute(property.getName(), annotatedField, readMethod,
                    writeMethod);

            if (annotation != null) {
                final String name = annotation.name().isEmpty() ? property.getName() : annotation.name();

                if (!attributes.add(name)) {
                    throw new MalformedMBeanException(MessageFormat.format("Attribute with name {0} already registered",
                            name));
                }

                ManagedAttributeMetadata.Builder attributeBuilder = new ManagedAttributeMetadata.Builder(name);

                if (!annotation.description().isEmpty()) {
                    attributeBuilder.withDescription(annotation.description());
                }

                if (readMethod != null) {
                    attributeBuilder.withReadMethod(readMethod);
                }

                if (writeMethod != null) {
                    attributeBuilder.withWriteMethod(writeMethod);
                }

                builder.putAttribute(attributeBuilder.build());
            }
        }

        if (!indexFields.isEmpty()) {
            throw new MalformedMBeanException(MessageFormat.format("Found attribute(s) without getters/setters: {0}",
                    indexFields.keySet()));
        }
    }

    private ManagedAttribute getManagedAttribute(final String name, final Field annotatedField, final Method readMethod,
            final Method writeMethod) {

        int annotationCounter = 0;
        ManagedAttribute annotation = null;

        if (annotatedField != null) {
            annotation = annotatedField.getAnnotation(ManagedAttribute.class);
            annotationCounter++;
        }

        if (readMethod != null) {
            final ManagedAttribute getterAttribute = readMethod.getAnnotation(ManagedAttribute.class);
            if (getterAttribute != null) {
                annotation = getterAttribute;
                annotationCounter++;
            }
        }

        if (writeMethod != null) {
            final ManagedAttribute setterAttribute = writeMethod.getAnnotation(ManagedAttribute.class);
            if (setterAttribute != null) {
                annotation = setterAttribute;
                annotationCounter++;
            }
        }

        if (annotationCounter > 1) {
            throw new MalformedMBeanException(MessageFormat.format("Attribute {0} contains multiple annotations",
                    name));
        }

        return annotation;
    }

    protected void buildOperationMetadata(final BeanInfo beanInfo, final MBeanMetadata.Builder builder) {

        final Set<Method> allAccessors = allAccessors(beanInfo);
        final MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        final Set<String> operationMethods = Sets.newHashSetWithExpectedSize(methodDescriptors.length);

        for (MethodDescriptor descriptor : methodDescriptors) {
            Method method = descriptor.getMethod();
            ManagedOperation operationAnnotation = method.getAnnotation(ManagedOperation.class);

            if (operationAnnotation != null) {
                if (allAccessors.contains(method)) {
                    throw new MalformedMBeanException(MessageFormat.format("Accessor method {0} annotated as {1}",
                            method.getName(), ManagedOperation.class.getName()));
                }

                int mod = method.getModifiers();
                if (!Modifier.isPublic(mod) || Modifier.isStatic(mod)) {
                    throw new MalformedMBeanException(MessageFormat.format(
                            "MBean operation {0} should be public and non static!", method.getName()));
                }

                final String name = operationAnnotation.name().isEmpty() ? method.getName()
                                                                         : operationAnnotation.name();
                if (!operationMethods.add(name)) {
                    throw new MalformedMBeanException(MessageFormat.format("Operation with name {0} already registered",
                            name));
                }

                ManagedOperationMetadata.Builder operationBuilder = new ManagedOperationMetadata.Builder(method);

                if (!operationAnnotation.name().isEmpty()) {
                    operationBuilder.withName(operationAnnotation.name());
                }

                if (!operationAnnotation.description().isEmpty()) {
                    operationBuilder.withDescription(operationAnnotation.description());
                }

                operationBuilder.withImpact(operationAnnotation.impact());
                buildParameterMetadata(method, operationBuilder);

                builder.putOperation(operationBuilder.build());
            }
        }
    }

    private Set<Method> allAccessors(final BeanInfo beanInfo) {
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        final Set<Method> allAccessors = Sets.newHashSetWithExpectedSize(propertyDescriptors.length);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method getter = propertyDescriptor.getReadMethod();
            if (getter != null) {
                allAccessors.add(getter);
            }

            Method setter = propertyDescriptor.getWriteMethod();
            if (setter != null) {
                allAccessors.add(setter);
            }
        }

        return allAccessors;
    }

    protected void buildParameterMetadata(final Method method, final ManagedOperationMetadata.Builder builder) {
        final Class<?>[] parameters = method.getParameterTypes();
        for (int parameterIndex = 0; parameterIndex < parameters.length; parameterIndex++) {
            final String pType = method.getParameterTypes()[parameterIndex].getName();

            // locate parameter annotation
            ManagedParameter parameter = ReflectionUtils.getParameterAnnotation(method, parameterIndex,
                    ManagedParameter.class);

            if (parameter != null) {
                ManagedParameterMetadata.Builder parameterBuilder = new ManagedParameterMetadata.Builder(pType);

                if (!parameter.name().isEmpty()) {
                    parameterBuilder.withName(parameter.name());
                }

                if (!parameter.description().isEmpty()) {
                    parameterBuilder.wirhDescription(parameter.description());
                }

                builder.addParameterMetadata(parameterBuilder.build());
            }
        }

    }
}
