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
package org.jalphanode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class-level annotation used to annotate an object as being a valid listener.
 *
 * <p>Note that even if a class is annotated with this annotation, it still needs method-level annotation to actually
 * receive notifications.
 *
 * Delivery Semantics:
 *
 * <p>An event is delivered immediately after the respective operation, but before the underlying call returns. For
 * this reason it is important to keep listener processing logic short-lived. If a long running task needs to be
 * performed, it's recommended to use option sync = false, to execute the task asynchronously.
 *
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Listener {

    /**
     * Specifies whether callbacks on any class annotated with this annotation happens synchronously (in the caller's
     * thread) or asynchronously (using a separate thread). Defaults to <tt>true</tt>.
     *
     * @return  true if the expectation is that callbacks are called using the caller's thread; false if they are to be
     *          made in a separate thread.
     */
    boolean sync() default true;
}
