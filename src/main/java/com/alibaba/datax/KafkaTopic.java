package com.alibaba.datax;

import kafka.admin.AdminUtils;
import kafka.admin.RackAwareMode;
import kafka.serializer.StringEncoder;
import kafka.server.ConfigType;
import kafka.utils.ZkUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.security.JaasUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 Kafka topic 的基本操作
 */
public class KafkaTopic {

    private final static String KafkaURL = "192.168.1.114:2181";
    private final static int KafkaSessionTimeout = 30000;
    private final static int KafkaConnectionTimeout = 30000;

    //创建主题
    public void createKafkaTopic(){
        ZkUtils zkUtils = ZkUtils.apply(KafkaURL, KafkaSessionTimeout, KafkaConnectionTimeout, JaasUtils.isZkSecurityEnabled());
        // 创建一个topic: topic="t1" 分区=1 副本=1
        AdminUtils.createTopic(zkUtils, "t1", 1, 1, new Properties(), RackAwareMode.Enforced$.MODULE$);
        zkUtils.close();
    }

    // 删除主题(未彻底删除)
    public void deleteKafkaTopic(){
        ZkUtils zkUtils = ZkUtils.apply(KafkaURL, KafkaSessionTimeout, KafkaConnectionTimeout, JaasUtils.isZkSecurityEnabled());
        AdminUtils.deleteTopic(zkUtils,"t1");
        zkUtils.close();
    }

    // 修改主题
    private void editTopic() {
        ZkUtils zkUtils = ZkUtils.apply(KafkaURL, KafkaSessionTimeout, KafkaConnectionTimeout, JaasUtils.isZkSecurityEnabled());
        // 获取topic 't1'的topic属性属性
        Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "t1");
        // 增加topic级别属性
        props.put("min.cleanable.dirty.ratio", "0.3");
        // 删除topic级别属性
        props.remove("max.message.bytes");
        // 修改topic 't1'的属性
        AdminUtils.changeTopicConfig(zkUtils, "t1", props);
        zkUtils.close();
    }

    // 主题读取
    private void queryTopic() {
        ZkUtils zkUtils = ZkUtils.apply(KafkaURL, KafkaSessionTimeout, KafkaConnectionTimeout, JaasUtils.isZkSecurityEnabled());
        // 获取topic 't1'的topic属性属性
        Properties props = AdminUtils.fetchEntityConfig(zkUtils, ConfigType.Topic(), "t1");
        // 查询topic-level属性
        Iterator it = props.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " = " + value);
        }
        zkUtils.close();
    }

    //生产者
    public void produce(){
        //Kafka属性设置
        Properties properties = new Properties();
        properties.put("zookeeper.connect", "192.168.1.114:2181");//声明zk  如何ZK是集群呢？
        properties.put("serializer.class", StringEncoder.class.getName());
        properties.put("metadata.broker.list", "192.168.1.114:9092");// 声明kafka broker

        Producer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int messageNo = 1; messageNo < 10; messageNo++) {
            producer.send(new ProducerRecord<String, String>("t1",messageNo + "", UUID.randomUUID() + "itcast"));
        }
    }

    //消费者
    public void consumer() throws IOException {
        //kafka配置
        Properties properties = new Properties();
        InputStream inStream = KafkaTopic.class.getClassLoader().getResourceAsStream("consumer.properties");
        properties.load(inStream);

        //消费者
        Consumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        consumer.subscribe(Arrays.asList("t1"));//topics  消费者订阅topic
        while (true) {
            //从topic上拉取数据
            ConsumerRecords<String, String> records = consumer.poll(100);
            if (records.count() > 0) {
                //遍历每一条数据
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(record.value()+" "+record.topic()+" "+record.offset()+" "+record.partition());
                }
            }
        }
    }

    //多个分区，怎么办？
    //问题？topic
    //      insert:value.流.offset.
    //      select:

    //可以通过自定义partitioner来决定我们的消息应该存到哪个partition上，只需要在我们的代码上实现Partitioner接口即可。
    //!!! partitioner.class=com.uplooking.bigdata.kafka.partitioner.MyKafkaPartitioner
    public class MyKafkaPartitioner implements Partitioner {

        public void configure(Map<String, ?> configs) {

        }

        /**
         * 根据给定的数据设置相关的分区
         *
         * @param topic      主题名称
         * @param key        key
         * @param keyBytes   序列化之后的key
         * @param value      value
         * @param valueBytes 序列化之后的value
         * @param cluster    当前集群的元数据信息
         */
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            Integer partitionNums = cluster.partitionCountForTopic(topic);
            int targetPartition = -1;
            if (key == null || keyBytes == null) {
                targetPartition = new Random().nextInt(10000) % partitionNums;
            } else {
                int hashCode = key.hashCode();
                targetPartition = hashCode % partitionNums;
                System.out.println("key: " + key + ", value: " + value + ", hashCode: " + hashCode + ", partition: " + targetPartition);
            }
            return targetPartition;
        }

        public void close() {
        }
    }

    //Consumer Group
    public void consumerGroup(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "");//groupId
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serializa-tion.StringDeserializer");
        props.put("value.deserializer",
                "org.apache.kafka.common.serializa-tion.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList("t1"));//topic
        System.out.println("Subscribed to topic " +"t1");//topic
        int i = 0;

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
        }
    }

    public static void main(String[] args){

    }
}
