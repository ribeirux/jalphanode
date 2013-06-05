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
 * $Id: JGroupsAddress.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
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
