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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import org.jalphanode.annotation.Listener;
import org.jalphanode.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Generic listener.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public abstract class AbstractListener {

    private final Executor asyncExecutor;
    private final Executor syncExecutor;

    /**
     * Initializes internal fields.
     *
     * @param  asyncExecutor  asynchronous executor
     */
    protected AbstractListener(final Executor asyncExecutor) {
        this.asyncExecutor = Preconditions.checkNotNull(asyncExecutor, "asyncExecutor");
        this.syncExecutor = MoreExecutors.directExecutor();
    }

    /**
     * Adds a new listener.
     *
     * @param  listener  listener to add
     */
    public void addListener(final Object listener) {
        this.validateAndAddListenerInvocation(listener);
    }

    /**
     * Gets all listeners with specified annotation.
     *
     * @param   annotation  annotation class
     *
     * @return  a collection of listeners with specified annotation
     */
    public List<ListenerInvocation> getListenerCollectionForAnnotation(final Class<? extends Annotation> annotation) {
        return this.getListenersMap().get(Preconditions.checkNotNull(annotation, "annotation"));
    }

    /**
     * Gets all listeners.
     *
     * @return  all listeners
     */
    public Set<Object> getListeners() {
        final ImmutableSet.Builder<Object> builder = new ImmutableSet.Builder<>();
        for (final List<ListenerInvocation> list : this.getListenersMap().values()) {
            for (final ListenerInvocation li : list) {
                builder.add(li.getTarget());
            }
        }

        return builder.build();
    }

    /**
     * Removes all listeners from the notifier.
     */
    public void removeAllListeners() {
        this.getListenersMap().values().stream().filter(list -> list != null).forEach(List<ListenerInvocation>::clear);
    }

    /**
     * Removes specified listener.
     *
     * @param  listener  listener to remove
     */
    public void removeListener(final Object listener) {
        Preconditions.checkNotNull(listener, "listener");

        for (final Class<? extends Annotation> annotation : this.getAllowedMethodAnnotations().keySet()) {
            this.removeListenerInvocation(annotation, listener);
        }
    }

    /**
     * Remove the listener with specified annotation.
     *
     * @param  annotation  listener annotation
     * @param  listener    listener to remove
     */
    public void removeListenerInvocation(final Class<? extends Annotation> annotation, final Object listener) {
        Preconditions.checkNotNull(annotation, "annotation");
        Preconditions.checkNotNull(listener, "listener");

        final List<ListenerInvocation> listeners = this.getListenerCollectionForAnnotation(annotation);
        listeners.removeAll(listeners.stream()
                .filter(li -> listener.equals(li.getTarget()))
                .collect(Collectors.toSet()));
    }

    /**
     * Tests if a class is properly annotated with Listener and returns whether callbacks on this class should be
     * invoked synchronously or asynchronously.
     *
     * @param   listenerClass  class to inspect
     *
     * @return  true if callbacks on this class should use the syncProcessor or false if it should use the
     *          asyncProcessor.
     */
    protected boolean testListenerClassValidity(final Class<?> listenerClass) {
        final Listener listener = ReflectionUtils.getAnnotation(listenerClass, Listener.class);
        if (listener == null) {
            throw new MalformedListenerException("Listener class should be annotated with listener annotation");
        }

        if (!Modifier.isPublic(listenerClass.getModifiers())) {
            throw new MalformedListenerException("Listener modifiers should be public!");
        }

        return listener.sync();
    }

    /**
     * Tests if the listener method is valid.
     *
     * @param  method            method to test
     * @param  allowedParameter  allowed parameter
     * @param  annotationName    method annotation
     */
    protected void testListenerMethodValidity(final Method method, final Class<?> allowedParameter,
            final String annotationName) {

        final int mod = method.getModifiers();
        if (!Modifier.isPublic(mod) || Modifier.isStatic(mod)) {
            throw new MalformedListenerException(MessageFormat.format(
                    "Methods annotated with {0} should be public and non static!", annotationName));
        }

        if ((method.getParameterTypes().length != 1)
                || !method.getParameterTypes()[0].isAssignableFrom(allowedParameter)) {
            throw new MalformedListenerException(MessageFormat.format(
                    "Methods annotated with {0}  must accept exactly one parameter, of assignable from type {1}",
                    annotationName, allowedParameter.getName()));
        }

        if (!method.getReturnType().equals(void.class)) {
            throw new MalformedListenerException(MessageFormat.format(
                    "Methods annotated with {0} should have a return type of void.", annotationName));
        }
    }

    /**
     * Loops through all valid methods on the object passed in, and caches the relevant methods as
     * {@link ListenerInvocation} for invocation by reflection.
     *
     * @param  listener  object to be considered as a listener.
     */
    private void validateAndAddListenerInvocation(final Object listener) {

        Preconditions.checkNotNull(listener, "listener");

        final boolean sync = this.testListenerClassValidity(listener.getClass());
        boolean foundMethods = false;
        final Map<Class<? extends Annotation>, Class<?>> allowedListeners = this.getAllowedMethodAnnotations();

        // now try all methods on the listener for anything that we like. Note that only PUBLIC methods are scanned.
        for (final Method m : listener.getClass().getMethods()) {

            // loop through all valid method annotations
            for (final Map.Entry<Class<? extends Annotation>, Class<?>> annotationEntry : allowedListeners.entrySet()) {
                final Class<? extends Annotation> key = annotationEntry.getKey();
                final Class<?> value = annotationEntry.getValue();
                if (m.isAnnotationPresent(key)) {
                    this.testListenerMethodValidity(m, value, key.getName());
                    this.addListenerInvocation(key,
                        new ListenerInvocation(listener, m, (sync ? this.syncExecutor : this.asyncExecutor)));
                    foundMethods = true;
                }
            }
        }

        if (!foundMethods) {
            throw new MalformedListenerException(MessageFormat.format(
                    "Attempted to register listener of class {0} , but no valid public methods "
                        + "annotated with method-level event annotations found!", listener.getClass()));
        }
    }

    private void addListenerInvocation(final Class<? extends Annotation> annotation, final ListenerInvocation li) {
        this.getListenerCollectionForAnnotation(annotation).add(li);
    }

    protected void invokeListeners(final Iterable<ListenerInvocation> listeners, final Object event) {
        listeners.forEach(l -> l.invoke(event));
    }

    /**
     * Gets allowed method annotations.
     *
     * @return  all allowed method annotations
     */
    public abstract Map<Class<? extends Annotation>, Class<?>> getAllowedMethodAnnotations();

    /**
     * Gets all listeners.
     *
     * @return  all listeners
     */
    protected abstract Map<Class<? extends Annotation>, List<ListenerInvocation>> getListenersMap();

}
