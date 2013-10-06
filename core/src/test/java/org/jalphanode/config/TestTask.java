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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;

import org.jalphanode.task.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dummy task.
 *
 * @author   ribeirux
 * @version  $Revision: 149 $
 */
public final class TestTask implements Task {

    private static final Logger LOG = LoggerFactory.getLogger(TestTask.class);

    @Override
    public void onTimeout(final TaskConfig config) {
        final Date currentTime = new Date();
        final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss SSS");

        LOG.warn("Dummy task: {0} -> {1}", config.getTaskName(), dateFormat.format(currentTime));
    }

}
