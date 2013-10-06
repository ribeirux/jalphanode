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

import java.lang.reflect.Method;

import java.util.List;

import javax.management.MBeanOperationInfo;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * MBean operation metadata.
 *
 * @author  ribeirux
 */
public class ManagedOperationMetadata {

    // required fields
    private final Method method;

    // optional fields
    private final String name;
    private final int impact;
    private final String description;
    private final List<ManagedParameterMetadata> parameters;

    public static class Builder {

        // required fields
        private final Method method;

        // optional fields
        private int impact;
        private String name;
        private String description;
        private final ImmutableList.Builder<ManagedParameterMetadata> parameters;

        public Builder(final Method method) {
            this.method = Preconditions.checkNotNull(method, "method");
            this.name = method.getName();
            this.impact = MBeanOperationInfo.UNKNOWN;
            this.parameters = new ImmutableList.Builder<ManagedParameterMetadata>();
        }

        public Builder withImpact(final int impact) {
            this.impact = impact;

            return this;
        }

        public Builder withName(final String name) {
            this.name = Preconditions.checkNotNull(name, "name");

            return this;

        }

        public Builder withDescription(final String description) {
            this.description = Preconditions.checkNotNull(description, "description");

            return this;
        }

        public Builder addParameterMetadata(final ManagedParameterMetadata parameterMetadata) {
            parameters.add(Preconditions.checkNotNull(parameterMetadata, "attributeMetadata"));

            return this;
        }

        public ManagedOperationMetadata build() {
            return new ManagedOperationMetadata(this);
        }
    }

    private ManagedOperationMetadata(final Builder builder) {
        this.method = builder.method;
        this.impact = builder.impact;
        this.name = builder.name;
        this.description = builder.description;
        this.parameters = builder.parameters.build();
    }

    public Method getMethod() {
        return method;
    }

    public int getImpact() {
        return impact;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ManagedParameterMetadata> getParameters() {
        return parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(method, name, impact, description, parameters);
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof ManagedOperationMetadata) {
            final ManagedOperationMetadata other = (ManagedOperationMetadata) obj;

            return Objects.equal(getMethod(), other.getMethod()) && Objects.equal(getName(), other.getName())
                    && Objects.equal(getDescription(), other.getDescription())
                    && Objects.equal(getImpact(), other.getImpact())
                    && Objects.equal(getParameters(), other.getParameters());
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ManagedOperationMetadata [method=");
        builder.append(method);
        builder.append(", name=");
        builder.append(name);
        builder.append(", impact=");
        builder.append(impact);
        builder.append(", description=");
        builder.append(description);
        builder.append(", parameters=");
        builder.append(parameters);
        builder.append(']');
        return builder.toString();
    }

}
