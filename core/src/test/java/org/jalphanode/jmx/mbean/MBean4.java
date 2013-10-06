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
package org.jalphanode.jmx.mbean;

import org.jalphanode.jmx.annotation.MBean;
import org.jalphanode.jmx.annotation.ManagedAttribute;
import org.jalphanode.jmx.annotation.ManagedOperation;
import org.jalphanode.jmx.annotation.ManagedParameter;

@MBean
public class MBean4 {

    @ManagedAttribute
    private String field;

    private int operationExecutionCount;

    public void setField(final String field) {
        this.field = field;
    }

    public int getOperationExecutionCount() {
        return operationExecutionCount;
    }

    @ManagedOperation
    public void operation(@ManagedParameter final String param) {
        operationExecutionCount++;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("AnnotatedMBean [field=");
        builder.append(field);
        builder.append(", operationExecutionCount=");
        builder.append(operationExecutionCount);
        builder.append(']');
        return builder.toString();
    }

}
