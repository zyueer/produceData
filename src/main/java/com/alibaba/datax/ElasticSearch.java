package com.alibaba.datax;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * mysql数据--kafka--hbase--ES
*/

public class ElasticSearch {
    public static TransportClient client;
    private final static Logger logger = LoggerFactory.getLogger(ElasticSearch.class);

    public static String esIP;
    public static int esPORT;
    public static String CLUSTER_NAME;
    public static String indexName;
    public static String ontologyId;

    //ES 建立连接初始化
    public void buildClient(String es_IP, int es_port, String cluster_name, String index_name, String ontology_id) {
        esIP = es_IP;
        esPORT = es_port;
        CLUSTER_NAME = cluster_name;
        indexName = index_name;
        ontologyId = ontology_id;
        //建立连接，详见ES Java API
        try {
            Settings esSettings = Settings.builder().put("cluster.name", CLUSTER_NAME)
                    .put("client.transport.sniff", true).build();
            client = new PreBuiltTransportClient(esSettings);
            //单节点ES连接
//            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.24.2.24"), 9300));
//            System.out.println(client);

            //多节点连接
            List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
            for (String host : es_IP.split(",")) {
                host = host.trim();
                InetSocketTransportAddress address = new InetSocketTransportAddress(
                        InetAddress.getByName(host), es_port);
                addressList.add(address);
            }
            client.addTransportAddresses(
                    addressList.toArray(new InetSocketTransportAddress[addressList.size()]));
            logger.info("create es client success");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //this.buildMapping(origin2type);
    }

    //ES 建立索引,参数可以是json、Map、XContentBuilder
    public void buildIndex(Map<String, Object> content){
        //String content是json
        IndexResponse response = client.prepareIndex("zhang1", "106")
                .setSource(content)
                .get();
        //content 是Map

        logger.info("index create success");
        //
    }

    //ES 添加数据
    public static void addDocument(String content) throws Exception{    //JSONArray格式content=[{},{}]
        //BulkRequestBuilder
//        BulkRequestBuilder bulkRequest = client.prepareBulk();
//        JSONArray data = JSONArray.parseArray(content);
//        for(int i = 0; i < data.size(); i++){
//            data.getJSONObject(i).remove("index");
//            bulkRequest.add(client.prepareIndex(indexName, ontologyId,data.getJSONObject(i).getString("rowkey"))
//                    .setSource(data.getJSONObject(i)));
//        }
//        BulkResponse bulkResponse = bulkRequest.get();
//        if(bulkResponse.hasFailures()){
//            System.out.println("bulk add succeed!");
//        }
        //content是一条数据
        JSONObject data = JSONObject.parseObject(content);
        IndexRequestBuilder builder = client.prepareIndex(indexName, ontologyId,data.getString("rowkey"));
        data.remove("index");
        builder.setSource(data);
        builder.get();
    }
    public static void main(String[] args){
        ElasticSearch es = new ElasticSearch();
        //1、建立连接
        es.buildClient("172.24.2.24",9300,"dynamic_ontology","zhang1","AWQ58AYd4nLJdtX-UO3V");
        //2、建立索引
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        //es.buildIndex(json);

    }


}
