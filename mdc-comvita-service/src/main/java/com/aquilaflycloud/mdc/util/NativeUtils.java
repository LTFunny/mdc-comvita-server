package com.aquilaflycloud.mdc.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeUtils {

    private static final String NATIVE_FOLDER_PATH_PREFIX = "nativeUtils_";

    private static File temporaryDir;

    /**
     * @param is       要加载动态库的输入流
     * @param filename 动态库文件名
     * @throws IOException           动态库读写错误
     * @throws FileNotFoundException 没有在jar包中找到指定的文件
     */
    public static synchronized void loadLibrary(InputStream is, String filename) throws IOException {
        // 创建临时文件夹
        if (temporaryDir == null) {
            temporaryDir = Files.createTempDirectory(NATIVE_FOLDER_PATH_PREFIX + System.nanoTime()).toFile();
            temporaryDir.deleteOnExit();
        }
        // 临时文件夹下的动态库名
        File temp = new File(temporaryDir, filename);
        Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        try {
            // 加载临时文件夹中的动态库
            System.load(temp.getAbsolutePath());
        } finally {
            // 设置在JVM结束时删除临时文件
            temp.deleteOnExit();
        }
    }

    public static synchronized String getTempFromJar(String path, Class<?> loadClass) throws IOException {
        if (null == path || !path.startsWith("/")) {
            throw new IllegalArgumentException("The path has to be absolute (start with '/').");
        }
        // Obtain filename from path
        String[] parts = path.split("/");
        String filename = (parts.length > 1) ? parts[parts.length - 1] : null;

        // Check if the filename is okay
        if (filename == null) {
            throw new IllegalArgumentException("The filename has to be not null.");
        }

        // 创建临时文件夹
        if (temporaryDir == null) {
            temporaryDir = Files.createTempDirectory(NATIVE_FOLDER_PATH_PREFIX + System.nanoTime()).toFile();
            temporaryDir.deleteOnExit();
        }
        // 临时文件夹下的动态库名
        File temp = new File(temporaryDir, filename);
        Class<?> clazz = loadClass == null ? NativeUtils.class : loadClass;
        // 从jar包中复制文件到系统临时文件夹
        try (InputStream is = clazz.getResourceAsStream(path)) {
            Files.copy(is, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            temp.delete();
            throw e;
        } catch (NullPointerException e) {
            temp.delete();
            throw new FileNotFoundException("File " + path + " was not found inside JAR.");
        }
        // 返回临时文件路径
        try {
            return temp.getPath();
        } finally {
            // 设置在JVM结束时删除临时文件
            temp.deleteOnExit();
        }
    }
}