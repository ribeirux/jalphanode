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
package org.jalphanode.jmx;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * By default all MBeans are registered in platform MBean server.
 *
 * @author  pribeiro
 */
public class DefaultMBeanRegistry implements MBeanRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMBeanRegistry.class);

    public static final String DOMAIN = "org.jalphanode";
    public static final String COMPONENT_KEY = "component";
    public static final String SCHEDULER_JMX_GROUP = "type=Scheduler";

    private final Set<ObjectName> registeredBeans = Sets.newSetFromMap(new ConcurrentHashMap<>());

    private final MBeanServer mBeanServer;

    public DefaultMBeanRegistry() {
        this(ManagementFactory.getPlatformMBeanServer());
    }

    public DefaultMBeanRegistry(final MBeanServer mBeanServer) {
        this.mBeanServer = Preconditions.checkNotNull(mBeanServer, "mBeanServer");
    }

    @Override
    public ObjectInstance register(final Object instance, final String objectName) throws JMException {
        Preconditions.checkNotNull(instance, "instance");
        Preconditions.checkNotNull(objectName, "objectName");

        ObjectName name = getObjectName(objectName);
        ObjectInstance objectInstance = mBeanServer.registerMBean(instance, name);
        registeredBeans.add(name);

        return objectInstance;
    }

    public void unregister(final Object instance, final String objectName) {
        Preconditions.checkNotNull(instance, objectName);

        try {
            ObjectName name = getObjectName(objectName);
            unregister(name);
        } catch (JMException e) {
            LOG.warn("Could not revolve object name for class {} with name {}", instance.getClass(), objectName, e);
        }
    }

    @Override
    public void unregister(final ObjectName name) {
        Preconditions.checkNotNull(name, "name");

        try {
            mBeanServer.unregisterMBean(name);
        } catch (JMException e) {
            LOG.warn("Could not unregister MBean: {}", name, e);
        }

        registeredBeans.remove(name);
    }

    @Override
    public void unregisterAll() {
        registeredBeans.forEach(this::unregister);
        registeredBeans.clear();
    }

    protected ObjectName getObjectName(final String resourceName) throws JMException {
        final StringBuilder nameBuilder = new StringBuilder(DOMAIN).append(':');
        nameBuilder.append(SCHEDULER_JMX_GROUP);
        nameBuilder.append(',');
        nameBuilder.append(COMPONENT_KEY);
        nameBuilder.append('=');
        nameBuilder.append(resourceName);

        return new ObjectName(nameBuilder.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DefaultMBeanRegistry [mBeanServer=");
        builder.append(mBeanServer);
        builder.append(']');
        return builder.toString();
    }

}
