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
package org.jalphanode.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Properties configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class TypedProperties extends Properties implements TypedPropertiesConfig {

    /**
     * The serialVersionUID.
     */
    private static final long serialVersionUID = 2284916991567398693L;

    private static final Logger LOG = LoggerFactory.getLogger(TypedProperties.class);

    private static final String NULL_PROP_MSG = "Property [{}] is null. Using default value [{}]";

    /**
     * Default constructor that returns an empty instance.
     */
    public TypedProperties() {
        super();
    }

    /**
     * Copy constructor.
     *
     * @param  properties  properties instance to from. If null, then it is treated as an empty Properties instance.
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
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public boolean getBooleanProperty(final String key, final boolean defaultValue) {
        return this.getBooleanProperty(key, defaultValue, false);
    }

    /**
     * Gets a boolean property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public boolean getBooleanProperty(final String key, final boolean defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        boolean result = defaultValue;
        if (property == null) {
            LOG.warn(TypedProperties.NULL_PROP_MSG, key, defaultValue);
        } else {
            try {
                result = Boolean.parseBoolean(property);
            } catch (final Exception e) {
                LOG.warn("Unable to convert string property [{}] to a boolean! Using default value [{}]", property,
                    defaultValue, e);
            }
        }

        return result;
    }

    /**
     * Gets an int property.
     *
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public int getIntProperty(final String key, final int defaultValue) {
        return this.getIntProperty(key, defaultValue, false);
    }

    /**
     * Gets an int property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public int getIntProperty(final String key, final int defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        int result = defaultValue;
        if (property == null) {
            LOG.warn(TypedProperties.NULL_PROP_MSG, key, defaultValue);
        } else {
            try {
                result = Integer.parseInt(property);
            } catch (final NumberFormatException e) {
                LOG.warn("Unable to convert string property [{}] to an int! Using default value [{}]", property,
                    defaultValue, e);
            }
        }

        return result;
    }

    /**
     * Gets a long property.
     *
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public long getLongProperty(final String key, final long defaultValue) {
        return this.getLongProperty(key, defaultValue, false);
    }

    /**
     * Gets a long property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public long getLongProperty(final String key, final long defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        long result = defaultValue;
        if (property == null) {
            LOG.warn(TypedProperties.NULL_PROP_MSG, key, defaultValue);
        } else {
            try {
                result = Long.parseLong(property);
            } catch (final NumberFormatException e) {
                LOG.warn("Unable to convert string property [{}] to a long! Using default value [{}]", property,
                    defaultValue, e);
            }
        }

        return result;
    }

    /**
     * Gets a string property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    @Override
    public String getProperty(final String key, final String defaultValue, final boolean resolveSysProp) {

        final String property = this.getProperty(key, resolveSysProp);

        String result = defaultValue;
        if (property == null) {
            LOG.warn(TypedProperties.NULL_PROP_MSG, key, defaultValue);
        } else {
            result = property;
        }

        return result;
    }

    /**
     * Gets a property for specified {@code key}.
     *
     * @param   key             property key
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  property value
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

            result = ((property == null) ? value : property);
        }

        return result;
    }
}
