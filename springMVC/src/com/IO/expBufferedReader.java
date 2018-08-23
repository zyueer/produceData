package com.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;

public class expBufferedReader {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int s = bufferedReader.read();//显示的是ASCII码
        char c = (char) bufferedReader.read();//显示的是ASCII码
        String str = bufferedReader.readLine();
        System.out.println("字符："+s+"字符："+c+" 字符串："+str);


//        char c;
//        // 使用 System.in 创建 BufferedReader
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("输入字符, 按下 'q' 键退出。");
//        // 读取字符
//        do {
//            c = (char) br.read();
//            System.out.println(c);
//        } while (c != 'q');


//        Scanner scan = new Scanner(System.in);
//        // next方式接收字符串
//        System.out.println("next方式接收：");
//        // 判断是否还有输入
//        if (scan.hasNext()) {
//            String str1 = scan.next();
//            System.out.println("输入的数据为：" + str1);
//        }
//        scan.close();


//        Scanner scan = new Scanner(System.in);
//        double sum = 0;
//        int m = 0;
//        while (scan.hasNextDouble()) {
//            double x = scan.nextDouble();
//            m = m + 1;
//            sum = sum + x;
//        }
//        System.out.println(m + "个数的和为" + sum);
//        System.out.println(m + "个数的平均值是" + (sum / m));
//        scan.close();


//        Scanner scan = new Scanner(System.in);
//        // nextLine方式接收字符串
//        System.out.println("nextLine方式接收：");
//        // 判断是否还有输入
//        if (scan.hasNextLine()) {
//            String str2 = scan.nextLine();
//            System.out.println("输入的数据为：" + str2);
//        }
//        scan.close();
    }
}
