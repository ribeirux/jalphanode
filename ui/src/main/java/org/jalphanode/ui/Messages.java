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
 * $Id: Messages.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.ui;

import java.beans.Beans;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * UI properties.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public final class Messages {

    private Messages() {
        // do not instantiate
    }

    private static final String BUNDLE_NAME = "jalphanode-ui"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = Messages.loadBundle();

    private static ResourceBundle loadBundle() {
        return ResourceBundle.getBundle(Messages.BUNDLE_NAME);
    }

    public static String getString(final String key) {
        String returnValue;

        try {
            final ResourceBundle bundle = Beans.isDesignTime() ? Messages.loadBundle() : Messages.RESOURCE_BUNDLE;
            returnValue = bundle.getString(key);
        } catch (final MissingResourceException e) {
            returnValue = "!" + key + "!";
        }

        return returnValue;
    }
}
