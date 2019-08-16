package com.martin.packagescanner.scanner;

import java.io.IOException;
import java.util.List;

/**
 * This scanner is used to find out all classes in a package.
 *
 * @author chen.tengfei <br>
 * @author zhangjiashuai <br>
 * @version 1.0<br>
 * @CreateDate 2017/10/18 <br>
 */
public interface PackageScanner {

    /**
     * Get all fully qualified names located in the specified package
     * and its sub-package.
     *
     * @param basePackages The base package to scan.
     * @return A list of fully qualified names.
     * @throws IOException <br>
     */
    List<String> scan(String... basePackages) throws IOException;

    /**
     * Get all <code>java.lang.Class</> objects in the specified package
     * and its sub-package.
     *
     * @param basePackages The base package to scan.
     * @return A list of <code>java.lang.Class</> objects
     * @throws IOException <br>
     */
    List<Class> scanForClasses(String... basePackages) throws IOException;
}
