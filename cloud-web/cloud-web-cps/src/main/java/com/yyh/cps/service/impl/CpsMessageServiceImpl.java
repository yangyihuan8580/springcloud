package com.yyh.cps.service.impl;

import com.yyh.cache.cache.constant.CacheKeyPrefix;
import com.yyh.cache.cache.service.CacheService;
import com.yyh.cps.constant.CpsResultCodeEnum;
import com.yyh.cps.constant.FailConstant;
import com.yyh.cps.entity.TcpPushMessage;
import com.yyh.cps.executor.TcpResult;
import com.yyh.cps.executor.future.FutureRepository;
import com.yyh.cps.executor.future.SyncWriteFuture;
import com.yyh.cps.mq.MqService;
import com.yyh.cps.service.CpsMessageService;
import com.yyh.cps.socket.ChannelRepository;
import com.yyh.cps.socket.ServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service("cpsMessageService")
public class CpsMessageServiceImpl implements CpsMessageService {

    @Autowired
    private CacheService cacheService;

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
                saveFailMessage(tcpPushMessage, FailConstant.PARK_OFFLINE);
            }
            return TcpResult.result(CpsResultCodeEnum.UNREGISTERED, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
        }

        SyncWriteFuture future = pushMessageToPark(tcpPushMessage);
        if (tcpPushMessage.isSync()) {
            return syncWaitResponse(tcpPushMessage, future);
        } else {
            return syncWaitStatus(tcpPushMessage, future);
        }
    }

    private TcpResult syncWaitStatus(TcpPushMessage tcpPushMessage, SyncWriteFuture syncWriteFuture) {
        TcpResult tcpResult;
        try {
            boolean status = syncWriteFuture.isStatus(tcpPushMessage.getTimeout(), TimeUnit.SECONDS);
            if (syncWriteFuture.timeout()) {
                tcpResult = TcpResult.result(CpsResultCodeEnum.REQUEST_TIMEOUT, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
            } else if (status){
                tcpResult = TcpResult.success(tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
            } else {
                tcpResult = TcpResult.error(tcpPushMessage.getCode(), tcpPushMessage.getMsgId());

            }
        } catch (Exception e) {
            log.error("下发数据异常" + e.getMessage(), e);
            tcpResult = TcpResult.error(tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
        } finally {
            FutureRepository.futureMap.remove(tcpPushMessage.getMsgId());
        }
        return tcpResult;
    }

    @NotNull
    private TcpResult syncWaitResponse(TcpPushMessage tcpPushMessage, SyncWriteFuture syncWriteFuture) {
        TcpResult tcpResult;
        try {
            Object o1 = syncWriteFuture.getResponse(tcpPushMessage.getTimeout(), TimeUnit.SECONDS);
            if (syncWriteFuture.timeout()) {
                tcpResult = TcpResult.result(CpsResultCodeEnum.REQUEST_TIMEOUT, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
                saveFailMessage(tcpPushMessage, FailConstant.PUSH_TIMEOUT);
            } else {
                tcpResult = TcpResult.success(o1, tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
            }
        } catch (Exception e) {
            log.error("下发数据异常" + e.getMessage(), e);
            tcpResult = TcpResult.error(tcpPushMessage.getCode(), tcpPushMessage.getMsgId());
            saveFailMessage(tcpPushMessage, FailConstant.PUSH_ERROR);
        } finally {
            FutureRepository.futureMap.remove(tcpPushMessage.getMsgId());
        }
        return tcpResult;
    }

    @NotNull
    private SyncWriteFuture pushMessageToPark(TcpPushMessage tcpPushMessage) {
        SyncWriteFuture future = FutureRepository.createFuture(tcpPushMessage.getMsgId(), tcpPushMessage.getTimeout());
        if (ChannelRepository.getInstance().getChannel(tcpPushMessage.getParkId()) != null) {
            ChannelRepository.getInstance().sendMessageToPark(tcpPushMessage, future);
        } else {
//            mqService.sendMsg()
        }
        return future;
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


    private void saveFailMessage(TcpPushMessage message, int failType) {

    }
}
