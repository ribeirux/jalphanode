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
 * $Id: ListenerInvocation.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.notification;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.text.MessageFormat;

import java.util.concurrent.Executor;

import com.google.common.base.Preconditions;

/**
 * Invokes a listener method.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class ListenerInvocation {

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
                    ListenerInvocation.this.method.invoke(ListenerInvocation.this.target, event);
                } catch (final InvocationTargetException e) {
                    throw new ListenerInvocationException(MessageFormat.format(
                            "Caught exception invoking listener method {0} on instance {1}",
                            ListenerInvocation.this.method, ListenerInvocation.this.target), e.getTargetException());
                } catch (final IllegalAccessException e) {
                    throw new ListenerInvocationException(MessageFormat.format(
                            "Unable to invoke listener method {0} on instance {1}.", ListenerInvocation.this.method,
                            ListenerInvocation.this.target), e);
                }
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
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
