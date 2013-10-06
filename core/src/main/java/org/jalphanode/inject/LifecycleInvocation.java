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
package org.jalphanode.inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.text.MessageFormat;

import com.google.common.base.Preconditions;

public class LifecycleInvocation {

    private static final Object[] NO_ARGS = {};

    private final int priority;
    private final Object target;
    private final Method method;

    public LifecycleInvocation(final int priority, final Object target, final Method method) {
        this.priority = priority;
        this.target = Preconditions.checkNotNull(target, "target");
        this.method = Preconditions.checkNotNull(method, "method");
    }

    public int getPriority() {
        return priority;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public void invoke() {
        try {
            this.method.invoke(this.target, NO_ARGS);
        } catch (InvocationTargetException e) {
            throw new LifecycleInvocationException(MessageFormat.format(
                    "Caught exception invoking lifecycle method {0} on instance {1}", this.method, this.target),
                e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new LifecycleInvocationException(MessageFormat.format(
                    "Unable to invoke lifecycle method {0} on instance {1}.", this.method, this.target), e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("LifecycleInvocation [priority=");
        builder.append(priority);
        builder.append(", target=");
        builder.append(target);
        builder.append(", method=");
        builder.append(method);
        builder.append(']');
        return builder.toString();
    }

}
