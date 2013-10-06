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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedMap;

/**
 * MBean metadata.
 *
 * @author  ribeirux
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
            final MBeanMetadata other = (MBeanMetadata) obj;

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
        final StringBuilder builder = new StringBuilder();
        builder.append("MBeanMetadata [instanceClass=");
        builder.append(instanceClass);
        builder.append(", objectName=");
        builder.append(objectName);
        builder.append(", description=");
        builder.append(description);
        builder.append(", attributes=");
        builder.append(attributes);
        builder.append(", operations=");
        builder.append(operations);
        builder.append(']');
        return builder.toString();
    }

}
