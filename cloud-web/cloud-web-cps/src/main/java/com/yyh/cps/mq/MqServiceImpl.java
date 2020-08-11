package com.yyh.cps.mq;

import org.springframework.stereotype.Service;

@Service("mqService")
public class MqServiceImpl implements MqService {

    @Override
    public boolean sendMsg(String topic, String tags, String key, String message) {
        return false;
    }
}
