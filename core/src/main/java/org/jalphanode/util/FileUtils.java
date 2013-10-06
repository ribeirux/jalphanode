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
package org.jalphanode.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Holds the logic of looking up a file, in the following sequence:
 *
 * <ol>
 *   <li>try to load it with the current thread's context ClassLoader</li>
 *   <li>if fails, try to load it as a file from the disk</li>
 * </ol>
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public final class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
        // utilities class
    }

    /**
     * Looks up the file, see : {@link FileUtils}.
     *
     * @param   filename  might be the name of the file (too look it up in the class path) or an url to a file.
     *
     * @return  an input stream to the file or null if nothing found through all lookup steps.
     */
    public static InputStream lookupFile(final String filename) {
        Preconditions.checkNotNull(filename, "filename");

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(filename);

        if (stream == null) {
            LOG.debug("Unable to find file {} in classpath. Searching for this file on the filesystem instead.",
                filename);
            try {
                stream = new FileInputStream(filename);
            } catch (final FileNotFoundException e) {
                LOG.warn("The file {} was not found: {}", filename, e.getLocalizedMessage(), e);
            }
        }

        return stream;
    }

    /**
     * Looks up the file, see : {@link FileUtils}.
     *
     * @param   filename  might be the name of the file (too look it up in the class path) or an url to a file.
     *
     * @return  an {@link URL} to the file or null if nothing found through all lookup steps.
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
                    LOG.error("The file {} was found but an error ocurred while creating the URL cause: {}", filename,
                        e.getLocalizedMessage(), e);
                }
            }
        }

        return url;
    }
}
