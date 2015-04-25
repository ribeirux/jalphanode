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
package org.jalphanode.ui;

import org.jalphanode.util.FileUtils;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.net.URL;
import java.text.MessageFormat;

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

    private final Icon image;

    NodeTypeImage(final Icon image) {
        this.image = image;
    }

    public Icon getImage() {
        return this.image;
    }

}
