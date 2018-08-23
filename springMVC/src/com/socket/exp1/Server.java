package com.socket.exp1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //服务器  控制台输出、文本文件输出
    public static void main(String[] args) throws IOException {
//        //服务器  控制台输出
//        ServerSocket ss = new ServerSocket(12306);
//
//        Socket s = ss.accept();
//        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
////        此处是字节流的输出。
////        Byte[] bys = new Byte[1024];
////        int len = br.read(bys);
////        String str = new String(bys,0,len);
//        String line = null;
//        while((line = br.readLine()) != null){
//            System.out.println(line);
//        }
//        s.close();


//        //服务器  文本文件输出
        ServerSocket ss = new ServerSocket(12306);
        Socket s = ss.accept();

        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

        BufferedWriter bw = new BufferedWriter(new FileWriter("exp1.txt"));

        String line = null;
        while((line = br.readLine()) != null){
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        bw.close();
        s.close();
    }
}
