package com.socket.exp2;

import java.io.*;
import java.net.Socket;

// 客户端 文本读取，服务器  控制台输出
public class Client {

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("10.11.20.36",12306);

        BufferedReader br = new BufferedReader(new FileReader("exp1.txt"));

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

        String line = null;
        while((line = br.readLine()) != null){
            bw.write(line);
            bw.newLine();
            bw.flush();
        }

        //  shutdownOutput()提供终止，通知服务器别等了。
        s.shutdownOutput();
        //接受反馈信息
        BufferedReader brClient = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String client = brClient.readLine();
        System.out.println(client);

        br.close();
        s.close();
    }

}
