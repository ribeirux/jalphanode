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

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.jalphanode.task.Task;

import org.jalphanode.util.ConfigurationUtils;

import com.google.common.base.Preconditions;

/**
 * Converts a class name into a java Class.
 *
 * @author   ribeirux
 * @version  $Revision: 118 $
 */
public class TaskClassAdapter extends XmlAdapter<String, Task> {

    @Override
    public String marshal(final Task task) throws Exception {
        return Preconditions.checkNotNull(task, "task").getClass().getName();
    }

    @Override
    public Task unmarshal(final String taskClass) throws Exception {
        return ConfigurationUtils.getClass(Preconditions.checkNotNull(taskClass, "taskClass"), Task.class)
                                 .newInstance();
    }
}
