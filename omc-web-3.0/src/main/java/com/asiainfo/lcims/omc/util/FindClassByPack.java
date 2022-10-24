package com.asiainfo.lcims.omc.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取指定package下的所有class
 * 
 * @author qinwoli
 * 
 */
public class FindClassByPack {
    private static final Logger LOG = LoggerFactory.getLogger(FindClassByPack.class);

    private FindClassByPack() {

    }

    public static List<Class<?>> addClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        try {
            String packageDirName = packageName.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader()
                    .getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("jar".equals(protocol)) {
                    findClassInJar(packageDirName, classes, url);
                } else if ("file".equals(protocol)) {
                    String packagePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findClassInFile(packageName, classes, packagePath);
                }
            }
            return classes;
        } catch (Exception e) {
            LOG.error("读取指定package下的class出错", e);
            return classes;
        }
    }

    /**
     * 从文件中读取相应class
     * 
     * @param packageName
     * @param classes
     * @param packagePath
     * @throws ClassNotFoundException
     */
    private static void findClassInFile(String packageName, List<Class<?>> classes,
            String packagePath) throws ClassNotFoundException {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles();
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findClassInFile(packageName + "." + file.getName(), classes, file.getAbsolutePath());
            } else if (file.getName().indexOf('$') == -1) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(packageName + '.' + className));
            }
        }
    }

    /**
     * 从jar包中读取相应class
     * 
     * @param packageDirName
     * @param classes
     * @param url
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void findClassInJar(String packageDirName, List<Class<?>> classes, URL url)
            throws IOException, ClassNotFoundException {
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if (!entry.isDirectory() && name.startsWith(packageDirName) && name.endsWith(".class")
                    && name.indexOf('$') == -1) {
                name = name.replace('/', '.');
                classes.add(Class.forName(name.substring(0, name.length() - 6)));
            }
        }
    }
}
