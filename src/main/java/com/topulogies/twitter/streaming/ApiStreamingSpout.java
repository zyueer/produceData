package com.topulogies.twitter.streaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ApiStreamingSpout implements IRichSpout {

	static String STREAMING_API_URL="https://stream.twitter.com/1/statuses/filter.json?track=";
	private String track;
	private String user;
	private String password;
	private DefaultHttpClient client;
	private SpoutOutputCollector collector;
	private UsernamePasswordCredentials credentials;
	private BasicCredentialsProvider credentialProvider;

	LinkedBlockingQueue<String> tweets = new LinkedBlockingQueue<String>();
	
	static Logger LOG = Logger.getLogger(ApiStreamingSpout.class);
	static JSONParser jsonParser = new JSONParser();
	
	@Override
	public void nextTuple() {
		/*
		 *  创建http客户端
		 */
		client = new DefaultHttpClient ();//setCredentialsProvider
		client.setCredentialsProvider(credentialProvider);
		HttpGet get = new HttpGet(STREAMING_API_URL+track);		
		HttpResponse response;
		try {
			//Execute
			response = client.execute(get);
			StatusLine status = response.getStatusLine();
			if(status.getStatusCode() == 200){
				InputStream inputStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				String in;
				//Read line by line
				//它一次读一行数据，并把数据从 JSON 转化成 Java 对象，然后发布它。
				while((in = reader.readLine())!=null){
					try{
						//Parse and emit
						Object json = jsonParser.parse(in);
						collector.emit(new Values(track,json));
					}catch (ParseException e) {
						LOG.error("Error parsing message from twitter",e);
					}
				}
			}
		} catch (IOException e) {
			LOG.error("Error in communication with twitter api ["+get.getURI().toString()+"]");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
			}
		} 
	}

	@Override
	public void ack(Object o) {

	}

	@Override
	public void fail(Object o) {

	}

	//采用拓扑并行化，多个 spout 从同一个流读取数据的不同部分。
	//Storm 你可以在任意组件内（spouts/bolts）访问TopologyContext。
	// 利用这一特性，你能够把流划分到多个 spouts 读取。
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		//从context对象获取spout大小
		int spoutsSize = context.getComponentTasks(context.getThisComponentId()).size();
		//从这个spout得到任务id
		int myIdx = context.getThisTaskIndex();
		String[] tracks = ((String) conf.get("track")).split(",");
		StringBuffer tracksBuffer = new StringBuffer();
		for(int i=0; i< tracks.length;i++){
			////Check if this spout must read the track word
			if( i % spoutsSize == myIdx){
				tracksBuffer.append(",");
				tracksBuffer.append(tracks[i]);
			}
		}
		
		if(tracksBuffer.length() == 0)
			throw new RuntimeException("没有为spout得到track配置" +
				" [spouts大小:"+spoutsSize+", tracks:"+tracks.length+"] tracks的数量必须高于spout的数量");
		
		this.track =tracksBuffer.substring(1).toString();
		
		user = (String) conf.get("user");
		password = (String) conf.get("password");
		
		credentials = new UsernamePasswordCredentials(user, password);//spout 从配置对象得到连接参数（track，user，password)
		credentialProvider = new BasicCredentialsProvider();
		credentialProvider.setCredentials(AuthScope.ANY, credentials);
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
		declarer.declare(new Fields("criteria","tweet"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
