package com.yyh.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * 车场配置类
 */
@Component
@RefreshScope
@Data
public class ParkConfiguration {

    @Value("${parkName}")
    private String parkName;
}
