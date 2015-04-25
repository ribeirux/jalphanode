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
package org.jalphanode.jmx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Public classes annotated with this annotation will be exposed as MBeans. Take a look at
 * {@link org.jalphanode.jmx.annotation.ManagedAttribute}, {@link org.jalphanode.jmx.annotation.ManagedOperation} and
 * {@link org.jalphanode.jmx.annotation.ManagedParameter} for more details.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface MBean {

    /**
     * MBean object name. Refer to {@link javax.management.ObjectName} for more details.
     *
     * @return  the MBean object name
     */
    String objectName() default "";

    /**
     * Mbean description.
     *
     * @return  the MBean description
     */
    String description() default "";
}
