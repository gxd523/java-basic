package com.demo.java.basic.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private Map<String, Socket> socketMap = new HashMap<>();

    public ChatServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            ExecutorService executorService = Executors.newCachedThreadPool();

            while (true) {
                Socket socket = serverSocket.accept();
                String key = String.valueOf(System.currentTimeMillis());
                socketMap.put(key, socket);
                System.out.println(key + "..." + "已接入服务器");

                updateClientList(socketMap.values());

                executorService.submit(new ChatServer.SocketTask(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateClientList(Collection<Socket> socketCollection) throws IOException {
        for (Socket socket : socketCollection) {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(socketMap.keySet().toString().getBytes());
            outputStream.flush();
            System.out.println("更新客户端列表-->" + socketMap.keySet().toString());
        }
    }

    public static void main(String[] args) {
        new ChatServer();
    }

    private class SocketTask implements Runnable {
        private String socketKey;

        public SocketTask(String socketKey) {
            this.socketKey = socketKey;
        }

        public void run() {
            Socket socket = ChatServer.this.socketMap.get(socketKey);

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    System.out.println("服务器收到信息-->" + s);
                    if ("close".equals(s)) {
                        socket.close();
                        ChatServer.this.socketMap.remove(socketKey);

                        updateClientList(socketMap.values());
                        break;
                    }

                    String[] split = s.split("==>");
                    if (split.length > 1) {
                        Socket socket1 = ChatServer.this.socketMap.get(split[1]);
                        OutputStream outputStream = socket1.getOutputStream();
                        String s1 = socketKey + "==>" + split[0];
                        outputStream.write(s1.getBytes());
                        outputStream.flush();
                        System.out.println("服务器向..." + split[1] + "...发送数据:" + split[0]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
