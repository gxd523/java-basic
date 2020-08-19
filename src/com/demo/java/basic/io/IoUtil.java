package com.demo.java.basic.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IoUtil {
    public static String streamToString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 通过键盘录入文字输出到文件
     */
    public static void printToFile(String dstPath, boolean append) {
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dstPath, append);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            InputStreamReader inputStreamReader = new InputStreamReader(System.in);
            bufferedReader = new BufferedReader(inputStreamReader);

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                bufferedWriter.write(s);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝文件
     */
    public static void copyFile(String srcPath, String dstPath) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(srcPath);
            bufferedInputStream = new BufferedInputStream(fileInputStream);

            FileOutputStream fileOutputStream = new FileOutputStream(dstPath);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            int length;
            byte[] bytes = new byte[1024];
            while ((length = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 序列化
     */
    public static <T> void serialize(T t, File file) throws IOException {
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
        outputStream.writeObject(t);
        outputStream.close();
    }

    /**
     * 反序列化
     * User user = IoUtil.<User>deserialize();
     */
    public static <T> T deserialize(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
        T t = (T) inputStream.readObject();
        inputStream.close();
        return t;
    }
}
