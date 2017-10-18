package com.martin.packagescanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.martin.packagescanner.scanner.PackageScanner;
import com.martin.packagescanner.util.StringUtil;

/**
 * This scanner is used to find out all classes in a package.
 * Created by ctf on 2017-10-18.
 */
public class ClasspathPackageScanner implements PackageScanner {

    private Logger logger = LoggerFactory.getLogger(ClasspathPackageScanner.class);

    private ClassLoader classLoader;

    private List<String> includeFilter = new LinkedList<>();

    private List<String> excludeFilter = new LinkedList<>();

    /**
     * Construct an instance and specify the base package it should scan.
     */
    public ClasspathPackageScanner() {
        this.classLoader = getClass().getClassLoader();
        resetFilter(true);
    }

    /**
     * Construct an instance with base package and class loader.
     * @param classLoader Use this class load to locate the package.
     */
    public ClasspathPackageScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
        resetFilter(true);
    }

    /**
     * Add an include regex to the inclusion filter list
     * @param regex <br>
     */
    public void addIncludeFilter(String regex) {
        includeFilter.add(regex);
    }

    /**
     * Add an exclude regex to the exclusion filter list
     * @param regex <br>
     */
    public void addExcludeFilter(String regex) {
        excludeFilter.add(regex);
    }

    /**
     * Reset the configured filters.
     */
    public void resetFilter() {
        resetFilter(false);
    }
    /**
     * Reset the configured filters.
     * @param useDefaultFilter whether to re-register the default filter.
     */
    public void resetFilter(boolean useDefaultFilter) {
        includeFilter.clear();
        excludeFilter.clear();
        if (useDefaultFilter) {
            registerDefaultFilter();
        }
    }

    private void registerDefaultFilter() {
        includeFilter.add(".*");
    }

    @Override
    public List<String> scan(String... basePackages) throws IOException {
        if (basePackages == null || basePackages.length == 0) {
            logger.debug("basePackages is empty.");
            return null;
        }

        List<String> classNames = new ArrayList<>();
        for (String basePackage : basePackages) {
            logger.debug("begin scan package[{}]", basePackage);
            classNames.addAll(doScan(basePackage));
        }
        return classNames;
    }

    /**
     * Actually perform the scanning procedure.
     *
     * @param basePackage The base package to scan.
     * @return A list of fully qualified names.
     *
     * @throws IOException <br>
     */
    private List<String> doScan(String basePackage) throws IOException {
        List<String> classNames = new ArrayList<>();

        if (StringUtil.isEmpty(basePackage)) {
            logger.debug("basePackages is empty.");
            return classNames;
        }

        // replace dots with splashes
        String splashPath = StringUtil.dotToSplash(basePackage);

        // get file path
        URL url = classLoader.getResource(splashPath);
        if (url == null) {
            logger.debug("url is null, basePackage[{}] maybe is error", basePackage);
            return classNames;
        }
        String filePath = StringUtil.getRootPath(url);

        // Get classes in that package.
        // If the web server unzips the jar file, then the classes will exist in the form of
        // normal file in the directory.
        // If the web server does not unzip the jar file, then classes will exist in jar file.
        List<String> names; // contains the name of the class file. e.g., Apple.class will be stored as "Apple"
        if (isJarFile(filePath)) {
            // jar file
            names = readFromJarFile(filePath, splashPath);
        } else {
            // directory
            names = readFromDirectory(filePath, splashPath);
        }

        for (String name : names) {
            if (isClassFile(name)) {
                name = StringUtil.trimExtension(name);
                name = StringUtil.splashToDot(name);
                if (isMatch(name)) {
                    classNames.add(name);
                }
            }
        }

        return classNames;
    }

    private List<String> readFromJarFile(String jarPath, String splashedPackageName) throws IOException {
        JarInputStream jarIn = new JarInputStream(new FileInputStream(jarPath));
        JarEntry entry = jarIn.getNextJarEntry();

        List<String> nameList = new ArrayList<>();
        while (null != entry) {
            String name = entry.getName();
            if (name.startsWith(splashedPackageName) && isClassFile(name)) {
                nameList.add(name);
            }

            entry = jarIn.getNextJarEntry();
        }

        return nameList;
    }

    private List<String> readFromDirectory(String filePath, String splashedPackageName) {
        List<String> nameList = new ArrayList<>();

        File directory = new File(filePath);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    List<String> subDirectoryList = readFromDirectory(filePath + "/" + file.getName(),
                            splashedPackageName + "/" + file.getName());
                    if (subDirectoryList != null) {
                        nameList.addAll(subDirectoryList);
                    }
                } else if (isClassFile(file.getName())) {
                    nameList.add(splashedPackageName + "/" + file.getName());
                }
            }
        }

        return nameList;
    }

    private boolean isMatch(String input) {
        for (String regex : excludeFilter) {
            if (Pattern.matches(regex, input)) {
                return false;
            }
        }
        for (String regex : includeFilter) {
            if (Pattern.matches(regex, input)) {
                return true;
            }
        }
        return includeFilter.isEmpty(); // if inclusion filter is empty, that mean matched.
    }

    private boolean isClassFile(String name) {
        return name.endsWith(".class");
    }

    private boolean isJarFile(String name) {
        return name.endsWith(".jar");
    }


}