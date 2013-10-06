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
package org.jalphanode.notification;

import java.util.Set;

/**
 * Denotes that the implementation can have listeners attached to it.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public interface Listenable {

    /**
     * Adds a listener to the component.
     *
     * <p/>
     *
     * @param  listener  must not be null.
     */
    void addListener(Object listener);

    /**
     * Gets all registered listeners.
     *
     * @return  a set of all listeners registered on this component.
     */
    Set<Object> getListeners();

    /**
     * Removes a listener from the component.
     *
     * @param  listener  listener to remove. Must not be null.
     */
    void removeListener(Object listener);
}
