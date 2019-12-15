package com.yyh.elastic.example;


import org.elasticsearch.action.index.IndexRequest;

/**
 * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.2/java-rest-high-search.html
 */
public class Example {

    public static void main(String[] args) {
        IndexRequest request = new IndexRequest();
        request.type("parkRecord").id("1111").index()
    }
}