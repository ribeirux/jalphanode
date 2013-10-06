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

/**
 * Typed properties configuration.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public interface TypedPropertiesConfig {

    /**
     * Gets a boolean property.
     *
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    boolean getBooleanProperty(final String key, final boolean defaultValue);

    /**
     * Gets a boolean property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    boolean getBooleanProperty(final String key, final boolean defaultValue, final boolean resolveSysProp);

    /**
     * Gets an int property.
     *
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    int getIntProperty(final String key, final int defaultValue);

    /**
     * Gets an int property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    int getIntProperty(final String key, final int defaultValue, final boolean resolveSysProp);

    /**
     * Gets a long property.
     *
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    long getLongProperty(final String key, final long defaultValue);

    /**
     * Gets a long property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    long getLongProperty(final String key, final long defaultValue, final boolean resolveSysProp);

    /**
     * Gets a property for specified <code>key</code>.
     *
     * @param   key  property key
     *
     * @return  property value
     */
    String getProperty(final String key);

    /**
     * Gets a string property.
     *
     * @param   key           property key
     * @param   defaultValue  default value
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    String getProperty(final String key, final String defaultValue);

    /**
     * Gets a property for specified <code>key</code>.
     *
     * @param   key             property key
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  property value
     */
    String getProperty(final String key, final boolean resolveSysProp);

    /**
     * Gets a string property.
     *
     * @param   key             property key
     * @param   defaultValue    default value
     * @param   resolveSysProp  if true checks if the property value exists as system property
     *
     * @return  if the key property exists, then its value is returned otherwise returns the default value
     */
    String getProperty(final String key, final String defaultValue, final boolean resolveSysProp);

    /**
     * Checks if the key exists.
     *
     * @param   key  key to check
     *
     * @return  true if the key exists otherwise false
     */
    boolean containsKey(Object key);

    /**
     * Gets the number of configured properties.
     *
     * @return  the number of configured properties.
     */
    int size();

}
