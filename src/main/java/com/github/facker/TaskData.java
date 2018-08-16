package com.github.facker;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

public class TaskData implements Runnable{


    @Override
    public void run() {
        Locale local = new Locale("zh","CN");
        Faker faker = new Faker(local);
        for (int i=0;i<100;i++){
            StringBuilder dataUser = new StringBuilder("select * from ");
            try {
                produceData.getDataSource();//连接池
                Connection connection = produceData.dataSource.getConnection();
                Statement statement = connection.createStatement();
                statement.execute(toString());
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
