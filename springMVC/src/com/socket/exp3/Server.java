package com.socket.exp3;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(12306);

        Socket s = ss.accept();
        BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("mn.jpg"));

        byte[] bytes = new byte[1024];
        int len = 0;
        while((len = bis.read(bytes)) != -1){
            bos.write(bytes,0,len);
            bos.flush();
        }

        OutputStream os = s.getOutputStream();
        os.write("图片上传成功".getBytes());
        bos.close();
        s.close();

    }
}
