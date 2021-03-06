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

import com.google.common.base.Preconditions;
import org.jalphanode.config.ClassNotAssignableException;
import org.jalphanode.config.ConfigBindingException;
import org.jalphanode.config.ConfigClassNotFoundException;
import org.jalphanode.config.ConfigException;
import org.jalphanode.config.ConfigFileNotFoundException;
import org.jalphanode.config.JAlphaNodeType;
import org.jalphanode.config.XMLEventHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Utility methods for configurations.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public final class ConfigurationUtils {

    /**
     * Resource file name.
     */
    private static final String BUNDLE_NAME = "jalphanode";

    /**
     * Resource bundle.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(ConfigurationUtils.BUNDLE_NAME);

    private ConfigurationUtils() {
        // utilities class
    }

    /**
     * Search for the file name on classpath and file system.
     *
     * @param   fileName  file name
     *
     * @return  the input stream of the file
     *
     * @throws  ConfigException  thrown if the file was not found
     */
    public static InputStream findInputStream(final String fileName) throws ConfigException {

        final InputStream stream = FileUtils.lookupFile(Preconditions.checkNotNull(fileName, "fileName"));

        if (stream == null) {
            throw new ConfigFileNotFoundException(MessageFormat.format(
                    "File {0} could not be found, either on the classpath or on the file system!", fileName));
        }

        return stream;
    }

    /**
     * Instantiates a class based on the provided class name.
     *
     * <p>Any exceptions encountered loading and instantiating the class is wrapped in a {@link ConfigException}.
     *
     * @param   <T>              assignable class
     * @param   classname        class to instantiate
     * @param   assignableClass  type of the class to return
     *
     * @return  the loaded class
     *
     * @throws  ConfigException  thrown if an error occurs while loading the class
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClass(final String classname, final Class<T> assignableClass) throws ConfigException {

        Preconditions.checkNotNull(classname, "classname");
        Preconditions.checkNotNull(assignableClass, "assignableClass");

        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            final Class<?> loadedClass = classLoader.loadClass(classname);

            if (!assignableClass.isAssignableFrom(loadedClass)) {
                throw new ClassNotAssignableException(MessageFormat.format(
                        "Class {0} is not assignable from {1}. Class {0} should extend or implement {1}.", classname,
                        assignableClass.getName()));
            }

            return (Class<T>) loadedClass;

        } catch (final ClassNotFoundException e) {
            throw new ConfigClassNotFoundException("Class not found: " + classname, e);
        }
    }

    /**
     * Factory method to create an object from input stream. If users want to verify file correctness against schema
     * then appropriate schema input stream should be provided as well.
     *
     * @param   <T>          generic type
     * @param   input        input stream
     * @param   schema       schema input stream
     * @param   returnClass  class of the object to return
     *
     * @return  T a new instance of type T
     *
     * @throws  ConfigException  if there are any issues creating jalphanode configuration
     */
    public static <T> T unmarshall(final InputStream input, final InputStream schema, final Class<T> returnClass)
        throws ConfigException {

        Preconditions.checkNotNull(input, "input");
        Preconditions.checkNotNull(returnClass, "returnClass");

        try {
            final JAXBContext ctx = JAXBContext.newInstance(returnClass);
            final Unmarshaller umr = ctx.createUnmarshaller();

            if (schema != null) {
                final SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                umr.setSchema(factory.newSchema(new StreamSource(schema)));
                umr.setEventHandler(new XMLEventHandler());
            }

            final Object rsConfig = umr.unmarshal(input);

            return returnClass.cast(rsConfig);
        } catch (final Exception e) {
            throw new ConfigBindingException(e);
        }
    }

    /**
     * Converts a String representing an XML snippet into an {@link org.w3c.dom.Element}.
     *
     * @param   xml  snippet as a string
     *
     * @return  a DOM Element
     *
     * @throws  ConfigException  if there are any issues creating jalphanode configuration
     */
    public static Element stringToElement(final String xml) throws ConfigException {

        Preconditions.checkNotNull(xml, "xml");

        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes(JAlphaNodeType.CONFIG_CHARSET))) {
                final Document document = builder.parse(bais);

                return document.getDocumentElement();
            }
        } catch (final Exception e) {
            throw new ConfigBindingException(e);
        }
    }

    /**
     * Gets a resource property by key.
     *
     * @param   key  key to search
     *
     * @return  the key property
     */
    public static String getResourceProperty(final String key) {
        return ConfigurationUtils.RESOURCE_BUNDLE.getString(Preconditions.checkNotNull(key, "key"));
    }
}
