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
package org.jalphanode;

import org.jalphanode.TaskManager.Status;

import org.jalphanode.annotation.Listener;
import org.jalphanode.annotation.ViewChanged;

import org.jalphanode.config.ConfigException;

import org.jalphanode.notification.ViewChangedEvent;

import org.testng.Assert;

import org.testng.annotations.Test;

/**
 * Default task manager tests.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class DefaultTaskManagerTest {

    @Test
    public void testDefaultConfig() throws ConfigException {
        final TaskManager taskManager = new DefaultTaskManager();

        Assert.assertEquals(taskManager.getStatus(), Status.INSTANTIATED);
        Assert.assertEquals(taskManager.getListeners().size(), 0);
        Assert.assertNotNull(taskManager.getConfig());

        taskManager.start();

        Assert.assertEquals(taskManager.getStatus(), Status.RUNNING);
        Assert.assertEquals(taskManager.getListeners().size(), 0);
        Assert.assertNotNull(taskManager.getConfig());

        taskManager.shutdown();
    }

    @Listener
    public static class ListenerTest {

        private boolean assertError = false;
        private boolean executed = false;

        @ViewChanged
        public void viewChanged(final ViewChangedEvent event) {
            if (this.assertError) {
                Assert.fail("view changed");
            } else {
                this.executed = true;
            }
        }

        /**
         * Gets the assertError property.
         *
         * @return  the assertError property
         */
        public boolean isExecuted() {
            return this.executed;
        }

        /**
         * Sets the assertError property.
         *
         * @param  assertError  the assertError to set
         */
        public void setAssertError(final boolean assertError) {
            this.assertError = assertError;
        }

    }

    public static class ListenerTestChild extends ListenerTest { }

    @Test
    public void testListener() throws ConfigException {

        final TaskManager taskManager = new DefaultTaskManager();

        final ListenerTest listenerTest = new ListenerTest();
        taskManager.addListener(listenerTest);

        final ListenerTestChild listenerTestChild = new ListenerTestChild();
        taskManager.addListener(listenerTestChild);

        Assert.assertEquals(taskManager.getListeners().size(), 2);

        taskManager.start();

        Assert.assertTrue(listenerTest.isExecuted());
        Assert.assertTrue(listenerTestChild.isExecuted());

        Assert.assertEquals(taskManager.getListeners().size(), 2);

        taskManager.removeListener(listenerTest);
        listenerTest.setAssertError(true);

        Assert.assertEquals(taskManager.getListeners().size(), 1);

        taskManager.removeListener(listenerTestChild);
        listenerTestChild.setAssertError(true);

        Assert.assertEquals(taskManager.getListeners().size(), 0);

        taskManager.shutdown();
    }

}
