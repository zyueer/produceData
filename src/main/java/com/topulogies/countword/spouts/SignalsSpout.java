package com.topulogies.countword.spouts;

import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class SignalsSpout implements IRichSpout {

	private SpoutOutputCollector collector;


	@Override
	public void nextTuple() {
		collector.emit("signals",new Values("refreshCache"));//数据流ID
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
	}

	@Override
	public void ack(Object o) {

	}

	@Override
	public void fail(Object o) {

	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void close() {

	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("signals",new Fields("action"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
