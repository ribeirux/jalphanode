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
 * Generic configuration.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface PropertiesContainerConfig {

    /**
     * Gets the properties of the component.
     *
     * @return  the properties of the component
     */
    TypedPropertiesConfig getProperties();

}
