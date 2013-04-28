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
 * $Id$
 *******************************************************************************/
package org.jalphanode.cluster;

import java.util.List;

/**
 * Decides witch node in the cluster should be the master node.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public interface MasterNodeElectionPolicy {

    /**
     * Elects the master node from the current cluster.
     *
     * @param   nodes  the current nodes of the cluster
     *
     * @return  the address of the master node.
     */
    NodeAddress elect(List<NodeAddress> nodes);

}
