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

import java.lang.reflect.Method;

import java.text.MessageFormat;

import java.util.List;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;

/**
 * Creates an dynamic MBean which exposes resources according to the MBean metadata.
 *
 * @author  ribeirux
 */
public class ResourceDynamicMBean implements DynamicMBean {

    private static final Log LOG = LogFactory.getLog(ResourceDynamicMBean.class);

    private final Object instance;
    private final MBeanMetadata metadata;
    private final MBeanInfo mbeanInfo;

    public ResourceDynamicMBean(final Object instance, final MBeanMetadata metadata) {
        this.instance = Preconditions.checkNotNull(instance, "instance");
        this.metadata = Preconditions.checkNotNull(metadata, "metadata");
        this.mbeanInfo = createMbeanInfo(metadata);
    }

    private MBeanInfo createMbeanInfo(final MBeanMetadata metadata) {

        // create attribute info
        final Map<String, ManagedAttributeMetadata> attributes = metadata.getAttributes();
        final MBeanAttributeInfo[] attributeInfo = new MBeanAttributeInfo[attributes.size()];

        int i = 0;
        for (final String propertyName : attributes.keySet()) {
            final ManagedAttributeMetadata property = attributes.get(propertyName);

            try {
                MBeanAttributeInfo info = new MBeanAttributeInfo(propertyName, property.getDescription(),
                        property.getReadMethod(), property.getWriteMethod());

                attributeInfo[i++] = info;
            } catch (IntrospectionException e) {
                throw new MalformedMBeanException(MessageFormat.format(
                        "Consistency problem in the definition of attribute: {0}", propertyName), e);
            }
        }

        // create operation info
        final Map<String, ManagedOperationMetadata> operations = metadata.getOperations();
        final MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[operations.size()];

        i = 0;
        for (final String methodName : operations.keySet()) {
            final ManagedOperationMetadata operationMetadata = operations.get(methodName);

            final List<ManagedParameterMetadata> parametersMetadata = operationMetadata.getParameters();
            final MBeanParameterInfo[] signature = new MBeanParameterInfo[parametersMetadata.size()];

            int parameterIndex = 0;
            for (final ManagedParameterMetadata parameterMetadata : parametersMetadata) {
                signature[parameterIndex++] = new MBeanParameterInfo(parameterMetadata.getName(),
                        parameterMetadata.getType(), parameterMetadata.getDescription());
            }

            operationInfo[i++] = new MBeanOperationInfo(operationMetadata.getName(), operationMetadata.getDescription(),
                    signature, operationMetadata.getMethod().getReturnType().getName(), operationMetadata.getImpact(),
                    null);
        }

        return new MBeanInfo(metadata.getInstanceClass().getName(), metadata.getDescription(), attributeInfo, null,
                operationInfo, null);
    }

    @Override
    public Object getAttribute(final String attribute) throws AttributeNotFoundException, MBeanException {
        Preconditions.checkNotNull(attribute, "attribute");

        final ManagedAttributeMetadata attributeMetadata = metadata.getAttribute(attribute);
        if (attributeMetadata == null) {
            throw new AttributeNotFoundException(attribute);
        }

        final Method getter = attributeMetadata.getReadMethod();
        if (getter == null) {
            throw new AttributeNotFoundException(MessageFormat.format("Getter for attribute {0} not found on MBean {1}",
                    attribute, metadata.getInstanceClass().getName()));
        }

        try {
            if (!getter.isAccessible()) {
                getter.setAccessible(true);
            }

            return getter.invoke(instance);
        } catch (Exception e) {
            throw new MBeanException(e);
        }

    }

    @Override
    public void setAttribute(final Attribute attribute) throws AttributeNotFoundException, MBeanException {
        Preconditions.checkNotNull(attribute, "attribute");

        final String name = attribute.getName();
        final ManagedAttributeMetadata attributeMetadata = metadata.getAttribute(name);
        if (attributeMetadata == null) {
            throw new AttributeNotFoundException(name);
        }

        final Method setter = attributeMetadata.getWriteMethod();
        if (setter == null) {
            throw new AttributeNotFoundException(MessageFormat.format(
                    "Setter for attribuete {0} not found on MBean {1}", name, metadata.getInstanceClass().getName()));
        }

        final Object value = attribute.getValue();
        try {
            if (!setter.isAccessible()) {
                setter.setAccessible(true);
            }

            setter.invoke(instance, value);
        } catch (Exception e) {
            throw new MBeanException(e);
        }
    }

    @Override
    public AttributeList getAttributes(final String[] attributes) {
        Preconditions.checkNotNull(attributes, "attributes");

        final AttributeList attributeList = new AttributeList(attributes.length);
        for (final String attributeName : attributes) {
            try {
                attributeList.add(new Attribute(attributeName, getAttribute(attributeName)));
            } catch (Exception e) {
                LOG.error(MessageFormat.format("Could not get attribute {0} on MBean {1}", attributeName,
                        metadata.getInstanceClass().getName()), e);
            }
        }

        return attributeList;
    }

    @Override
    public AttributeList setAttributes(final AttributeList attributes) {
        Preconditions.checkNotNull(attributes, "attributes");

        final AttributeList attributeList = new AttributeList(attributes.size());
        for (final Object object : attributes) {
            final Attribute attribute = (Attribute) object;

            try {
                setAttribute(attribute);
                attributeList.add(attribute);
            } catch (Exception e) {
                LOG.error(MessageFormat.format("Could not set attribute {0} to {1} on MBean {2}", attribute.getName(),
                        attribute.getValue(), metadata.getInstanceClass().getName()), e);
            }
        }

        return attributeList;
    }

    @Override
    public Object invoke(final String actionName, final Object[] params, final String[] signature)
        throws MBeanException {

        final ManagedOperationMetadata operationMetadata = metadata.getOperation(actionName);

        if (operationMetadata == null) {
            throw new IllegalArgumentException("No such operation: " + actionName);
        }

        final Method method = operationMetadata.getMethod();

        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            return method.invoke(instance, params);
        } catch (Exception e) {
            throw new MBeanException(e);
        }
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return mbeanInfo;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AnnotationDrivenDynamicMBean [instance=");
        builder.append(instance);
        builder.append(", metadata=");
        builder.append(metadata);
        builder.append(", mbeanInfo=");
        builder.append(mbeanInfo);
        builder.append(']');
        return builder.toString();
    }
}
