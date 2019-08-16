package com.martin.packagescanner;

import java.util.List;

import com.martin.packagescanner.service.BaseService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.martin.packagescanner.scanner.PackageScanner;

/** 
* ClasspathPackageScanner Tester. 
* 
* @author chen.tengfei
* @since <pre>10/18/2017</pre> 
* @version 1.0 
*/ 
public class ClasspathPackageScannerTest { 

    private static Logger logger = LoggerFactory.getLogger(ClasspathPackageScannerTest.class);

    /**
    * 
    * Method: {@link ClasspathPackageScanner#scan(String...)}
    * 
    */ 
    @Test
    public void testScan() throws Exception {
        ClasspathPackageScanner packageScanner = new ClasspathPackageScanner();
        packageScanner.resetFilter();
        packageScanner.addIncludeFilter(".*(xml).*");
        packageScanner.addExcludeFilter(".*(DOMConfigurator).*");
        List<String> classNames = packageScanner.scan("org.apache.log4j");

        Assert.assertNotNull(classNames);
        Assert.assertTrue(classNames.size() > 0);

        for (String name : classNames) {
            logger.info("find class [{}]", name);
        }
    }

    @Test
    public void testScanForClasses() throws Exception {
        ClasspathPackageScanner packageScanner = new ClasspathPackageScanner();
        List<Class> classes = packageScanner.scanForClasses("com.martin.packagescanner.service.impl");

        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.size() > 0);

        for (Class clazz : classes) {
            logger.info("find class [{}] is implement of BaseService [{}]", clazz, BaseService.class.isAssignableFrom(clazz));
        }
    }

    @Test
    public void testBuilder() throws Exception {
        ClasspathPackageScanner packageScanner = ClasspathPackageScanner.newBuilder().addExcludeFilter("^.*OtherServiceImpl$").build();
        List<Class> classes = packageScanner.scanForClasses("com.martin.packagescanner.service.impl");

        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.size() > 0);

        for (Class clazz : classes) {
            logger.info("find class [{}] is implement of BaseService [{}]", clazz, BaseService.class.isAssignableFrom(clazz));
        }
    }

    /**
     *
     * Method: {@link ClasspathPackageScanner#scan(String...)}
     *
     */
    @Test
    public void testScanWithClassLoader() throws Exception {
        PackageScanner packageScanner = new ClasspathPackageScanner(getClass().getClassLoader());
        List<String> classNames = packageScanner.scan("com.martin");

        Assert.assertNotNull(classNames);
        Assert.assertTrue(classNames.size() > 0);

        for (String name : classNames) {
            logger.info("find class [{}]", name);
        }
    }
} 
