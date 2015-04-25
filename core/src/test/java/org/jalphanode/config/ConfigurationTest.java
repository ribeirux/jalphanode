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
package org.jalphanode.config;

import org.jalphanode.scheduler.CronIterator;
import org.jalphanode.scheduler.SchedulerParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.TimeZone;

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
    public void testInputStreamConfig() throws Exception {

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

        try (InputStream is = new ByteArrayInputStream(xml.getBytes())) {
            final JAlphaNodeConfig config = JAlphaNodeConfigBuilder.buildFromStream(is);
            this.validateConfig(config);
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

    @Test(expectedExceptions = ConfigException.class)
    public void testBadFileConfig() throws ConfigException {
        JAlphaNodeConfigBuilder.buildFromFile("bad-jalphanode-config.xml");
    }
}
