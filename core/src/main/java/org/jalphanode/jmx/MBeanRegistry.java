package org.jalphanode.jmx;

import javax.management.JMException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

/**
 * Unified interface for registering/unregistering MBeans.
 */
public interface MBeanRegistry {

    /**
     * Registers a new MBean.
     *
     * @param  instance    instance to register
     * @param  objectName  name
     */
    ObjectInstance register(Object instance, String objectName) throws JMException;

    /**
     * Unregisters the MBean.
     *
     * @param  name  the object name of the MBean to be unregistered.
     */
    void unregister(final ObjectName name);

    /**
     * Unregister all currently registered MBeans.
     */
    void unregisterAll();

}
