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
 * $Id: ConfigurationTest.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.List;
import java.util.TimeZone;

import org.jalphanode.scheduler.CronIterator;
import org.jalphanode.scheduler.SchedulerParseException;

import org.testng.Assert;

import org.testng.annotations.Test;

import com.google.common.io.Closeables;

/**
 * Configuration tests.
 *
 * @author   ribeirux
 * @version  $Revision: 274 $
 */
public class ConfigurationTest {

    @Test
    public void testDefaultConfig() {
        final JAlphaNodeConfig config = new JAlphaNodeType();

        // tasks
        final TasksConfig tasksConfig = config.getTasks();
        Assert.assertNotNull(tasksConfig);

        final List<TaskConfig> tasks = tasksConfig.getTask();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(tasks.size(), 0);

        // task scheduler
        final TaskSchedulerConfig taskScheduler = config.getTaskScheduler();
        Assert.assertNotNull(taskScheduler);
        Assert.assertNotNull(taskScheduler.getProperties());

        // Async Executor
        final AsyncNotificationExecutorConfig asyncNotificationExecutor = config.getAsyncNotificationExecutor();
        Assert.assertNotNull(asyncNotificationExecutor);
        Assert.assertNotNull(asyncNotificationExecutor.getProperties());

        // Membership Manager
        final MembershipConfig membershipConf = config.getMembership();
        Assert.assertNotNull(membershipConf);
        Assert.assertNotNull(membershipConf.getProperties());
        Assert.assertNotNull(membershipConf.getNodeName());
        Assert.assertNotNull(membershipConf.getClusterName());
    }

    private void validateConfig(final JAlphaNodeConfig config) {

        // Tasks
        final TasksConfig scheduledExecutor = config.getTasks();
        Assert.assertNotNull(scheduledExecutor);

        final List<TaskConfig> tasks = scheduledExecutor.getTask();
        Assert.assertNotNull(tasks);
        Assert.assertEquals(tasks.size(), 1);

        final TaskConfig task = tasks.get(0);
        Assert.assertNotNull(task);
        Assert.assertNotNull(task.getProperties());
        Assert.assertEquals(task.getProperties().size(), 1);
        Assert.assertEquals(task.getProperties().getProperty("name"), "value");
        Assert.assertEquals(task.getTaskName(), "TestName");
        Assert.assertNotNull(task.getScheduleIterator());
        Assert.assertEquals(task.getTask().getClass().getName(), "org.jalphanode.config.TestTask");

        // Task Scheduler
        final TaskSchedulerConfig taskScheduler = config.getTaskScheduler();
        Assert.assertNotNull(taskScheduler);
        Assert.assertNotNull(taskScheduler.getProperties());
        Assert.assertEquals(taskScheduler.getProperties().size(), 2);
        Assert.assertEquals(taskScheduler.getProperties().getProperty("threadNamePrefix"), "task-pool");
        Assert.assertEquals(taskScheduler.getProperties().getProperty("threadPriority"), "5");
        Assert.assertEquals(taskScheduler.getPoolSize(), Integer.valueOf(5));

        // Async Executor
        final AsyncNotificationExecutorConfig asyncNotificationExecutor = config.getAsyncNotificationExecutor();
        Assert.assertNotNull(asyncNotificationExecutor);
        Assert.assertNotNull(asyncNotificationExecutor.getProperties());
        Assert.assertEquals(asyncNotificationExecutor.getProperties().size(), 2);
        Assert.assertEquals(asyncNotificationExecutor.getProperties().getProperty("threadNamePrefix"), "async-pool");
        Assert.assertEquals(asyncNotificationExecutor.getProperties().getProperty("threadPriority"), "5");
        Assert.assertEquals(asyncNotificationExecutor.getPoolSize(), Integer.valueOf(5));

        // Membership Manager
        final MembershipConfig membershipConf = config.getMembership();
        Assert.assertNotNull(membershipConf);
        Assert.assertNotNull(membershipConf.getProperties());
        Assert.assertEquals(membershipConf.getProperties().size(), 1);
        Assert.assertEquals(membershipConf.getProperties().getProperty("configurationFile"), "jgroups-udp.xml");
        Assert.assertEquals(membershipConf.getNodeName(), "nodeName");
        Assert.assertEquals(membershipConf.getClusterName(), "clusterName");
    }

    @Test
    public void testInputStreamConfig() throws ConfigException {

        final String xml =
            "<jalphanode xmlns=\"urn:jalphanode:config\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xsi:schemaLocation=\"urn:jalphanode:config https://raw.github.com/ribeirux/jalphanode/master/core/src/main/resources/schema/jalphanode-config.xsd\">"
                + "<tasks><task class=\"org.jalphanode.config.TestTask\" "
                + "taskName=\"TestName\"><trigger><expression>10 1 * * * ?</expression>"
                + "<timezone>Europe/Lisbon</timezone></trigger><properties>"
                + "<property name=\"name\" value=\"value\" /></properties>"
                + "</task></tasks><taskScheduler poolSize=\"5\"><properties>"
                + "<property name=\"threadNamePrefix\" value=\"task-pool\" />"
                + "<property name=\"threadPriority\" value=\"5\" /></properties></taskScheduler>"
                + "<asyncNotificationExecutor poolSize=\"5\" >"
                + "<properties><property name=\"threadNamePrefix\" value=\"async-pool\" />"
                + "<property name=\"threadPriority\" value=\"5\" />"
                + "</properties></asyncNotificationExecutor><membership nodeName=\"nodeName\""
                + " clusterName=\"clusterName\"><properties>"
                + "<property name=\"configurationFile\" value=\"jgroups-udp.xml\" />"
                + "</properties></membership></jalphanode>";

        final InputStream is = new ByteArrayInputStream(xml.getBytes());

        try {
            final JAlphaNodeConfig config = JAlphaNodeConfigBuilder.buildFromStream(is);
            this.validateConfig(config);
        } finally {
            Closeables.closeQuietly(is);
        }
    }

    @Test
    public void testGoodFileConfig() throws ConfigException {
        final JAlphaNodeConfig config = JAlphaNodeConfigBuilder.buildFromFile("good-jalphanode-config.xml");
        this.validateConfig(config);
    }

    @Test
    public void testBuilderConfig() throws SchedulerParseException {

        final JAlphaNodeConfig config = new JAlphaNodeConfigBuilder().addTask(new TestTask()).withName("TestName")
                                                                     .withScheduler(new CronIterator("10 1 * * * ?",
                                                                             TimeZone.getTimeZone("Europe/Lisbon")))
                                                                     .addProperty("name", "value").scheduler()
                                                                     .withPoolSize(5)
                                                                     .addProperty("threadNamePrefix", "task-pool")
                                                                     .addProperty("threadPriority", "5")
                                                                     .asyncNotificationExecutor().withPoolSize(5)
                                                                     .addProperty("threadNamePrefix", "async-pool")
                                                                     .addProperty("threadPriority", "5").membership()
                                                                     .withNodeName("nodeName")
                                                                     .withClusterName("clusterName")
                                                                     .addProperty("configurationFile",
                "jgroups-udp.xml").build();

        this.validateConfig(config);
    }

    @Test(expectedExceptions = {ConfigException.class})
    public void testBadFileConfig() throws ConfigException {
        final JAlphaNodeConfig config = JAlphaNodeConfigBuilder.buildFromFile("bad-jalphanode-config.xml");
    }
}
