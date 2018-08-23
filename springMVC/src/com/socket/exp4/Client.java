package com.socket.exp4;

import java.io.*;
import java.net.Socket;

//客户端  服务器  图片的读取和输出-------字节流  BufferedOutputStream
public class Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("10.11.20.36",12306);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("zhang.jpg"));
        BufferedOutputStream bos = new BufferedOutputStream(s.getOutputStream());

        byte[] bys = new byte[1024];
        int len = 0;
        while((len = bis.read(bys)) != -1){
            bos.write(bys,0,len);
            bos.flush();
        }

        s.shutdownOutput();
        InputStream is = s.getInputStream();
        byte[] bytes = new byte[1024];
        int length = is.read(bytes);
        String string = new String(bytes,0,length);
        System.out.println(string);

        bis.close();
        s.close();
    }
}
