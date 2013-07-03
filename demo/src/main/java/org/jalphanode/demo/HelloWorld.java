package org.jalphanode.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jalphanode.config.TaskConfig;

import org.jalphanode.task.Task;

public class HelloWorld implements Task {

    private static final Log LOG = LogFactory.getLog(HelloWorld.class);

    @Override
    public void onTimeout(final TaskConfig config) {
        LOG.info("Hello World");

        // simulate some work
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            LOG.warn("Hello world task interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

}
