package com.demo.java.basic.loader;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class<?> helloClass = classLoader.loadClass("Hello");
 * Object hello = helloClass.newInstance();
 */
public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) {
        byte[] bytes = loadClassData(name);
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] loadClassData(String name) {
        try {
            String classPath = String.format("%s.class", name);
            FileInputStream inputStream = new FileInputStream(classPath);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int ch;
            while ((ch = inputStream.read()) != -1) {
                outputStream.write(ch);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}