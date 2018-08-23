package com.basic;

import java.io.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class expBigDecimal {
    public static void main(String[] args) throws IOException {
        System.out.println(1.01 + 2.02);

        BigDecimal b1 =new BigDecimal("1.34");
        System.out.println("b1: " + b1);
        BigDecimal b2 =new BigDecimal("2.34");
        BigDecimal b3 = b1.add(b2);
        System.out.println("b3: " + b3);//b1并没有变
        Integer I1 = new Integer(100);
        boolean f =  Integer.valueOf("10") instanceof Integer;
        System.out.println(f);

        String str = new String("12");
        int s = Integer.parseInt(str);
        String z = str.toString();
        System.out.println(s);

        // 初始化 Date 对象
        Date date = new Date();
        System.out.println(date.getTime());
        // 使用 toString() 函数显示日期时间
        System.out.println(date.toString());
        Console cons = System.console();
        String username = cons.readLine("name");
        char[] passwd = cons.readPassword("Password: ");



    }
}
