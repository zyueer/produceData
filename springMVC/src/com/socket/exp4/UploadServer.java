package com.socket.exp4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class UploadServer {

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12306);
        while(true){
            Socket s = ss.accept();
            new Thread(new UsertThread(s)).start();
        }
    }
}
