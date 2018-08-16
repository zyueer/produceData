package com.github.facker;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

public class TaskUserBook implements Runnable{
    @Override
    public void run() {
        Locale local = new Locale("zh","CN");
        Faker faker = new Faker(local);
        for (int i=0;i<20;i++){
            StringBuilder dataUser = new StringBuilder("insert into data_borrow1(userId,bookName,date) values");
            for(int j=0;j<9999;j++){
                StringBuilder record = new StringBuilder("(3,");
                String bookName = faker.name().name();//注意！
                record.append("'"+bookName+"',");
                record.append("'"+new Date().toLocaleString()+"')");
                dataUser.append(record+",");
            }
            StringBuilder record = new StringBuilder("(3,");
            String bookName = faker.name().name();//注意！
            record.append("'"+bookName+"',");
            record.append("'"+new Date().toLocaleString()+"')");
            dataUser.append(record);

            try {
                produceData.getDataSource();//连接池
                Connection connection = produceData.dataSource.getConnection();
                Statement statement = connection.createStatement();
                statement.execute(dataUser.toString());
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
