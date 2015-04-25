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

import org.jalphanode.jmx.mbean.MBean1;
import org.jalphanode.jmx.mbean.MBean2;
import org.jalphanode.jmx.mbean.MBean3;
import org.jalphanode.jmx.mbean.MBean4;
import org.jalphanode.jmx.mbean.MBean5;
import org.jalphanode.jmx.mbean.MBean6;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.MBeanOperationInfo;

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
            .withReadMethod(testClass.getMethod("getField")) //
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
            .withReadMethod(testClass.getMethod("getField"))                   //
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
            .withReadMethod(testClass.getMethod("getField")).build());

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
                .withReadMethod(testClass.getMethod("getField")) //
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
