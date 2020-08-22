package com.demo.java.basic.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IoUtil {
    public static String streamToString(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过键盘录入文字输出到文件
     */
    public static void printToFile(String dstPath, boolean append) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(dstPath, append);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ) {
            String s;
            while ((s = bufferedReader.readLine()) != null && !"exit".equals(s)) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件
     */
    public static void copyFile(String srcPath, String dstPath) {
        try (// 使用try-with-resources可以自动关闭流
             BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcPath));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(dstPath))
        ) {
            int length;
            byte[] bytes = new byte[1024];
            while ((length = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化
     */
    public static <T> void serialize(T t, File file) throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(t);
        }
    }

    /**
     * 反序列化
     * User user = IoUtil.<User>deserialize();
     */
    public static <T> T deserialize(File file) throws IOException, ClassNotFoundException {
        T t;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            t = (T) inputStream.readObject();
        }
        return t;
    }
}
