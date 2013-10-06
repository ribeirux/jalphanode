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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Invokes a listener method.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class ListenerInvocation {

    private static final Logger LOG = LoggerFactory.getLogger(ListenerInvocation.class);

    private final Object target;
    private final Method method;
    private final Executor executor;

    /**
     * Creates a new listener invocation.
     *
     * @param  target    listener
     * @param  method    method to run
     * @param  executor  executor
     */
    public ListenerInvocation(final Object target, final Method method, final Executor executor) {
        this.target = Preconditions.checkNotNull(target, "target");
        this.method = Preconditions.checkNotNull(method, "method");
        this.executor = Preconditions.checkNotNull(executor, "executor");
    }

    /**
     * Gets the method property.
     *
     * @return  the method property
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * Gets the target property.
     *
     * @return  the target property
     */
    public Object getTarget() {
        return this.target;
    }

    /**
     * Invokes the listener method.
     *
     * @param  event  event to pass to the method
     */
    public void invoke(final Object event) {
        Preconditions.checkNotNull(event, "event");
        this.executor.execute(this.buildRunnable(event));
    }

    private Runnable buildRunnable(final Object event) {
        return new Runnable() {

            @Override
            public void run() {
                try {
                    method.invoke(target, event);
                } catch (final InvocationTargetException e) {
                    LOG.error("Caught exception invoking listener method {} on instance {}", method, target,
                        e.getTargetException());
                } catch (final IllegalAccessException e) {
                    LOG.error("Unable to invoke listener method {} on instance {}.", method, target, e);
                }
            }
        };
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ListenerInvocation [target=");
        builder.append(target);
        builder.append(", method=");
        builder.append(method);
        builder.append(", executor=");
        builder.append(executor);
        builder.append(']');
        return builder.toString();
    }

}
