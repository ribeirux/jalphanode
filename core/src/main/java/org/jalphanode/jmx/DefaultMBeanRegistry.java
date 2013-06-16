package org.jalphanode.jmx;

import java.lang.management.ManagementFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * By default all MBeans are registered in platform MBean server.
 *
 * @author  pribeiro
 */
public class DefaultMBeanRegistry implements MBeanRegistry {

    private static final Log LOG = LogFactory.getLog(DefaultMBeanRegistry.class);

    public static final String DOMAIN = "org.jalphanode";
    public static final String COMPONENT_KEY = "component";
    public static final String SCHEDULER_JMX_GROUP = "type=Scheduler";

    private final Set<ObjectName> registeredBeans = Sets.newSetFromMap(new ConcurrentHashMap<ObjectName, Boolean>());

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
            LOG.warn("Could not revolve object name for class: " + instance.getClass() + " with name: " + objectName,
                e);
        }

    }

    @Override
    public void unregister(final ObjectName name) {
        Preconditions.checkNotNull(name, "name");

        try {
            mBeanServer.unregisterMBean(name);
        } catch (JMException e) {
            LOG.warn("Could not unregister MBean: " + name, e);
        }

        registeredBeans.remove(name);
    }

    @Override
    public void unregisterAll() {
        for (ObjectName name : registeredBeans) {
            unregister(name);
        }

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
