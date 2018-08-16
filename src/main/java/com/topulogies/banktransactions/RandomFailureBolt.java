package com.topulogies.banktransactions;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.Map;
import java.util.Random;



public class RandomFailureBolt implements IRichBolt {

	private static final Integer MAX_PERCENT_FAIL = 80;
	Random random = new Random();
	private OutputCollector collector;
	
	public void execute(Tuple input) {
		Integer r = random.nextInt(100);
		if(r > MAX_PERCENT_FAIL){
			collector.ack(input);
		}else{
			collector.fail(input);
		}

	}

	@Override
	public void cleanup() {

	}

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
