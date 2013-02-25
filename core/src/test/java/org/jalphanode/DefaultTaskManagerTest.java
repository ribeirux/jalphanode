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
 * $Id: DefaultTaskManagerTest.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode;

import java.util.Date;

import org.jalphanode.annotation.Listener;
import org.jalphanode.annotation.ViewChanged;
import org.jalphanode.config.ConfigException;
import org.jalphanode.notification.ListenerMethodException;
import org.jalphanode.notification.ViewChangedEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Default task manager tests.
 * 
 * @author ribeirux
 * @version $Revision: 274 $
 */
public class DefaultTaskManagerTest {

    @Test
    public void testDefaultConfig() throws ConfigException {
        final TaskManager taskManager = new DefaultTaskManager();

        Assert.assertEquals(taskManager.getStatus(), Status.INSTANTIATED);
        Assert.assertEquals(taskManager.getListeners().size(), 0);
        Assert.assertNotNull(taskManager.getConfig());

        taskManager.start();

        final Period period = taskManager.getRunningTime();
        Assert.assertNotNull(period, period.toString());
        final Date startDate = taskManager.getStartDate();
        Assert.assertNotNull(startDate, startDate.toString());
        Assert.assertEquals(taskManager.getStatus(), Status.RUNNING);
        Assert.assertEquals(taskManager.getListeners().size(), 0);
        Assert.assertNotNull(taskManager.getConfig());

        taskManager.shutdown();
    }

    @Listener
    public class ListenerTest {

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
         * @return the assertError property
         */
        public boolean isExecuted() {
            return this.executed;
        }

        /**
         * Sets the assertError property.
         * 
         * @param assertError the assertError to set
         */
        public void setAssertError(final boolean assertError) {
            this.assertError = assertError;
        }

    }

    public class ListenerTestChild extends ListenerTest {
    }

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

    @Test(expectedExceptions = { ListenerMethodException.class })
    public void testListenerException() throws ConfigException {

        final TaskManager taskManager = new DefaultTaskManager();

        taskManager.addListener(new Object());
    }

    @Listener
    public class MethodTest {

        @ViewChanged
        void viewChanged(final ViewChangedEvent event) {
        }
    }

    @Test(expectedExceptions = { ListenerMethodException.class })
    public void testListenerMethod() throws ConfigException {

        final TaskManager taskManager = new DefaultTaskManager();

        taskManager.addListener(new MethodTest());
    }

    @Listener
    class ClassTest {

        @ViewChanged
        public void viewChanged(final ViewChangedEvent event) {
        }
    }

    @Test(expectedExceptions = { ListenerMethodException.class })
    public void testListenerPublicModifier() throws ConfigException {

        final TaskManager taskManager = new DefaultTaskManager();

        taskManager.addListener(new ClassTest());
    }

}
