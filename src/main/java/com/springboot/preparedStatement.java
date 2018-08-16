package com.springboot;

import java.sql.*;

public class preparedStatement {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://172.24.8.34:3306/2018626test?useUnicode=true&characterEncoding=UTF8";
        String userName = "root";
        String password = "123456";
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver");


        try {
            conn = DriverManager.getConnection(url, userName, password);

//创建PreparedStatement语句
            PreparedStatement pstmtDelete = conn.prepareStatement(
                    "DELETE FROM student WHERE stu_id>=?");
            PreparedStatement pstmtInsert = conn.prepareStatement(
                    "INSERT INTO student VALUES(?, ?, ?, ?)");
            PreparedStatement pstmtSelect = conn.prepareStatement(
                    "SELECT * FROM student WHERE stu_id>=? " +
                            "ORDER BY stu_id");

            int id = 0;

//多次执行同一语句
            for (int i=0; i<3; i++, id++) {

//使用setXXX方法设置IN参数
                pstmtDelete.setString(1, Integer.toString(id));

                pstmtInsert.setString(1, Integer.toString(id));
                pstmtInsert.setString(2, "name"+id);
                pstmtInsert.setString(3, "city"+id);
                pstmtInsert.setDate(4, new Date(78, 2, id));

//执行PreparedStatement语句
                pstmtDelete.executeUpdate();
                pstmtInsert.executeUpdate();
                ResultSet rs = pstmtSelect.executeQuery();

                System.out.println("");
                System.out.println("第 " + (i+1) + " 次循环后的结果集为：");

//显示返回的结果集
                while (rs.next()) {
                    String stuID = rs.getString(1);
                    String address = rs.getString(3);
                    String birthday= rs.getString(4);

                }
            }

            pstmtDelete.close();
            pstmtInsert.close();
            pstmtSelect.close();

        } catch(SQLException e) {
            System.out.println("出现SQLException异常");
        } finally {
//关闭语句和数据库连接
            try {
                if (conn != null) conn.close();
            } catch(SQLException e) {
                System.out.println("关闭数据库连接时出现异常");
            }
        }

    }
}
