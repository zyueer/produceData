package com.github.facker;

import com.github.javafaker.Faker;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable{
    public void run() {
        Locale local = new Locale("zh","CN");
        Faker faker = new Faker(local);
        for (int i=0;i<2;i++){
            StringBuilder insertPerson = new StringBuilder("insert into data_person(id_number,name,telephone,address,location,gender,marriage,father_name,mother_name,create_date) values");
            StringBuilder insertCall = new StringBuilder("insert into data_call(source_tel,target_tel,start_time,duration,create_date) values");
            StringBuilder insertTelephone = new StringBuilder("insert into data_telephone(telephone,user_name,operator_name,operator_address,remark,service_time,create_date) values");
            for (int j=0,k=0;j<100;j+=2,k++){
                StringBuilder firstPersonRecord = new StringBuilder("(");
                StringBuilder secondPersonRecord = new StringBuilder("(");
                StringBuilder callRecord = new StringBuilder("(");
                StringBuilder firstTelRecord = new StringBuilder("(");
                StringBuilder secondTelRecord = new StringBuilder("(");

                String idNumber1=faker.idNumber().validSvSeSsn();
                if (idNumber1.contains("-"))
                    idNumber1=idNumber1.replace("-",faker.date().birthday().toLocaleString()
                            .substring(0,5).replace("-","0605"));
                else
                    idNumber1=idNumber1.replace("+",faker.date().birthday().toLocaleString()
                            .substring(0,5).replace("-","0605"));
                firstPersonRecord.append(idNumber1+",");
                String name1 = faker.name().name();
                firstPersonRecord.append("'"+name1+"',");
                String phone1 = faker.phoneNumber().cellPhone();
                firstPersonRecord.append(phone1+",");
                firstPersonRecord.append("'"+faker.address().city()+"',");
                String location1 = faker.address().longitude()+","+faker.address().latitude();
//                firstPersonRecord.append("'"+faker.address().latitude()+"',");
//                firstPersonRecord.append("'"+faker.address().longitude()+"',");
                firstPersonRecord.append("'"+location1+"',");
                firstPersonRecord.append("'男',");
                firstPersonRecord.append("'未婚',");
                firstPersonRecord.append("'"+faker.name().name()+"',");
                firstPersonRecord.append("'"+faker.name().name()+"',");
                firstPersonRecord.append("'"+new Date().toLocaleString()+"')");

                firstTelRecord.append(phone1+",");
                firstTelRecord.append("'"+name1+"',");
                firstTelRecord.append("'中国移动',");
                firstTelRecord.append("'"+faker.address().city()+"',");
                firstTelRecord.append("'这是默认备注',");
                firstTelRecord.append(faker.number().numberBetween(1,20)+",");
                firstTelRecord.append("'"+new Date().toLocaleString()+"')");


                String idNumber2=faker.idNumber().validSvSeSsn();
                if (idNumber2.contains("-"))
                    idNumber2=idNumber2.replace("-",faker.date().birthday().toLocaleString()
                            .substring(0,5).replace("-","0605"));
                else
                    idNumber2=idNumber2.replace("+",faker.date().birthday().toLocaleString()
                            .substring(0,5).replace("-","0605"));
                secondPersonRecord.append(idNumber2+",");
                String name2 = faker.name().name();
                secondPersonRecord.append("'"+name2+"',");
                String phone2 = faker.phoneNumber().cellPhone();
                secondPersonRecord.append(phone2+",");
                secondPersonRecord.append("'"+faker.address().city()+"',");
                String location2 = faker.address().longitude()+","+faker.address().latitude();
                secondPersonRecord.append("'"+location2+"',");
//                secondPersonRecord.append("'"+faker.address().latitude()+"',");
//                secondPersonRecord.append("'"+faker.address().longitude()+"',");
                secondPersonRecord.append("'女',");
                secondPersonRecord.append("'未婚',");
                secondPersonRecord.append("'"+faker.name().name()+"',");
                secondPersonRecord.append("'"+faker.name().name()+"',");
                secondPersonRecord.append("'"+new Date().toLocaleString()+"')");

                secondTelRecord.append(phone2+",");
                secondTelRecord.append("'"+name2+"',");
                secondTelRecord.append("'中国联通',");
                secondTelRecord.append("'"+faker.address().city()+"',");
                secondTelRecord.append("'这是默认备注',");
                secondTelRecord.append(faker.number().numberBetween(1,20)+",");
                secondTelRecord.append("'"+new Date().toLocaleString()+"')");

                callRecord.append(phone1+",");
                callRecord.append(phone2+",");
                callRecord.append("'"+faker.date().past(10,TimeUnit.DAYS).toLocaleString()+"',");
                callRecord.append(faker.number().numberBetween(20,1000)+",");
                callRecord.append("'"+new Date().toLocaleString()+"')");

                insertPerson.append(firstPersonRecord+",");
                insertTelephone.append(firstTelRecord+",");
                if (k == 49){
                    insertPerson.append(secondPersonRecord);
                    insertTelephone.append(secondTelRecord);
                    insertCall.append(callRecord);
                } else{
                    insertPerson.append(secondPersonRecord+",");
                    insertTelephone.append(secondTelRecord+",");
                    insertCall.append(callRecord+",");
                }
            }
            try {
                produceData.getDataSource();
                Connection connection = null;
                long start = System.currentTimeMillis();
                connection = produceData.dataSource.getConnection();
                Statement statement = connection.createStatement();
                statement.execute(insertPerson.toString());
                statement.execute(insertCall.toString());
                statement.execute(insertTelephone.toString());
                long end = System.currentTimeMillis();
                System.out.println(Thread.currentThread().getName()+"-插入100000条数据消耗时间"+(end-start));
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
