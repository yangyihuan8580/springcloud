package com.yyh.cps.service.impl;

import com.alibaba.fastjson.JSON;
import com.yyh.cache.cache.constant.CacheKeyPrefix;
import com.yyh.cache.cache.service.CacheService;
import com.yyh.cps.constant.CpsResultCodeEnum;
import com.yyh.cps.entity.TcpPushMessage;
import com.yyh.cps.executor.TcpResult;
import com.yyh.cps.executor.future.FutureRepository;
import com.yyh.cps.executor.future.SyncWriteFuture;
import com.yyh.cps.mq.MqService;
import com.yyh.cps.service.CpsMessageService;
import com.yyh.cps.socket.ChannelRepository;
import com.yyh.cps.socket.ServerConfig;
import com.yyh.cps.vo.ParkChannelVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service("cpsMessageService")
public class CpsMessageServiceImpl implements CpsMessageService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private MqService mqService;

    @Override
    public TcpResult sendMessage(TcpPushMessage tcpPushMessage) {
        boolean checked = checkMessage(tcpPushMessage);
        if (!checked) {
            return TcpResult.result(CpsResultCodeEnum.PARAM_ERROR, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
        }
        boolean success = cacheService.setNx(CacheKeyPrefix.CHANNEL_HTTP_LOCK.getPrefix() + tcpPushMessage.getMsgId(), tcpPushMessage.getMsgId(), CacheKeyPrefix.CHANNEL_HTTP_LOCK.getTime());
        if (!success) {
            return TcpResult.result(CpsResultCodeEnum.REQUEST_REPEAT, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
        }
        String parkId = tcpPushMessage.getParkId();
        /** 判断车场是否在线 */
        Object o = cacheService.get(CacheKeyPrefix.CHANNEL_PARK.getPrefix() + parkId);
        if (o == null) {
            log.info("sendMessage,车场不在线,parkId:{}");
            if (tcpPushMessage.isOffline()) {
                log.info("sendMessage,数据需要存储");
                // TODO
            }
            return TcpResult.result(CpsResultCodeEnum.UNREGISTERED, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
        }
        ParkChannelVO channelParkVO = (ParkChannelVO)o;
        FutureRepository.futureMap.put(tcpPushMessage.getMsgId(), new SyncWriteFuture(tcpPushMessage.getMsgId(), tcpPushMessage.getTimeout()));
        Channel channel = ChannelRepository.getInstance().getChannel(parkId);
        if (channel != null) {
            channel.writeAndFlush(JSON.toJSONString(tcpPushMessage) + serverConfig.getDelimiter())
                    .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.info("消息发送成功");
                    } else {
                        log.warn("消息发送失败", channelFuture.cause());
                    }
                }
            });
        } else {
//            mqService.sendMsg()
        }
        TcpResult tcpResult;
        SyncWriteFuture syncWriteFuture = FutureRepository.futureMap.get(tcpPushMessage.getMsgId());
        try {
            Object o1 = syncWriteFuture.get(tcpPushMessage.getTimeout(), TimeUnit.SECONDS);
            if (syncWriteFuture.timeout()) {
                tcpResult = TcpResult.result(CpsResultCodeEnum.REQUEST_TIMEOUT, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
            } else {
                tcpResult = TcpResult.success(o1, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
            }
        } catch (Exception e) {
            log.error("下发数据异常" + e.getMessage(), e);
            tcpResult = TcpResult.error(tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
        } finally {
            FutureRepository.futureMap.remove(tcpPushMessage.getMsgId());
        }
        return tcpResult;
    }

    private boolean checkMessage(TcpPushMessage tcpPushMessage) {
        if (tcpPushMessage == null
                || StringUtils.isEmpty(tcpPushMessage.getCode())
                || StringUtils.isEmpty(tcpPushMessage.getMsgId())
                || StringUtils.isEmpty(tcpPushMessage.getParkId())
            ) {
            return false;
        }
        return true;
    }
}
