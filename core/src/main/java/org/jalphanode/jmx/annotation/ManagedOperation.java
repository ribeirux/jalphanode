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

import javax.management.MBeanOperationInfo;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a public method should be exposed as a MBean operation. This annotation can be applied to a public
 * method of a public class that is itself is optionally annotated with an @MBean annotation, or inherits such an
 * annotation from a superclass.
 *
 * @author  pribeiro
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ManagedOperation {

    String name() default "";

    String description() default "";

    int impact() default MBeanOperationInfo.UNKNOWN;
}
