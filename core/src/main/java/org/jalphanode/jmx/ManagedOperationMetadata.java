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

import java.util.List;

import javax.management.MBeanOperationInfo;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * MBean operation metadata.
 * 
 * @author   ribeirux
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
            this.description = Preconditions.checkNotNull(name, "name");

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
            ManagedOperationMetadata other = (ManagedOperationMetadata) obj;

            return Objects.equal(getMethod(), other.getMethod()) && Objects.equal(getName(), other.getName())
                    && Objects.equal(getDescription(), other.getDescription())
                    && Objects.equal(getImpact(), other.getImpact())
                    && Objects.equal(getParameters(), other.getParameters());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("ManagedOperationMetadata [method=");
        builder2.append(method);
        builder2.append(", name=");
        builder2.append(name);
        builder2.append(", impact=");
        builder2.append(impact);
        builder2.append(", description=");
        builder2.append(description);
        builder2.append(", parameters=");
        builder2.append(parameters);
        builder2.append("]");
        return builder2.toString();
    }

}
