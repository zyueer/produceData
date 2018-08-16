package com.alibaba.datax;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

//按行读取文件并每行发布一个元组.
public class WordReader implements IRichSpout {
    private SpoutOutputCollector collector;
    private TopologyContext context;
    private FileReader fileReader;
    private boolean complete = false;

    /**
     第一个被调用的方法
     参数：  conf:配置参数  topologyContext:拓扑数据  spoutOutputCollector:传给bolts处理的数据
     我们将创建一个文件并维持一个collector对象.
     */
    public void open(Map conf, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.context = topologyContext;
        try {
            this.fileReader = new FileReader(conf.get("wordsFile").toString());//主函数的Config配置
            System.out.println("WordReader open");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.collector = spoutOutputCollector;
    }

    /**
     通过它向 bolts 发布待处理的数据.
     给第一个bolt,分发文件中的文本行，这个方法会不断的被调用，直到整个文件都读完了.？？？
     nextTuple 会在同一个循环内被 ack() 和 fail() 周期性的调用
     */
    public void nextTuple() {
        if(complete)  return;

        //用来按行读取文件并每行发布一个元组
        BufferedReader reader = new BufferedReader(fileReader);
        System.out.println("WordReader nextTuple");
        try {
            String str = reader.readLine();//按行读取
            while(str != null){
                //按行发布一个新值
                this.collector.emit(new Values(str),str);//Values 是一个 ArrarList 实现.
                str = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            complete = true;
        }

    }

    //声明输入域
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("line"));//??要声明 bolt 的出参
    }

    public void close() {

    }

    public void activate() {

    }

    public void deactivate() {

    }

    //在一个元组被正确的处理时调用 ack** 方法，而在失败时调用 fail** 方法。
    public void ack(Object o) {
        System.out.println("OK:"+o);
    }

    public void fail(Object o) {
        System.out.println("Fail:"+o);
    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
