package com.yyh.cps.socket;


import com.alibaba.fastjson.JSON;
import com.yyh.cache.cache.constant.CacheKeyPrefix;
import com.yyh.cache.cache.service.CacheService;
import com.yyh.common.context.SpringContextUtils;
import com.yyh.common.util.AddressUtils;
import com.yyh.cps.entity.TcpPushMessage;
import com.yyh.cps.executor.future.SyncWriteFuture;
import com.yyh.cps.vo.ParkChannelVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ChannelRepository {

    /** 车场Id -> channelId */
    private ConcurrentHashMap<String,ChannelId> parkId2channelIdMap = new ConcurrentHashMap();

    /** channelId -> 车场Id */
    private ConcurrentHashMap<ChannelId,String> channelId2parkIdMap = new ConcurrentHashMap();

    /** channelId -> channel */
    private ConcurrentHashMap<ChannelId,Channel> channelMap = new ConcurrentHashMap();

    private CacheService cacheService;

    private ServerConfig serverConfig;

    private ChannelRepository() {

    }

    private static class ChannelRepositoryInstance {

        private static final ChannelRepository instance = new ChannelRepository();

    }

    public static ChannelRepository getInstance() {
        return ChannelRepositoryInstance.instance;
    }


    private CacheService getCacheService() {
        if (cacheService == null) {
            cacheService = SpringContextUtils.getBean(CacheService.class);
        }
        return cacheService;
    }


    private ServerConfig getServerConfig() {
        if (serverConfig == null) {
            serverConfig = SpringContextUtils.getBean(ServerConfig.class);
        }
        return serverConfig;
    }

    public ChannelId getChannelId(String parkId) {
        if (StringUtils.isNotEmpty(parkId)) {
            ChannelId channelId = parkId2channelIdMap.get(parkId);
            return channelId;
        }
        return null;
    }


    public Channel getChannel(String parkId) {
        if (StringUtils.isNotEmpty(parkId)) {
            ChannelId channelId = getChannelId(parkId);
            if(channelId != null) {
                Channel channel = channelMap.get(channelId);
                return channel;
            }
        }
        return null;
    }


    public String getParkId(ChannelId channelId) {
        if (channelId != null) {
            String parkId = channelId2parkIdMap.get(channelId);
            return parkId;
        }
        return null;
    }

    public void sendMessageToPark(TcpPushMessage tcpPushMessage, SyncWriteFuture future) {
        Channel channel = getChannel(tcpPushMessage.getParkId());
        log.info("向MS下发数据,message:{}", JSON.toJSONString(tcpPushMessage));
        channel.writeAndFlush(JSON.toJSONString(tcpPushMessage) + getServerConfig().getDelimiter())
            .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.info("消息发送成功");
                        if (!tcpPushMessage.isSync() && future != null) {
                            future.setStatus(true);
                        }
                    } else {
                        log.warn("消息发送失败", channelFuture.cause());
                        if (!tcpPushMessage.isSync() && future != null) {
                            future.setStatus(false);
                        }
                    }
                }
            });
    }


    public void putChannel(String parkId, Channel channel) {
        if (StringUtils.isEmpty(parkId) || channel == null) {
            return;
        }
        channelMap.put(channel.id(), channel);
        parkId2channelIdMap.put(parkId, channel.id());
        channelId2parkIdMap.put(channel.id(), parkId);

        /** 放入redis */
        ParkChannelVO channelParkVO = new ParkChannelVO(channel.id().asLongText(), AddressUtils.localIdentify, parkId);
        getCacheService().set(CacheKeyPrefix.CHANNEL_PARK.getPrefix() + parkId, channelParkVO, CacheKeyPrefix.CHANNEL_PARK.getTime(), TimeUnit.SECONDS);
        /**  车场上线以及后续操作处理  */

    }

    /** 通道信息更新 */
    public void updateChannelInfo(Channel channel, String parkId) {
        if (channel == null || StringUtils.isEmpty(parkId)) {
            return;
        }
        getCacheService().expire(CacheKeyPrefix.CHANNEL_PARK.getPrefix() + parkId, CacheKeyPrefix.CHANNEL_PARK.getTime(), TimeUnit.SECONDS);

    }

    public void closeChannel(Channel channel) {
        if (channel == null) {
            return;
        }

        if (!channel.isOpen()) {
            channel.close();
        }
        String parkId = channelId2parkIdMap.remove(channel.id());
        log.info("channel close, parkId:{}", parkId);
        channelMap.remove(channel);
        if (StringUtils.isNotEmpty(parkId)) {
            parkId2channelIdMap.remove(parkId);
        }
        /** 清除redis */
        getCacheService().del(CacheKeyPrefix.CHANNEL_PARK.getPrefix() + parkId);

        /**  车场状态变更以及后续操作处理  */
    }
}