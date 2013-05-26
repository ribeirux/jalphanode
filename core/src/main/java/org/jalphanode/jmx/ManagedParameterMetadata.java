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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * MBean parameter metadata.
 * 
 * @author   ribeirux
 */
public class ManagedParameterMetadata {

    // required fields
    private final String type;

    // optional fields
    private final String name;
    private final String description;

    public static class Builder {

        // required fields
        private final String type;

        // optional fields
        private String name = "param";
        private String description;

        public Builder(final String type) {
            this.type = Preconditions.checkNotNull(type, "type");
        }

        public Builder withName(final String name) {
            this.name = Preconditions.checkNotNull(name, "name");

            return this;
        }

        public Builder wirhDescription(final String description) {
            this.description = Preconditions.checkNotNull(description, "description");

            return this;
        }

        public ManagedParameterMetadata build() {
            return new ManagedParameterMetadata(this);
        }
    }

    private ManagedParameterMetadata(final Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
        this.description = builder.description;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, name, description);
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof ManagedParameterMetadata) {
            ManagedParameterMetadata other = (ManagedParameterMetadata) obj;

            return Objects.equal(getType(), other.getType()) && Objects.equal(getName(), other.getName())
                    && Objects.equal(getDescription(), other.getDescription());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder builder2 = new StringBuilder();
        builder2.append("ManagedParameterMetadata [type=");
        builder2.append(type);
        builder2.append(", name=");
        builder2.append(name);
        builder2.append(", description=");
        builder2.append(description);
        builder2.append("]");
        return builder2.toString();
    }

}
