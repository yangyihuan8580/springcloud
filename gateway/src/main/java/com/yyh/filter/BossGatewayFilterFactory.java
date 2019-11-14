package com.yyh.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Arrays;
import java.util.List;

/**
 *  自定义boss拦截器
 */
public class BossGatewayFilterFactory extends AbstractGatewayFilterFactory<BossGatewayFilterFactory.Config> {

    private static final Logger logger = LoggerFactory.getLogger(BossGatewayFilterFactory.class);

    public static final String STATUS_KEY = "boss";

    public BossGatewayFilterFactory() {
        super(BossGatewayFilterFactory.Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(STATUS_KEY);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            /** 添加自定义逻辑 */
            ServerHttpRequest request = exchange.getRequest();
            logger.info("=====boss端请求======URI:{}",request.getURI().toString());
            String status = config.getStatus();
            logger.info("=====boss端请求======status:{}",status);
            return chain.filter(exchange);
        };
    }

    public static class Config {

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
