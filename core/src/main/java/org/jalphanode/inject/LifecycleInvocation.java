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
 * $Id: TaskType.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.inject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.text.MessageFormat;

import com.google.common.base.Preconditions;

public class LifecycleInvocation implements Comparable<LifecycleInvocation> {

    private final Object[] NO_ARGS = {};

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
    public int compareTo(final LifecycleInvocation o) {
        return Integer.compare(this.priority, o.getPriority());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LifecycleInvocation [priority=");
        builder.append(priority);
        builder.append(", target=");
        builder.append(target);
        builder.append(", method=");
        builder.append(method);
        builder.append("]");
        return builder.toString();
    }

}
