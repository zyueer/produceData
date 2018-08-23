package com.socket.exp1;

import java.io.*;
import java.net.Socket;

// 客户端 键盘录入，服务器  控制台输出
public class Client {

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("10.11.20.36",12306);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

        String line = null;
        while((line = br.readLine()) != null){
            if("end".equals(line)) break;
            bw.write(line);
            bw.newLine();
            bw.flush();
        }
        s.close();
    }

}
