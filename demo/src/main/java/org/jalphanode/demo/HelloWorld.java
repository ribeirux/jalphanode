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
package org.jalphanode.demo;

import org.jalphanode.config.TaskConfig;

import org.jalphanode.task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(HelloWorld.class);

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
