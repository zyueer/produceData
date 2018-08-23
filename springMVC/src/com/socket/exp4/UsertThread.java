package com.socket.exp4;

import java.io.*;
import java.net.Socket;

public class UsertThread implements Runnable{
    private Socket s;
    public UsertThread(Socket s){
        this.s = s;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String newName = System.currentTimeMillis()+".txt";
            BufferedWriter bw = new BufferedWriter(new FileWriter(newName));

            String line = null;
            while((line = br.readLine()) != null){
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
            //给出反馈 略
            bw.close();
            s.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
