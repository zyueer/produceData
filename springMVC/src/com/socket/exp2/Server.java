package com.socket.exp2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 客户端 文本读取，服务器  控制台输出
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

        BufferedWriter bw = new BufferedWriter(new FileWriter("exp2.txt"));

        String line = null;
        while((line = br.readLine()) != null){
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        //对 shutdownOutput给出反馈
        BufferedWriter bwServer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        bwServer.write("文件上传成功");
        bwServer.newLine();
        bwServer.flush();

        bw.close();
        s.close();
    }
}
