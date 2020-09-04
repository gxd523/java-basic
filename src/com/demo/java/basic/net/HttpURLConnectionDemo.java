package com.demo.java.basic.net;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class HttpURLConnectionDemo {
    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.submit(get());
//        executor.submit(post());
//        executor.submit(uploadFile());
//        executor.submit(uploadMultiFile());
//        executor.submit(downloadFile());
    }

    public static Runnable get() {
        return () -> {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL("http://zmapi.dangbei.net/time.php?timestamp=" + System.currentTimeMillis());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                // 是否开启输入流，默认true
                httpURLConnection.setDoInput(true);
                // 是否开启输出流，默认false
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } else {
                    stringBuilder.append(httpURLConnection.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                stringBuilder.append(e.getMessage());
            }
            System.out.printf("Get请求返回数据-->%s\n", stringBuilder.toString());
        };
    }

    public static Runnable post() {
        return () -> {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL("http://zmapi.dangbei.net/time.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset-utf-8");
                httpURLConnection.connect();

                String body = String.format("{\"data\":{\"timestamp\":%s},\"code\":0,\"message\":\"success\"}", System.currentTimeMillis());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), StandardCharsets.UTF_8));
                writer.write(body);
                writer.close();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } else {
                    stringBuilder.append(httpURLConnection.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                stringBuilder.append(e.getMessage());
            }
            System.out.printf("Get请求返回数据-->%s\n", stringBuilder.toString());
        };
    }

    public static Runnable uploadFile() {
        return () -> {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL("http://zmapi.dangbei.net/time.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestProperty("Content-Type", "file/*");
                httpURLConnection.connect();

                File file = new File("b.txt");
                if (!file.exists()) {
                    System.out.println("文件不存在");
                    return;
                }
                FileInputStream inputStream = new FileInputStream(file);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                byte[] bytes = new byte[1024];
                int length;
                while ((length = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, length);
                }
                inputStream.close();
                outputStream.close();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } else {
                    stringBuilder.append(httpURLConnection.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
                stringBuilder.append(e.getMessage());
            }
            System.out.printf("打印结果...%s\n", stringBuilder.toString());
        };
    }

    private static Runnable uploadMultiFile() {
        return () -> {
        };
    }

    public static Runnable downloadFile() {
        return () -> {
            try {
                URL url = new URL("http://down.znds.com/getdownurl/?s=ZG93bi8yMDIwMDcxMS9kYnptXzMuMy4xZGFuZ2JlaS5hcGs=");
                final HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int currentLength = 0;
                    InputStream inputStream = httpURLConnection.getInputStream();
                    File apkFile = new File("zzz.apk");
                    FileOutputStream outputStream = new FileOutputStream(apkFile);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                        currentLength += length;

                        final int finalCurrentLength = currentLength;
                        int downloadPercent = finalCurrentLength * 100 / httpURLConnection.getContentLength();
                        System.out.printf("已下载...%s%%\n", downloadPercent);
                    }
                    inputStream.close();
                    outputStream.close();

                    if (apkFile.exists()) {
                        System.out.println("文件下载成功!");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}