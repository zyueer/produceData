package com.springboot;

import java.sql.*;
import java.util.HashMap;

public class statement {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://172.24.8.34:3306/2018626test?useUnicode=true&characterEncoding=UTF8";
        String userName = "root";
        String password = "123456";

        Class.forName("com.mysql.jdbc.Driver");

        Connection conn = DriverManager.getConnection(url, userName, password);
        String name = "红楼梦";
        String sql_1 = "update book set bookName='"+name+"' where id = 1";//********
        Statement statement_1 = conn.createStatement();
        int r_1 = statement_1.executeUpdate(sql_1);
        System.out.println("r_1 >>>>"+r_1);
        statement_1.close();

        Statement statement_2 = conn.createStatement();
        ResultSet rs = statement_2.executeQuery("select * from book ");
        ResultSetMetaData rsm = rs.getMetaData();//**********
        rs.close();

        conn.close();
        HashMap map = new HashMap();
        map.put("","");
    }
}
