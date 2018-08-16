package com.topulogies.countword.bolts;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;


public class WordCounter implements IRichBolt {

	Integer id;
	String name;
	Map<String, Integer> counters;
	private OutputCollector collector;

	/**
	 * On each word We will count
	 */
	@Override
	public void execute(Tuple input) {
		String str = null;
		try{
			str = input.getStringByField("word");
		}catch (IllegalArgumentException e) {
			//Do nothing
		}

		if(str!=null){
			/**
			 * If the word dosn't exist in the map we will create
			 * this, if not We will add 1
			 */
			if(!counters.containsKey(str)){
				counters.put(str, 1);
			}else{
				Integer c = counters.get(str) + 1;
				counters.put(str, c);
			}
		}else{
			if(input.getSourceStreamId().equals("signals")){
				str = input.getStringByField("action");
				if("refreshCache".equals(str))
					counters.clear();
			}
		}
		//Set the tuple as Acknowledge
		collector.ack(input);
	}

	/**
	 * At the end of the spout (when the cluster is shutdown
	 * We will show the word counters
	 */
	@Override
	public void cleanup() {
		System.out.println("-- Word Counter ["+name+"-"+id+"] --");
		for(Map.Entry<String, Integer> entry : counters.entrySet()){
			System.out.println(entry.getKey()+": "+entry.getValue());
		}
	}

	/**
	 * On create 
	 */
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

	}
}
