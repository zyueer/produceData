package com.topulogies.drpc;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.security.InvalidParameterException;
import java.util.Map;


public class AdderBolt<String> implements IRichBolt {

	private static final Object NULL = "NULL";
	private OutputCollector collector;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("id","result"));
	}

	@Override
	public Map<java.lang.String, Object> getComponentConfiguration() {
		return null;
	}

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

	}

	@Override
	public void execute(Tuple input) {
		//Parse the add expression
		String[] numbers = (String[]) input.getString(1).split("\\+");
		Integer added = 0;
		try{
			if(numbers.length<2){
				throw new InvalidParameterException("Should be at least 2 numbers");
			}
			for(String num : numbers){
				//Add each member
				added += Integer.parseInt((java.lang.String) num);
			}
		}catch(Exception e){
			//On error emit null
			collector.emit(new Values(input.getValue(0),NULL));
		}
		//Emit the result
		collector.emit(new Values(input.getValue(0),added));
	}

	@Override
	public void cleanup() {

	}
}
