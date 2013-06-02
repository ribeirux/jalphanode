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
 * $Id$
 *******************************************************************************/
package org.jalphanode.config;

import java.io.InputStream;


import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.task.Task;

import org.jalphanode.util.ConfigurationUtils;

import com.google.common.base.Preconditions;
import com.google.common.io.Closeables;

/**
 * JAlphanode configuration builder.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public class JAlphaNodeConfigBuilder {

    private final JAlphaNodeType config;

    /**
     * Creates a new configuration builder.
     */
    public JAlphaNodeConfigBuilder() {
        this(new JAlphaNodeType());
    }

    protected JAlphaNodeConfigBuilder(final JAlphaNodeType config) {
        this.config = config;
    }

    /**
     * Adds a new task for execution.
     *
     * @param   task  task to execute
     *
     * @return  the task builder instance
     */
    public TaskBuilder addTask(final Task task) {

        final TaskType taskType = new TaskType();
        taskType.setTask(task);
        this.config.getTasks().addTask(taskType);

        return new TaskBuilder(this.config, taskType);
    }

    /**
     * Gets the task scheduler builder.
     *
     * @return  the task scheduler builder instance
     */
    public TaskSchedulerBuilder scheduler() {
        return new TaskSchedulerBuilder(this.config);
    }

    /**
     * Gets the async executor builder.
     *
     * @return  the async executor builder instance
     */
    public AsyncNotificationExecutorBuilder asyncNotificationExecutor() {
        return new AsyncNotificationExecutorBuilder(this.config);
    }

    /**
     * Gets the membership builder.
     *
     * @return  the membership builder instance
     */
    public MembershipBuilder membership() {
        return new MembershipBuilder(this.config);
    }

    /**
     * Builds the configuration.
     *
     * @return  the jalphanode configuration.
     */
    public JAlphaNodeConfig build() {
        return this.config;
    }

    protected JAlphaNodeType getConfig() {
        return this.config;
    }

    /**
     * Factory method to create an instance of jalphanode configuration. If users want to verify configuration file
     * correctness against schema then appropriate schema file name should be provided as well.
     *
     * <p/>Both configuration file and schema file are looked up in following order:
     *
     * <p/>
     * <ol>
     *   <li>using current thread's context ClassLoader</li>
     *   <li>if fails, the system ClassLoader</li>
     *   <li>if fails, attempt is made to load it as a file from the disk</li>
     * </ol>
     *
     * @param   configStream  input stream configuration
     *
     * @return  jalphanode configuration
     *
     * @throws  ConfigException  if there are any issues creating jalphanode configuration
     */
    public static JAlphaNodeConfig buildFromStream(final InputStream configStream) throws ConfigException {

        Preconditions.checkNotNull(configStream, "configStream");

        InputStream schemaIS = null;

        try {
            schemaIS = ConfigurationUtils.findInputStream(ResourceKeys.SCHEMA_LOCATION.getValue());
            return ConfigurationUtils.unmarshall(configStream, schemaIS, JAlphaNodeType.class);
        } finally {
            Closeables.closeQuietly(schemaIS);
        }
    }

    /**
     * Factory method to create an instance of jalphanode configuration. If users want to verify configuration file
     * correctness against schema then appropriate schema file name should be provided as well.
     *
     * <p/>Both configuration file and schema file are looked up in following order:
     *
     * <p/>
     * <ol>
     *   <li>using current thread's context ClassLoader</li>
     *   <li>if fails, the system ClassLoader</li>
     *   <li>if fails, attempt is made to load it as a file from the disk</li>
     * </ol>
     *
     * @param   configFileName  configuration file name
     *
     * @return  jalphanode configuration
     *
     * @throws  ConfigException  if there are any issues creating jalphanode configuration
     */
    public static JAlphaNodeConfig buildFromFile(final String configFileName) throws ConfigException {

        Preconditions.checkNotNull(configFileName, "configFileName");

        InputStream configIS = null;
        InputStream schemaIS = null;

        try {
            configIS = ConfigurationUtils.findInputStream(configFileName);
            schemaIS = ConfigurationUtils.findInputStream(ResourceKeys.SCHEMA_LOCATION.getValue());

            return ConfigurationUtils.unmarshall(configIS, schemaIS, JAlphaNodeType.class);
        } finally {
            Closeables.closeQuietly(configIS);
            Closeables.closeQuietly(schemaIS);
        }
    }

    /**
     * Task builder.
     *
     * @author   ribeirux
     * @version  $Revision$
     */
    public static class TaskBuilder extends JAlphaNodeConfigBuilder {

        private final TaskType taskType;

        protected TaskBuilder(final JAlphaNodeType config, final TaskType taskType) {
            super(config);
            this.taskType = taskType;
        }

        /**
         * The current task will have the specified name.
         *
         * @param   taskName  task name
         *
         * @return  the current task builder instance
         */
        public TaskBuilder withName(final String taskName) {
            this.taskType.setTaskName(taskName);
            return this;
        }

        /**
         * The current task will have the specified scheduler.
         *
         * @param   scheduleIterator  the scheduler iterator
         *
         * @return  the current task builder instance
         */
        public TaskBuilder withScheduler(final ScheduleIterator scheduleIterator) {
            this.taskType.setScheduleIterator(scheduleIterator);
            return this;
        }

        /**
         * The current task will have the specified property.
         *
         * @param   key    property key
         * @param   value  property value
         *
         * @return  the current task builder instance
         */
        public TaskBuilder addProperty(final Object key, final Object value) {
            this.taskType.getProperties().put(key, value);
            return this;
        }
    }

    /**
     * Task scheduler builder.
     *
     * @author   ribeirux
     * @version  $Revision$
     */
    public static class TaskSchedulerBuilder extends JAlphaNodeConfigBuilder {

        protected TaskSchedulerBuilder(final JAlphaNodeType config) {
            super(config);
        }

        /**
         * The task scheduler will have the specified pool size.
         *
         * @param   size  pool size
         *
         * @return  the task scheduler builder instance
         */
        public TaskSchedulerBuilder withPoolSize(final Integer size) {
            this.getConfig().getTaskScheduler().setPoolSize(size);
            return this;
        }

        /**
         * The task scheduler will have the specified property.
         *
         * @param   key    property key
         * @param   value  property value
         *
         * @return  the task scheduler builder instance
         */
        public TaskSchedulerBuilder addProperty(final Object key, final Object value) {
            this.getConfig().getTaskScheduler().getProperties().put(key, value);
            return this;
        }
    }

    /**
     * Async executor builder.
     *
     * @author   ribeirux
     * @version  $Revision$
     */
    public static class AsyncNotificationExecutorBuilder extends JAlphaNodeConfigBuilder {

        protected AsyncNotificationExecutorBuilder(final JAlphaNodeType config) {
            super(config);
        }

        /**
         * The async executor will have the specified pool size.
         *
         * @param   size  pool size
         *
         * @return  the async executor builder instance
         */
        public AsyncNotificationExecutorBuilder withPoolSize(final Integer size) {
            this.getConfig().getAsyncNotificationExecutor().setPoolSize(size);
            return this;
        }

        /**
         * The async executor will have the specified property.
         *
         * @param   key    property key
         * @param   value  property value
         *
         * @return  the async executor builder instance
         */
        public AsyncNotificationExecutorBuilder addProperty(final Object key, final Object value) {
            this.getConfig().getAsyncNotificationExecutor().getProperties().put(key, value);
            return this;
        }
    }

    /**
     * Membership builder.
     *
     * @author   ribeirux
     * @version  $Revision$
     */
    public static class MembershipBuilder extends JAlphaNodeConfigBuilder {

        protected MembershipBuilder(final JAlphaNodeType config) {
            super(config);
        }

        /**
         * The current node will have the specified name in the group.
         *
         * @param   nodeName  node name
         *
         * @return  the membership builder instance
         */
        public MembershipBuilder withNodeName(final String nodeName) {
            this.getConfig().getMembership().setNodeName(nodeName);
            return this;
        }

        /**
         * This node will connect to the specified cluster name.
         *
         * @param   clusterName  cluster name
         *
         * @return  the membership builder instance
         */
        public MembershipBuilder withClusterName(final String clusterName) {
            this.getConfig().getMembership().setClusterName(clusterName);
            return this;
        }

        /**
         * The group membership will have the specified property.
         *
         * @param   key    property key
         * @param   value  property value
         *
         * @return  the group membership builder instance
         */
        public MembershipBuilder addProperty(final Object key, final Object value) {
            this.getConfig().getMembership().getProperties().put(key, value);
            return this;
        }
    }
}
