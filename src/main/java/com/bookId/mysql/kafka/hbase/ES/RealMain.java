package com.bookId.mysql.kafka.hbase.ES;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.*;

import java.sql.*;
import java.util.*;

/**
 bookId--mysql--kafka--hbase--ES
 */
public class RealMain {
    public final static Logger logger = LoggerFactory.getLogger(RealMain.class);

    //连接mysql获取bookGroup的 多条的数据，批量。
    public void getOneDataFromMysql(int bookGroup) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.info("class do not find");
        }
        Connection conn = null;
        Statement state = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://172.24.8.34:3306/2018626test","root","123456");
            String sql = "SELECT bookId,bookGroup,bookName,bookAuthor,bookType,createTime FROM book WHERE bookGroup = '"+bookGroup+"'";
            state = conn.createStatement();
            ResultSet rs = state.executeQuery(sql);
            JSONArray resultJSON = convertJSON(rs);
            System.out.println(resultJSON);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("mysql connection no success");
        }
    }

    //扔到kafka消息队列里面
    public void insertKafka(String context){
        Properties props = new Properties();
        props.put("metadata.broker.list", "slave1:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        // key.serializer.class默认为serializer.class
        props.put("key.serializer.class", "kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
//        ProducerConfig config = new ProducerConfig(props);
//
//        Producer<String, String> producer = new Producer<String, String>(config);
//        for (int i = 0; i < 1000; i++) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            producer.send(new KeyedMessage<String, String>(topic, key, data + i));
//
//        }
//
//        producer.close();
    }
    //将ResultSet转为JSON
    public JSONArray convertJSON(ResultSet rs) throws SQLException {
        JSONArray resultJSON = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()){
            JSONObject jsonObject = new JSONObject();
            for(int i = 1; i <= rsmd.getColumnCount(); i++ ){
                jsonObject.put(rsmd.getColumnName(i),rs.getString(i));
            }
            resultJSON.add(jsonObject);
        }
        return resultJSON;
    }
    //将ResultSet转为List
    public List onvertList(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        while (rs.next()) {     //每一行数据
            Map rowData = new HashMap();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;

    }
    public static void main(String[] args){
        RealMain RM = new RealMain();
        RM.getOneDataFromMysql(101);
    }

}
