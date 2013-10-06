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

import java.lang.annotation.Annotation;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.jalphanode.annotation.AfterTask;
import org.jalphanode.annotation.BeforeTask;
import org.jalphanode.annotation.NotifierExecutor;
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
    public NotifierImpl(@NotifierExecutor final Executor asyncExecutor) {
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
