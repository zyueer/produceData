package com.socket.UDP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class ChartRoom {
    //使用多线程，实现同一个窗口的发送和接收。
    public static void main(String[] args) throws SocketException, InterruptedException {
        DatagramSocket send = new DatagramSocket();
        DatagramSocket reveive = new DatagramSocket(10086);


        SendThread st = new SendThread(send);
        ReceiveThread rt = new ReceiveThread(reveive);

        Thread t1 = new Thread(st);
        Thread t2 = new Thread(rt);

        t1.start();
//        Thread.sleep(10000);
        t2.start();

    }
}
class  SendThread implements Runnable{
    private DatagramSocket ds;

    public SendThread(DatagramSocket ds){
        this.ds = ds;
    }

    @Override
    public void run() {
        try{
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
                ds.send(datagramPacket);
            }
            //4.关闭
            ds.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class  ReceiveThread implements Runnable{
    private DatagramSocket ds;

    public ReceiveThread(DatagramSocket ds){
        this.ds = ds;
    }

    @Override
    public void run() {
        try{
            while(true){
                //创建一个数据包，用来接收数据
                byte[] bytes = new byte[1024];
                int length = bytes.length;
                DatagramPacket datagramPacket = new DatagramPacket(bytes,bytes.length);
                //接收数据
                ds.receive(datagramPacket);
                //解析数据
                System.out.println("ip "+datagramPacket.getAddress().getHostAddress());
                String str = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                System.out.println("接收内容 "+str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}