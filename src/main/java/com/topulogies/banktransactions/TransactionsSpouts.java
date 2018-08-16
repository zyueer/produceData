package com.topulogies.banktransactions;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;


public class TransactionsSpouts implements IRichSpout {

	private static final Integer MAX_FAILS = 2;
	Map<Integer,String> messages;
	Map<Integer,Integer> transactionFailureCount;
	Map<Integer,String> toSend;
	private SpoutOutputCollector collector;
	
	static Logger LOG = Logger.getLogger(TransactionsSpouts.class);

	public void open(Map conf, TopologyContext context,
					 SpoutOutputCollector collector) {
		Random random = new Random();
		messages = new HashMap<Integer, String>();
		toSend = new HashMap<Integer, String>();
		transactionFailureCount = new HashMap<Integer, Integer>();
		for(int i = 0; i< 100; i++){
			messages.put(i, "transaction_"+random.nextInt());
			transactionFailureCount.put(i, 0);
		}
		toSend.putAll(messages);
		this.collector = collector;
	}

	public void nextTuple() {
		if(!toSend.isEmpty()){
			for(Map.Entry<Integer, String> transactionEntry : toSend.entrySet()){
				Integer transactionId = transactionEntry.getKey();
				String transactionMessage = transactionEntry.getValue();
				collector.emit(new Values(transactionMessage),transactionId);
			}
			/*
			 * The nextTuple, ack and fail methods run in the same loop, so
			 * we can considerate the clear method atomic
			 */
			toSend.clear();
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {}
	}

	//	ack 方法只是简单的把事务从每个列表中删除。
	public void ack(Object msgId) {
		messages.remove(msgId);
		LOG.info("Message fully processed ["+msgId+"]");
	}

	//fail 方法决定应该重新发送一条消息，还是已经失败太多次而放弃它。
	public void fail(Object msgId) {
		if(!transactionFailureCount.containsKey(msgId))
			throw new RuntimeException("Error, transaction id not found ["+msgId+"]");
		Integer transactionId = (Integer) msgId;

		//Get the transactions fail
		Integer failures = transactionFailureCount.get(transactionId) + 1;
		if(failures >= MAX_FAILS){
			//If exceeds the max fails will go down the topology
			throw new RuntimeException("Error, transaction id ["+transactionId+"] has had many errors ["+failures+"]");
		}
		//If not exceeds the max fails we save the new fails quantity and re-send the message
		transactionFailureCount.put(transactionId, failures);
		toSend.put(transactionId,messages.get(transactionId));
		LOG.info("Re-sending message ["+msgId+"]");
	}

	public void close() {
		
	}

	@Override
	public void activate() {

	}

	@Override
	public void deactivate() {

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("transactionMessage"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
