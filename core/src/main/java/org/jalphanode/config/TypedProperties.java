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
 * $Id: TypedProperties.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Properties configuration.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class TypedProperties extends Properties implements TypedPropertiesConfig {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = 2284916991567398693L;

    private static final Log LOG = LogFactory.getLog(TypedProperties.class);

    private static final String NULL_PROP_MSG = "Property [{0}] is null. Using default value [{1}]";

    /**
     * Default constructor that returns an empty instance.
     */
    public TypedProperties() {
        super();
    }

    /**
     * Copy constructor.
     * 
     * @param properties properties instance to from. If null, then it is treated as an empty Properties instance.
     */
    public TypedProperties(final Properties properties) {
        super();
        if ((properties != null) && !properties.isEmpty()) {
            this.putAll(properties);
        }
    }

    /**
     * Gets a boolean property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public boolean getBooleanProperty(final String key, final boolean defaultValue) {
        return this.getBooleanProperty(key, defaultValue, false);
    }

    /**
     * Gets a boolean property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @param resolveSysProp if true checks if the property value exists as system property
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public boolean getBooleanProperty(final String key, final boolean defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        boolean result;

        if (property == null) {
            result = defaultValue;
            TypedProperties.LOG.warn(MessageFormat.format(TypedProperties.NULL_PROP_MSG, key, defaultValue));
        } else {
            try {
                result = Boolean.parseBoolean(property);
            } catch (final Exception e) {
                result = defaultValue;
                TypedProperties.LOG.warn(MessageFormat.format(
                        "Unable to convert string property [{0}] to a boolean! Using default value [{1}]", property,
                        defaultValue), e);
            }
        }

        return result;
    }

    /**
     * Gets an int property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public int getIntProperty(final String key, final int defaultValue) {
        return this.getIntProperty(key, defaultValue, false);
    }

    /**
     * Gets an int property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @param resolveSysProp if true checks if the property value exists as system property
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public int getIntProperty(final String key, final int defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        int result;

        if (property == null) {
            result = defaultValue;
            TypedProperties.LOG.warn(MessageFormat.format(TypedProperties.NULL_PROP_MSG, key, defaultValue));
        } else {
            try {
                result = Integer.parseInt(property);
            } catch (final NumberFormatException e) {
                result = defaultValue;
                TypedProperties.LOG.warn(MessageFormat.format(
                        "Unable to convert string property [{0}] to an int! Using default value [{1}]", property,
                        defaultValue), e);
            }
        }

        return result;
    }

    /**
     * Gets a long property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public long getLongProperty(final String key, final long defaultValue) {
        return this.getLongProperty(key, defaultValue, false);
    }

    /**
     * Gets a long property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @param resolveSysProp if true checks if the property value exists as system property
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public long getLongProperty(final String key, final long defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        long result;

        if (property == null) {
            result = defaultValue;
            TypedProperties.LOG.warn(MessageFormat.format(TypedProperties.NULL_PROP_MSG, key, defaultValue));
        } else {
            try {
                result = Long.parseLong(property);
            } catch (final NumberFormatException e) {
                result = defaultValue;
                TypedProperties.LOG.warn(MessageFormat.format(
                        "Unable to convert string property [{0}] to a long! Using default value [{1}]", property,
                        defaultValue), e);
            }
        }

        return result;
    }

    /**
     * Gets a string property.
     * 
     * @param key property key
     * @param defaultValue default value
     * @param resolveSysProp if true checks if the property value exists as system property
     * @return if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public String getProperty(final String key, final String defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        String result;
        if (property == null) {
            result = defaultValue;
            TypedProperties.LOG.warn(MessageFormat.format(TypedProperties.NULL_PROP_MSG, key, defaultValue));
        } else {
            result = property;
        }

        return result;
    }

    /**
     * Gets a property for specified <code>key</code>.
     * 
     * @param key property key
     * @param resolveSysProp if true checks if the property value exists as system property
     * @return property value
     */
    @Override
    public String getProperty(final String key, final boolean resolveSysProp) {

        String value = this.getProperty(Preconditions.checkNotNull(key, "key"));

        String result = null;

        if (!Strings.isNullOrEmpty(value)) {
            value = value.trim();
            String property = null;
            if (resolveSysProp) {
                property = System.getProperty(value);
            }
            result = (property == null ? value : property);
        }

        return result;
    }
}
