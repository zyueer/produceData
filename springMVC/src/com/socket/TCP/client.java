package com.socket.TCP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws IOException {
        //1.常见客户端 对象
        Socket s = new Socket("10.11.20.36",12306);
        //2.获取数据流 写数据
        OutputStream os = s.getOutputStream();
        os.write("hello,tcp,I come".getBytes());
        //3.释放资源
        s.close();
    }
}
