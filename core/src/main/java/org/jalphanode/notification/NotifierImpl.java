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
 * $Id: NotifierImpl.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.notification;

import java.lang.annotation.Annotation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.jalphanode.annotation.AfterTask;
import org.jalphanode.annotation.BeforeTask;
import org.jalphanode.annotation.ViewChanged;

import org.jalphanode.cluster.NodeAddress;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import com.google.inject.Inject;

/**
 * Notifier implementation.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class NotifierImpl extends AbstractListener implements Notifier {

    private final List<ListenerInvocation> viewChangedListeners = Lists.newCopyOnWriteArrayList();
    private final List<ListenerInvocation> beforeTaskRunListeners = Lists.newCopyOnWriteArrayList();
    private final List<ListenerInvocation> afterTaskRunListeners = Lists.newCopyOnWriteArrayList();

    private final Map<Class<? extends Annotation>, Class<?>> allowedListeners = ImmutableMap
            .<Class<? extends Annotation>, Class<?>>of(    //
                ViewChanged.class, ViewChangedEvent.class, //
                BeforeTask.class, Event.class,             //
                AfterTask.class, Event.class);

    private final Map<Class<? extends Annotation>, List<ListenerInvocation>> listeners = ImmutableMap.of(
            ViewChanged.class, this.viewChangedListeners,  //
            BeforeTask.class, this.beforeTaskRunListeners, //
            AfterTask.class, this.afterTaskRunListeners);

    /**
     * Creates a new notifier.
     *
     * @param  asyncExecutor  asynchronous executor
     */
    @Inject
    public NotifierImpl(final ExecutorService asyncExecutor) {
        super(asyncExecutor);
    }

    @Override
    public Map<Class<? extends Annotation>, Class<?>> getAllowedMethodAnnotations() {
        return this.allowedListeners;
    }

    @Override
    protected Map<Class<? extends Annotation>, List<ListenerInvocation>> getListenersMap() {
        return this.listeners;
    }

    @Override
    public void notifyViewChange(final String nodeName, final String clusterName, final List<NodeAddress> members,
            final List<NodeAddress> oldMembers, final NodeAddress myAddress, final NodeAddress masterAddress,
            final boolean isMaster) {

        final EventImpl event = new EventImpl(nodeName, clusterName, myAddress, masterAddress, members, oldMembers,
                isMaster);

        this.invokeListeners(this.viewChangedListeners, event);
    }

    @Override
    public void beforeTask(final String taskName) {
        this.dispatchTask(taskName, this.beforeTaskRunListeners);
    }

    @Override
    public void afterTask(final String taskName) {
        this.dispatchTask(taskName, this.afterTaskRunListeners);
    }

    private void dispatchTask(final String taskName, final List<ListenerInvocation> listeners) {
        final EventImpl event = new EventImpl(taskName, null, null, null, null, null, null);
        this.invokeListeners(listeners, event);
    }
}
