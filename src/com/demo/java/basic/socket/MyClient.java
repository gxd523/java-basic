package com.demo.java.basic.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyClient {
    public static void main(String[] args) {
        System.out.println("客户端启动....");
        Socket socket = null;
        try {
            socket = new Socket("localhost", MyServer.PORT);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            new Thread(() -> {// 接收服务端的数据
                try {
                    byte[] bytes1 = new byte[1024];
                    int length1;
                    while ((length1 = inputStream.read(bytes1)) != -1) {
                        System.out.println("客户端接收到-->" + new String(bytes1, 0, length1).trim());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            byte[] bytes = new byte[1024];
            int length;
            while ((length = System.in.read(bytes)) != -1) {// 控制台输入数据发送到服务端
                outputStream.write(bytes, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
