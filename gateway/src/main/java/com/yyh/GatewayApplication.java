package com.yyh;

import com.yyh.filter.BossGatewayFilterFactory;
import com.yyh.filter.CustomGlobalFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AddResponseHeaderGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.HystrixGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.DispatcherHandler;

import java.util.*;

@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public GlobalFilter CustomGlobalFilter() {
        return new CustomGlobalFilter();
    }

    @Bean
    public BossGatewayFilterFactory bossGatewayFilterFactory() {
        return new BossGatewayFilterFactory();
    }

}
