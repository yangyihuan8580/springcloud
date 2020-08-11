package com.yyh.cps.mq;

public interface MqService {

    boolean sendMsg(String topic,String tags, String key,String message);

}
