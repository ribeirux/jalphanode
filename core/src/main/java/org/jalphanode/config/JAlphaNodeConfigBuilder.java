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

import com.google.common.base.Preconditions;
import org.jalphanode.scheduler.ScheduleIterator;
import org.jalphanode.task.Task;
import org.jalphanode.util.ConfigurationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * JAlphanode configuration builder.
 *
 * @author   ribeirux
 * @version  $Revision$
 */
public class JAlphaNodeConfigBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(JAlphaNodeConfigBuilder.class);

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
     * <p>Both configuration file and schema file are looked up in following order:
     *
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

        InputStream schemaIS = ConfigurationUtils.findInputStream(ResourceKeys.SCHEMA_LOCATION.getValue());

        try {
            return ConfigurationUtils.unmarshall(configStream, schemaIS, JAlphaNodeType.class);
        } finally {
            try {
                schemaIS.close();
            } catch (IOException e) {
                LOG.error("Could not close input stream of schema: {}", ResourceKeys.SCHEMA_LOCATION.getValue(), e);
            }
        }
    }

    /**
     * Factory method to create an instance of jalphanode configuration. If users want to verify configuration file
     * correctness against schema then appropriate schema file name should be provided as well.
     *
     * <p>Both configuration file and schema file are looked up in following order:
     *
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
            if (configIS != null) {
                try {
                    configIS.close();
                } catch (IOException e) {
                    LOG.error("Could not close input stream of config file: {}", configFileName, e);
                }
            }

            if (schemaIS != null) {
                try {
                    schemaIS.close();
                } catch (IOException e) {
                    LOG.error("Could not close input stream of schema: {}", ResourceKeys.SCHEMA_LOCATION.getValue(), e);
                }
            }
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
