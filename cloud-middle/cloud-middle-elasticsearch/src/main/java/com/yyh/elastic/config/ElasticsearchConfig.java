package com.yyh.elastic.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

//    public RestHighLevelClient getClient(){
//        try {
//            RestHighLevelClient client =new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.1.66",9200,"http")));
//            return client;
//
//        } catch (Exception e){
//
//        }
//        return null;
//    }
}
