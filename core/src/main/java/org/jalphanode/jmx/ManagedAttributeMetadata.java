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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * MBean attribute metadata.
 *
 * @author  ribeirux
 */
public class ManagedAttributeMetadata {

    // required fields
    private final String name;

    // optional fields
    private final String description;
    private final Method readMethod;
    private final Method writeMethod;

    public static class Builder {

        // required fields
        private final String name;

        // optional fields
        private String description;
        private Method readMethod;
        private Method writeMethod;

        public Builder(final String name) {
            this.name = Preconditions.checkNotNull(name, "name");
        }

        public Builder withDescription(final String description) {
            this.description = Preconditions.checkNotNull(description, "description");

            return this;
        }

        public Builder withReadMethod(final Method readMethod) {
            this.readMethod = Preconditions.checkNotNull(readMethod, "readMethod");

            return this;
        }

        public Builder withWriteMethod(final Method writeMethod) {
            this.writeMethod = Preconditions.checkNotNull(writeMethod, "writeMethod");

            return this;
        }

        public ManagedAttributeMetadata build() {
            return new ManagedAttributeMetadata(this);
        }
    }

    private ManagedAttributeMetadata(final Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.readMethod = builder.readMethod;
        this.writeMethod = builder.writeMethod;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(description, name, readMethod, writeMethod);
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof ManagedAttributeMetadata) {
            final ManagedAttributeMetadata other = (ManagedAttributeMetadata) obj;

            return Objects.equal(getDescription(), other.getDescription()) && Objects.equal(getName(), other.getName())
                    && Objects.equal(getReadMethod(), other.getReadMethod())
                    && Objects.equal(getWriteMethod(), other.getWriteMethod());
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ManagedAttributeMetadata [name=");
        builder.append(name);
        builder.append(", description=");
        builder.append(description);
        builder.append(", readMethod=");
        builder.append(readMethod);
        builder.append(", writeMethod=");
        builder.append(writeMethod);
        builder.append(']');
        return builder.toString();
    }

}
