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
 * $Id: FileUtils.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;

/**
 * Holds the logic of looking up a file, in the following sequence:
 * <ol>
 * <li>try to load it with the current thread's context ClassLoader</li>
 * <li>if fails, try to load it as a file from the disk</li>
 * </ol>
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public final class FileUtils {

    private static final Log LOG = LogFactory.getLog(FileUtils.class);

    private FileUtils() {
    }

    /**
     * Looks up the file, see : {@link FileUtils}.
     * 
     * @param filename might be the name of the file (too look it up in the class path) or an url to a file.
     * @return an input stream to the file or null if nothing found through all lookup steps.
     */
    public static InputStream lookupFile(final String filename) {
        Preconditions.checkNotNull(filename, "filename");

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(filename);

        if (stream == null) {
            FileUtils.LOG.debug(MessageFormat.format(
                    "Unable to find file {0} in classpath. Searching for this file on the filesystem instead.",
                    filename));
            try {
                stream = new FileInputStream(filename);
            } catch (final FileNotFoundException e) {
                FileUtils.LOG.warn(
                        MessageFormat.format("The file {0} was not found: {1}", filename, e.getLocalizedMessage()), e);
            }
        }

        return stream;
    }

    /**
     * Looks up the file, see : {@link FileUtils}.
     * 
     * @param filename might be the name of the file (too look it up in the class path) or an url to a file.
     * @return an {@link URL} to the file or null if nothing found through all lookup steps.
     */
    public static URL lookupFileLocation(final String filename) {
        Preconditions.checkNotNull(filename, "filename");

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(filename);

        if (url == null) {
            final File file = new File(filename);
            if (file.exists()) {
                try {
                    url = file.toURI().toURL();
                } catch (final MalformedURLException e) {
                    FileUtils.LOG.error(MessageFormat.format(
                            "The file {0} was found but an error ocurred while creating the URL cause: {1}", filename,
                            e.getLocalizedMessage()), e);
                }
            }
        }

        return url;
    }
}
