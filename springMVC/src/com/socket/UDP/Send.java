package com.socket.UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class Send {
    public static void main(String[] args){
        //error!   InetAddress s  = new InetAddress();
        /*try {
            InetAddress inetAddress = InetAddress.getByName("LT-0173-PC.corp.bfd.com");//参数可以是主机名/IP地址。
            //InetAddress inetAddress = InetAddress.getByName("10.12.13.110");
            String name = inetAddress.getHostName();
            String ip = inetAddress.getHostAddress();
            System.out.println("name:"+name+"; ip:"+ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/
        try {
            //1.创建发送端 socket对象
            DatagramSocket datagramSocket = new DatagramSocket();
            //2.创建传输数据，并数据打包
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            while((line = br.readLine()) != null){
                if("886".equals(line)){
                    break;
                }
                //byte[] bytes = "hello world".getBytes();
                byte[] bytes = line.getBytes();
                //打包
                DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length,InetAddress.getByName("10.11.20.36"),10086);
                //3.发送端 发送数据
                datagramSocket.send(datagramPacket);
            }
            //4.关闭
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
