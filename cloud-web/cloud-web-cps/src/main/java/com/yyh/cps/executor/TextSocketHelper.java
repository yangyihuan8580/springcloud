package com.yyh.cps.executor;

import com.yyh.cache.cache.constant.CacheKeyPrefix;
import com.yyh.cache.cache.service.CacheService;
import com.yyh.common.context.SpringContextUtils;
import com.yyh.cps.constant.CpsApiConstant;
import com.yyh.cps.constant.CpsResultCodeEnum;
import com.yyh.cps.executor.future.FutureRepository;
import com.yyh.cps.executor.future.SyncWriteFuture;
import com.yyh.cps.executor.request.RequestMapping;
import com.yyh.cps.mq.MqService;
import com.yyh.cps.socket.ChannelRepository;
import com.yyh.cps.socket.ServerConfig;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

@Component
public class TextSocketHelper {

    private static Logger logger = LoggerFactory.getLogger(TcpMessageService.class);

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private MqService mqService;

    public void process(final TcpMessage tcpMessage) {
        logger.info("消息放入业务池中message:{},parkId:{}", tcpMessage.getMessage(), tcpMessage.getParkId());
        executor.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    logger.info("开始处理tcp消息,message:{},parkId:{}", tcpMessage.getMessage(), tcpMessage.getParkId());
                    UploadMessage uploadMessage = tcpMessage.parseData();
                    boolean checked = checkMessage(uploadMessage);
                    if (!checked) {
                        logger.info("==========报文格式错误=======");
                        String result = TcpResult.result(CpsResultCodeEnum.PARAM_ERROR, uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                        //setResult(uploadMessage, result);
                        return;
                    }
                    boolean success = cacheService.setNx(CacheKeyPrefix.CHANNEL_TCP_LOCK.getPrefix() + uploadMessage.getMsgId(), System.currentTimeMillis(), CacheKeyPrefix.CHANNEL_TCP_LOCK.getTime());
                    if (!success) {
                        logger.info("{}秒,接收到重复数据，数据丢弃", CacheKeyPrefix.CHANNEL_TCP_LOCK.getTime());
                        return;
                    }
                    /**  判断车场是否注册 */
                    if (!uploadMessage.getCode().equals(CpsApiConstant.CPS_001)) {
                        Channel channel = ChannelRepository.getInstance().getChannel(tcpMessage.getParkId());
                        if (channel == null) {
                            logger.info("==========车场未注册=======");
                            String result = TcpResult.result(CpsResultCodeEnum.UNREGISTERED, uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                            setResult(uploadMessage, result);
                            return;
                        } else {
                            /** 更新车场缓存信息 */
                            ChannelRepository.getInstance().updateChannelInfo(channel, uploadMessage.getParkId());
                        }
                    }
                    Class<?> tcpMessageServiceClass = RequestMapping.getClassByCode(uploadMessage.getCode());
                    if (tcpMessageServiceClass == null) {
                        logger.info("编码不存在，走默认逻辑====================");
                        callback(uploadMessage);
                        return;
                    }
                    TcpMessageService tcpMessageService = (TcpMessageService) SpringContextUtils.getBean(tcpMessageServiceClass);
                    if (tcpMessageService == null) {
                        logger.info("编码不存在，走默认逻辑====================");
                        callback(uploadMessage);
                        return;
                    }
                    TcpResult tcpResult;
                    try {
                        tcpResult = tcpMessageService.execute(uploadMessage);
                    } catch (Exception e) {
                        logger.info("消息处理异常:" + e.getMessage(), e);
                        tcpResult = TcpResult.error(uploadMessage.getCode(), uploadMessage.getMsgId());
                    }
                    String result;
                    if (tcpResult != null) {
                        result = tcpResult.toString();
                    } else {
                        result = TcpResult.success(uploadMessage.getCode(), uploadMessage.getMsgId()).toString();
                    }
                    if (tcpResult != null) {
                        setResult(uploadMessage, result);
                    }
                } catch (Exception e) {
                    logger.error("tcp上行消息处理异常:"+ e.getMessage(), e);
                }

            }
        });
    }

    private void callback(UploadMessage uploadMessage) {
        SyncWriteFuture syncWriteFuture = FutureRepository.futureMap.get(uploadMessage.getMsgId());
        logger.info("syncWriteFuture :"+ syncWriteFuture);
        if (syncWriteFuture != null) {
            syncWriteFuture.setResponse(uploadMessage.getData());
        } else {
//            mqService.sendMsg()
        }
    }

    private void setResult(UploadMessage message, String s) {
        logger.info("请求返回报文:{}", s);
        /** 心跳请求不返回 */
        if (!message.getCode().equals(CpsApiConstant.CPS_002)) {
//            message.getCtx().writeAndFlush(s + serverConfig.getDelimiter());
        }
    }

    private boolean checkMessage(UploadMessage uploadMessage) {
        if (uploadMessage == null
                || StringUtils.isEmpty(uploadMessage.getCode())
                || StringUtils.isEmpty(uploadMessage.getMsgId())
                || null == uploadMessage.getCurrentTime()
            ) {
            return false;
        }
        return true;
    }
}