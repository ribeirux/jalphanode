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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;

/**
 * MBean metadata.
 * 
 * @author   ribeirux
 */
public class MBeanMetadata {

    // required fields
    private final Class<?> instanceClass;

    // optional fields
    private final String objectName;
    private final String description;
    private final Map<String, ManagedAttributeMetadata> attributes;
    private final Map<String, ManagedOperationMetadata> operations;

    public static class Builder {

        // required fields
        private final Class<?> instanceClass;

        // optional fields
        private String objectName;
        private String description;
        private final Map<String, ManagedAttributeMetadata> attributes;
        private final Map<String, ManagedOperationMetadata> operations;

        public Builder(final Class<?> instanceClass) {
            this.instanceClass = Preconditions.checkNotNull(instanceClass, "instanceClass");
            this.objectName = instanceClass.getSimpleName();
            this.attributes = new HashMap<String, ManagedAttributeMetadata>();
            this.operations = new HashMap<String, ManagedOperationMetadata>();
        }

        public Builder withObjectName(final String objectName) {
            this.objectName = Preconditions.checkNotNull(objectName, "objectName");

            return this;
        }

        public Builder withDescription(final String description) {
            this.description = Preconditions.checkNotNull(description, "description");

            return this;
        }

        public Builder putAttribute(final ManagedAttributeMetadata attribute) {
            Preconditions.checkNotNull(attribute, "attribute");
            this.attributes.put(attribute.getName(), attribute);

            return this;
        }

        public Builder putOperation(final ManagedOperationMetadata operation) {
            Preconditions.checkNotNull(operation, "operation");
            this.operations.put(operation.getName(), operation);

            return this;
        }

        public MBeanMetadata build() {
            return new MBeanMetadata(this);
        }
    }

    private MBeanMetadata(final Builder builder) {
        instanceClass = builder.instanceClass;
        objectName = builder.objectName;
        description = builder.description;
        attributes = ImmutableSortedMap.copyOf(builder.attributes);
        operations = ImmutableSortedMap.copyOf(builder.operations);
    }

    public Class<?> getInstanceClass() {
        return instanceClass;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, ManagedAttributeMetadata> getAttributes() {
        return attributes;
    }

    public Map<String, ManagedOperationMetadata> getOperations() {
        return operations;
    }

    public ManagedAttributeMetadata getAttribute(final String name) {
        return attributes.get(Preconditions.checkNotNull(name, "name"));
    }

    public ManagedOperationMetadata getOperation(final String name) {
        return operations.get(Preconditions.checkNotNull(name, "name"));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(instanceClass, objectName, description, attributes, operations);
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof MBeanMetadata) {
            MBeanMetadata other = (MBeanMetadata) obj;

            return Objects.equal(getInstanceClass(), other.getInstanceClass())
                    && Objects.equal(getObjectName(), other.getObjectName())
                    && Objects.equal(getDescription(), other.getDescription())
                    && Objects.equal(getAttributes(), other.getAttributes())
                    && Objects.equal(getOperations(), other.getOperations());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("MBeanMetadata [instanceClass=");
        builder2.append(instanceClass);
        builder2.append(", objectName=");
        builder2.append(objectName);
        builder2.append(", description=");
        builder2.append(description);
        builder2.append(", attributes=");
        builder2.append(attributes);
        builder2.append(", operations=");
        builder2.append(operations);
        builder2.append("]");
        return builder2.toString();
    }

}
