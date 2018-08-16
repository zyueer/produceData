package com.alibaba.datax;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.utils.NimbusClient;
import org.apache.storm.utils.Utils;
import org.json.simple.JSONValue;

import java.util.Map;

import static org.apache.storm.Config.TOPOLOGY_NAME;

/**
 创建一个简单的拓扑，数单词数量.
 找出 Twitter 上的热点话题.
 */
public class StormTopology {
    //spout： WordReader 类实现了 IRichSpout 接口，WordReader负责从文件按行读取文本，并把文本行提供给第一个 bolt。
    //一个 spout 发布一个定义域列表。这个架构允许你使用不同的 bolts 从同一个spout 流读取数据，它们的输出也可作为其它 bolts 的定义域，

    public static void main(String[] args) {
        //  1、创建拓扑,设置spout、bolt.    TopologyBuilder
        //  在 spout 和 bolts 之间通过 shuffleGrouping ,或者fieldsGrouping方法连接。
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader", new WordReader());
        builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCounter(),2).fieldsGrouping("word-normalizer", new Fields("word"));
/*
        //配置
        //创建拓扑配置的 Config 对象，它会在运行时与集群配置合并，并通过prepare 方法发送给所有节点。
        Config conf = new Config();
        conf.put("wordsFile", args[0]);//由 spout 读取的文件的文件名，赋值给 wordFile 属性.文件在这里以参数传进去.
        conf.setDebug(false);

        //运行拓扑
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
        //LocalCluster cluster = new LocalCluster();  //运行拓扑模式，本地模式。StormSubmitter集群模式。
        //cluster.submitTopology("kafkaJobId", conf, builder.createTopology());//创建拓扑,提交拓扑
        try {
            StormSubmitter.submitTopology("kafkaJobId", conf, builder.createTopology());//创建拓扑,提交拓扑
        } catch (AlreadyAliveException e) {
            e.printStackTrace();
        } catch (InvalidTopologyException e) {
            e.printStackTrace();
        } catch (AuthorizationException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(5000);//休眠两秒钟（拓扑在另外的线程运行）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //cluster.shutdown();

        //问题？
        //如果你有一个非常大的日志文件呢？
        // 你能够很轻松的改变系统中的节点数量实现并行工作。这个时候，你就要创建两个 WordCounter** 实例。
        //builder.setBolt("word-counter", new WordCounter(),2).shuffleGrouping("word-normalizer"); //error,因为shuffleGrouping是随机分组
        //应该使用,builder.setBolt("word-counter", new WordCounter(),2).fieldsGrouping("word-normalizer", new Fields("word"));//域数据流组，如果你用 word 域为数据流分组，word-normalizer bolt 将只会把相同单词的元组发送给同一个 word-counterbolt 实例。
    */
// idea提交任务，读取本地Storm-core配置文件，提交集群
        try {
            Config conf = new Config();
            conf.put("wordsFile", args[0]);//由 spout 读取的文件的文件名，赋值给 wordFile 属性.文件在这里以参数传进去.
            conf.setDebug(false);

            //运行拓扑
            conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
            // 读取本地storm-core包下的storm.yaml配置
            Map stormConf = Utils.readStormConfig();
            // 读取classpath下的配置文件
            // Map stormConf = Utils.findAndReadConfigFile("storm.yaml");
            stormConf.put(Config.NIMBUS_HOST, "172.24.2.24");
            stormConf.put(Config.WORKER_CHILDOPTS, "-Xmx8192m");
            stormConf.putAll(conf);
            System.out.println(stormConf);

            // 提交集群运行的jar
            String inputJar = "D:\\GoogleDownload\\fackerData\\target\\fackerData-1.0-SNAPSHOT.jar";

            // 使用StormSubmitter提交jar包
            String uploadedJarLocation = StormSubmitter.submitJar(stormConf, inputJar);
            String jsonConf = JSONValue.toJSONString(stormConf);

            // 这种方式也可以，跟下面两句代码效果一致
            // Nimbus.Client client = NimbusClient.getConfiguredClient(stormConf).getClient();
            // client.submitTopology(TOPOLOGY_NAME, uploadedJarLocation, jsonConf, builder.createTopology());

            NimbusClient nimbus = new NimbusClient(stormConf, "172.24.2.24", 8744);
            nimbus.getClient().submitTopology("IDEA001", uploadedJarLocation, jsonConf, builder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
