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
 * $Id: ListenerMethodException.java 274 2012-07-01 23:04:24Z ribeirux@gmail.com $
 *******************************************************************************/
package org.jalphanode.jmx.mbean;

import org.jalphanode.jmx.annotation.MBean;
import org.jalphanode.jmx.annotation.ManagedAttribute;
import org.jalphanode.jmx.annotation.ManagedOperation;
import org.jalphanode.jmx.annotation.ManagedParameter;

@MBean
public class MBean6 {

    private String field;

    private int operationExecutionCount;

    @ManagedAttribute
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
        StringBuilder builder = new StringBuilder();
        builder.append("AnnotatedMBean [field=");
        builder.append(field);
        builder.append(", operationExecutionCount=");
        builder.append(operationExecutionCount);
        builder.append(']');
        return builder.toString();
    }

}
