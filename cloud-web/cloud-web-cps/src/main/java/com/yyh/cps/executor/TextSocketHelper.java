package com.yyh.cps.executor;

import com.alibaba.fastjson.JSON;
import com.yyh.common.base.Result;
import com.yyh.cps.socket.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class TextSocketHelper {

    private static Logger logger = LoggerFactory.getLogger(TcpMessageService.class);

    @Autowired
    private TcpMessageService tcpMessageService;

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ServerConfig serverConfig;

    public void process(final TcpMessage tcpMessage) {
        logger.info("消息放入业务池中message:{},parkId:{}", tcpMessage.getMessage(), tcpMessage.getParkId());
        executor.execute(new Runnable() {

            @Override
            public void run() {
                logger.info("开始处理tcp消息,message:{},parkId:{}", tcpMessage.getMessage(), tcpMessage.getParkId());


                Result result = tcpMessageService.execute(tcpMessage);
                logger.info("处理结果:{}", JSON.toJSONString(result));
                if (result != null) {
                    tcpMessage.getCtx().writeAndFlush(result + serverConfig.getDelimiter());
                }
            }
        });
    }
}