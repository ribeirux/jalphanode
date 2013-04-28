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
 * $Id: NodeTypeImage.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.ui;

import java.net.URL;

import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.jalphanode.util.FileUtils;

/**
 * Node images.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public enum NodeTypeImage {

    /**
     * The master node is local.
     */
    LOCAL_MASTER(NodeTypeImage.buildIcon("image.localMaster")),
    /**
     * The local node is slave.
     */
    LOCAL_SLAVE(NodeTypeImage.buildIcon("image.localSlave")),
    /**
     * The master node is remote.
     */
    REMOTE_MASTER(NodeTypeImage.buildIcon("image.remoteMaster")),
    /**
     * The remote node is a salve.
     */
    REMOTE_SLAVE(NodeTypeImage.buildIcon("image.remoteSlave"));

    private static Icon buildIcon(final String key) {
        final String file = Messages.getString(key);

        final URL url = FileUtils.lookupFileLocation(file);

        if (url == null) {
            throw new IllegalArgumentException(MessageFormat.format("Icon {0} not found.", file));
        }

        return new ImageIcon(url);
    }

    private Icon image;

    private NodeTypeImage(final Icon image) {
        this.image = image;
    }

    public Icon getImage() {
        return this.image;
    }

}
