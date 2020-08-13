package com.yyh.cps.mq.listener;

import com.alibaba.fastjson.JSON;
import com.yyh.cps.executor.UploadMessage;
import com.yyh.cps.executor.future.FutureRepository;
import com.yyh.cps.executor.future.SyncWriteFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SyncFutureListener {

    public void consume(UploadMessage uploadMessage) {
        log.info("接收到MS上传的消息, message:", JSON.toJSONString(uploadMessage));
        String msgId = uploadMessage.getMsgId();
        SyncWriteFuture future = FutureRepository.futureMap.get(msgId);
        if (future == null) {
            log.info("消息不存在, msgId:{}", msgId);
            return;
        }
        if (future.timeout()) {
            log.info("消息已经超时, msgId:{}", msgId);
            return;
        }
        future.setResponse(uploadMessage.getData());
    }
}
