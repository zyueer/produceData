package com.IO;

import java.io.*;

public class expFileInputStream{
    public static void main(String[] args) throws IOException {
//        InputStream f = new FileInputStream("E:\\produceData\\springMVC\\src\\com\\IO\\ReadMe");
//        byte[] bytes = new byte[1024];
//        int len =  f.read(bytes);
//        String str = new String(bytes,0,len);
//        System.out.println(str);


//        try {
//            byte bWrite[] = { 97, 98, 99, 100, 65 };
//            OutputStream os = new FileOutputStream("test.txt");
//            for (int x = 0; x < bWrite.length; x++) {
//                os.write(bWrite[x]); // writes the bytes
//            }
//            os.close();
//
//            InputStream is = new FileInputStream("test.txt");
//            int size = is.available();
//
//            for (int i = 0; i < size; i++) {
//                System.out.print((char) is.read() + "  ");
//            }
//            is.close();
//        } catch (IOException e) {
//            System.out.print("Exception");
//        }


        // 第1步：使用File类找到一个文件
        File f = new File("test.txt");// 声明File 对象
        // 第2步：通过子类实例化父类对象
        Writer out = null;
// 准备好一个输出的对象
        out = new FileWriter(f);
// 通过对象多态性进行实例化
        // 第3步：进行写操作
        String str = "Hello World!!!";
// 准备一个字符串
        out.write(str);
// 将内容输出
        // 第4步：关闭输出流
         out.close();
    }
}

