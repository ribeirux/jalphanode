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
package org.jalphanode.jmx;

import javax.management.MBeanOperationInfo;

import org.jalphanode.jmx.mbean.MBean1;
import org.jalphanode.jmx.mbean.MBean2;
import org.jalphanode.jmx.mbean.MBean3;
import org.jalphanode.jmx.mbean.MBean4;
import org.jalphanode.jmx.mbean.MBean5;
import org.jalphanode.jmx.mbean.MBean6;

import org.testng.Assert;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * {@link MBeanAnnotationScanner} tests.
 *
 * @author  pribeiro
 */
public class MBeanAnnotationScannerTest {

    private MBeanAnnotationScanner scanner;

    @BeforeClass
    public void setUp() {
        scanner = new MBeanAnnotationScanner();
    }

    @Test
    public void testMBean1() throws Exception {
        Class<?> testClass = MBean1.class;

        MBeanMetadata.Builder builder = new MBeanMetadata.Builder(testClass);

        builder.putAttribute(new ManagedAttributeMetadata.Builder("field")      //
            .withReadMethod(testClass.getMethod("getField", new Class<?>[] {})) //
            .withWriteMethod(testClass.getMethod("setField", String.class)).build());

        builder.putOperation(new ManagedOperationMetadata.Builder(testClass.getMethod("operation", String.class))
                .addParameterMetadata(new ManagedParameterMetadata.Builder(String.class.getName()).build()).build());

        MBeanMetadata metadata = scanner.scan(testClass);

        Assert.assertEquals(metadata, builder.build());
    }

    @Test
    public void testMBean2() throws Exception {
        Class<?> testClass = MBean2.class;

        MBeanMetadata.Builder builder = new MBeanMetadata.Builder(testClass);
        builder.withObjectName("objectName").withDescription("beanDescription");

        builder.putAttribute(new ManagedAttributeMetadata.Builder("fieldName").withDescription("fieldDescription") //
            .withReadMethod(testClass.getMethod("getField", new Class<?>[] {}))                   //
            .withWriteMethod(testClass.getMethod("setField", String.class)).build());

        builder.putOperation(new ManagedOperationMetadata.Builder(testClass.getMethod("operation", String.class))
                .withName("operationName").withImpact(MBeanOperationInfo.INFO).withDescription("operationDescription")
                .addParameterMetadata(new ManagedParameterMetadata.Builder(String.class.getName()).withName(
                        "paramName").wirhDescription("paramDescription").build()).build());

        MBeanMetadata metadata = scanner.scan(testClass);

        Assert.assertEquals(metadata, builder.build());
    }

    @Test
    public void testMBean3() throws Exception {
        Class<?> testClass = MBean3.class;

        MBeanMetadata.Builder builder = new MBeanMetadata.Builder(testClass);

        builder.putAttribute(new ManagedAttributeMetadata.Builder("field") //
            .withReadMethod(testClass.getMethod("getField", new Class<?>[] {})).build());

        builder.putOperation(new ManagedOperationMetadata.Builder(testClass.getMethod("operation", String.class))
                .addParameterMetadata(new ManagedParameterMetadata.Builder(String.class.getName()).build()).build());

        MBeanMetadata metadata = scanner.scan(testClass);

        Assert.assertEquals(metadata, builder.build());
    }

    @Test
    public void testMBean4() throws Exception {
        Class<?> testClass = MBean4.class;

        MBeanMetadata.Builder builder = new MBeanMetadata.Builder(testClass);

        builder.putAttribute(new ManagedAttributeMetadata.Builder("field") //
            .withWriteMethod(testClass.getMethod("setField", String.class)).build());

        builder.putOperation(new ManagedOperationMetadata.Builder(testClass.getMethod("operation", String.class))
                .addParameterMetadata(new ManagedParameterMetadata.Builder(String.class.getName()).build()).build());

        MBeanMetadata metadata = scanner.scan(testClass);

        Assert.assertEquals(metadata, builder.build());
    }

    @Test
    public void testMBean5() throws Exception {
        Class<?> testClass = MBean5.class;

        MBeanMetadata.Builder builder = new MBeanMetadata.Builder(testClass);

        builder.putAttribute(new ManagedAttributeMetadata.Builder("fieldName").withDescription("fieldDescription")
                .withReadMethod(testClass.getMethod("getField", new Class<?>[] {})) //
            .withWriteMethod(testClass.getMethod("setField", String.class)).build());

        MBeanMetadata metadata = scanner.scan(testClass);

        Assert.assertEquals(metadata, builder.build());
    }

    @Test
    public void testMBean6() throws Exception {
        Class<?> testClass = MBean6.class;

        MBeanMetadata.Builder builder = new MBeanMetadata.Builder(testClass);

        builder.putAttribute(new ManagedAttributeMetadata.Builder("field") //
            .withWriteMethod(testClass.getMethod("setField", String.class)).build());

        builder.putOperation(new ManagedOperationMetadata.Builder(testClass.getMethod("operation", String.class))
                .addParameterMetadata(new ManagedParameterMetadata.Builder(String.class.getName()).build()).build());

        MBeanMetadata metadata = scanner.scan(testClass);

        Assert.assertEquals(metadata, builder.build());
    }

}
