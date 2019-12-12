package com.demo.java.basic.socket.chat;

import java.net.Socket;

public interface OnSocketTaskListener {
    Socket getSocket(String socketKey);

    void onSocketClosed(String socketKey);
}