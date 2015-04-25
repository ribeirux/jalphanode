/**
 *    Copyright 2011 Pedro Ribeiro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.jalphanode.jmx;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * Creates an dynamic MBean which exposes resources according to the MBean metadata.
 *
 * @author  ribeirux
 */
public class ResourceDynamicMBean implements DynamicMBean {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceDynamicMBean.class);

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
        for (final Map.Entry<String, ManagedAttributeMetadata> entry : attributes.entrySet()) {
            final ManagedAttributeMetadata property = entry.getValue();
            try {
                MBeanAttributeInfo info = new MBeanAttributeInfo(entry.getKey(), property.getDescription(),
                        property.getReadMethod(), property.getWriteMethod());

                attributeInfo[i++] = info;
            } catch (IntrospectionException e) {
                throw new MalformedMBeanException(MessageFormat.format(
                        "Consistency problem in the definition of attribute: {0}", entry.getKey()), e);
            }
        }

        // create operation info
        final Map<String, ManagedOperationMetadata> operations = metadata.getOperations();
        final MBeanOperationInfo[] operationInfo = new MBeanOperationInfo[operations.size()];

        i = 0;
        for (final ManagedOperationMetadata operationMetadata : operations.values()) {
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
                LOG.error("Could not get attribute {} on MBean {}", attributeName,
                    metadata.getInstanceClass().getName(), e);
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
                LOG.error("Could not set attribute {} to {} on MBean {}", attribute.getName(), attribute.getValue(),
                    metadata.getInstanceClass().getName(), e);
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
