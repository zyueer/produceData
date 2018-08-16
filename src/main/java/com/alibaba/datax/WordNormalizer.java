package com.alibaba.datax;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 第一个 bolt，WordNormalizer，负责得到并标准化每行文本。它把文本行切分成单词，大写转化成小写，去掉头尾空白符。
 */
public class WordNormalizer implements IRichBolt {
    private OutputCollector collector;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector=outputCollector;
    }

    /**
     * 这个*bolt*的出参域“word”域
     */
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word"));
    }

    /**
     处理传入的元组,还会再发布若干个元组。
     第一行从元组读取值。值可以按位置或名称读取。接下来值被处理并用collector对象发布。
     最后，每次都调用collector 对象的 ack() 方法确认已成功处理了一个元组。
     * */
    public void execute(Tuple input) {
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for(String word : words){
            word = word.trim();
            if(!word.isEmpty()){
                word=word.toLowerCase();
                //发布这个单词
                List a = new ArrayList();
                a.add(input);
                collector.emit(a,new Values(word));
            }
        }
        //对元组做出应答
        collector.ack(input);
    }

    public void cleanup() {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
