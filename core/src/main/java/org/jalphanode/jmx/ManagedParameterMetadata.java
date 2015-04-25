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

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * MBean parameter metadata.
 *
 * @author  ribeirux
 */
public final class ManagedParameterMetadata {

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
            final ManagedParameterMetadata other = (ManagedParameterMetadata) obj;

            return Objects.equal(getType(), other.getType()) && Objects.equal(getName(), other.getName())
                    && Objects.equal(getDescription(), other.getDescription());
        }

        return false;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ManagedParameterMetadata [type=");
        builder.append(type);
        builder.append(", name=");
        builder.append(name);
        builder.append(", description=");
        builder.append(description);
        builder.append(']');
        return builder.toString();
    }

}
