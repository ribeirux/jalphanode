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
package org.jalphanode.cluster.jgroups;

import org.jalphanode.cluster.NodeAddress;

import org.jgroups.Address;

import com.google.common.base.Objects;

/**
 * An encapsulation of a JGroups Address.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class JGroupsAddress implements NodeAddress {

    private final Address address;

    /**
     * Creates a new instance off JGroups address.
     *
     * @param  address  JGroups address
     */
    public JGroupsAddress(final Address address) {
        this.address = address;
    }

    /**
     * Gets the address property.
     *
     * @return  the address property
     */
    public Address getAddress() {
        return this.address;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(address);
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof JGroupsAddress) {
            final JGroupsAddress other = (JGroupsAddress) obj;

            return Objects.equal(getAddress(), other.getAddress());
        }

        return false;
    }

    @Override
    public String toString() {
        return this.address.toString();
    }

}
