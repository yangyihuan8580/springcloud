package com.yyh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.yyh.elastic.*.repository")
@MapperScan("com.yyh.*.dao")
public class ParkProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParkProviderApplication.class, args);
    }

}
