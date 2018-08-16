package com.github.facker;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.github.javafaker.Faker;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class produceData {
    public static DataSource dataSource;

    public static void getDataSource() {    //Druid数据库连接池
        Properties properties = new Properties();
        try {
            InputStream inputStream = produceData.class.getClassLoader().getResourceAsStream("druid.properties");
            properties.load(inputStream);
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);//Executors线程池
        for (int i=0;i<5;i++){
            executorService.execute(new TaskUserBook());
        }
        executorService.shutdown();
        //更新
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        Locale local = new Locale("zh","CN");
//        Faker faker = new Faker(local);
//        Connection conn = null;
//        Statement state = null;
//        try {
//            conn = DriverManager.getConnection("jdbc:mysql://172.24.8.34:3306/2018626test","root","123456");
//            String location = faker.address().latitude()+","+faker.address().longitude();
//            String sql = "update data_person set longitude ='"+location+"' where id is not null ";
//            state = conn.createStatement();
//            state.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
