package com.yyh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * boss属性配置类，支持动态更新
 * 更新地址  http://ip:port/actuator/refresh
 * 配置加载优先级  config配置中心 > application.yml  >  bootstrap.yml  >   默认值
 */
@Component
@RefreshScope
@Data
public class BossConfiguration {

    @Value("${is_need_verify}")
    private boolean needVerify;

    @Value("${test:111}")
    private String test;

    @Value("${swagger2.enable}")
    private boolean swaggerEnable;

}
