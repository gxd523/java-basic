package com.demo.java.basic.socket.chat;

import java.io.*;
import java.net.Socket;

public class SocketTask implements Runnable {
    private final String socketKey;
    private OnSocketTaskListener onSocketTaskListener;

    public SocketTask(String socketKey, OnSocketTaskListener onSocketTaskListener) {
        this.socketKey = socketKey;
        this.onSocketTaskListener = onSocketTaskListener;
    }

    @Override
    public void run() {
        try {
            Socket socket = onSocketTaskListener.getSocket(socketKey);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null) {
                if ("close".equals(s)) {
                    socket.close();
                    onSocketTaskListener.onSocketClosed(socketKey);
                    break;
                }

                String[] split = s.split("==>");
                if (split.length > 1) {
                    Socket socket1 = onSocketTaskListener.getSocket(split[1]);
                    OutputStream outputStream = socket1.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    String msg = socketKey + "==>" + split[0];
                    printWriter.println(msg);
                    System.out.println("服务器向..." + split[1] + "...发送数据: " + split[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
