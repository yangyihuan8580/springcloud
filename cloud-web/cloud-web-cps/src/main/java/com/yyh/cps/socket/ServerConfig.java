package com.yyh.cps.socket;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server-config", ignoreUnknownFields = true)
@Data
public class ServerConfig {

    private int port ;

    private String delimiter;

    private int bossGroupSize;

    private int workGroupSize;

    private int bufferSize;

    private String dataType;

    private long readIdleTime;

    private long writeIdleTime=0;

    private long allIdleTime = 0;

    private boolean needHeart; //default false;

    private String listenerName;



}