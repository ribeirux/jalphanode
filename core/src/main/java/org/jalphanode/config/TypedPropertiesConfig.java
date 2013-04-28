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
