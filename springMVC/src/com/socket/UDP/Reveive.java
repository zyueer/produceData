package com.socket.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Reveive {
    public static void main(String[] args){
        try {
            //接收端一定要先运行!!!!
            //1.创建接收端 socket对象，一定要指定port
            DatagramSocket datagramSocket = new DatagramSocket(10086);
            while(true){
                //2.创建一个数据包，用来接收数据
                byte[] bytes = new byte[1024];
                int length = bytes.length;
                DatagramPacket datagramPacket = new DatagramPacket(bytes,length);
                //3.发送端 接收数据
                datagramSocket.receive(datagramPacket);//阻塞方式接收，运行接收端后，在没接收到数据之前会一直运行着。
                //4.解析数据包
                System.out.println("ip "+datagramPacket.getAddress().getHostAddress());
                System.out.println("host "+datagramPacket.getAddress().getHostName());
                byte[] bys = datagramPacket.getData();
                int len = datagramPacket.getLength();
                String str = new String(bys,0,len);
                System.out.println("接收内容 "+str);
            }

            //5.关闭
            //datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
