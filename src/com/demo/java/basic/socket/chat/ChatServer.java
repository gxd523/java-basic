package com.demo.java.basic.socket.chat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer implements OnSocketTaskListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private Map<String, Socket> socketMap = new HashMap<>();

    public ChatServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            ExecutorService threadPool = Executors.newCachedThreadPool();

            while (true) {
                Socket socket = serverSocket.accept();
                String key = String.valueOf(System.currentTimeMillis());
                socketMap.put(key, socket);

                updateClientList(socketMap.values());

                threadPool.submit(new SocketTask(key, this));
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

    @Override
    public Socket getSocket(String socketKey) {
        return socketMap.get(socketKey);
    }

    @Override
    public void onSocketClosed(String socketKey) {
        this.socketMap.remove(socketKey);
        try {
            updateClientList(socketMap.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
