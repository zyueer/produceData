package com.socket.TCP;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //一定要 先开服务器。
    public static void main(String[] args) throws IOException {
        // 1.创建服务器 对象
        ServerSocket ss = new ServerSocket(12306);
        //2.监听客户端连接，返回一个对应的Socket对象。
        Socket s = ss.accept();
        //3.获取输入流 并显示出来
        InputStream is = s.getInputStream();
        byte[] bys = new byte[1024];
        int len = is.read(bys);
        String str = new String(bys,0,len);
        System.out.println("ip "+s.getInetAddress().getHostAddress()+"传输内容 "+str);
        //ip 10.11.20.36传输内容 hello,tcp,I come
        //4.释放资源
        s.close();
        //ss.close();这个不要关闭
    }
}
