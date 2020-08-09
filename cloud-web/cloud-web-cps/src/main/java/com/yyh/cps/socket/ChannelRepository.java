package com.yyh.cps.socket;


import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelRepository {

    /** 车场Id -> channelId */
    private ConcurrentHashMap<String,ChannelId> parkId2channelIdMap = new ConcurrentHashMap();

    /** channelId -> 车场Id */
    private ConcurrentHashMap<ChannelId,String> channelId2parkIdMap = new ConcurrentHashMap();

    private ConcurrentHashMap<ChannelId,Channel> channelMap = new ConcurrentHashMap();

    private ChannelRepository() {

    }

    private static class ChannelRepositoryInstance {

        private static final ChannelRepository instance = new ChannelRepository();

    }

    public static ChannelRepository getInstance() {
        return ChannelRepositoryInstance.instance;
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


    public void putChannel(String parkId, Channel channel) {
        if (StringUtils.isEmpty(parkId) || channel == null) {
            return;
        }
        channelMap.put(channel.id(), channel);
        parkId2channelIdMap.put(parkId, channel.id());
        channelId2parkIdMap.put(channel.id(), parkId);
        /** 放入redis */

        /**  车场状态变更以及后续操作处理  */
    }

    public void closeChannel(Channel channel) {
        if (channel == null) {
            return;
        }
        channel.close();
        channelMap.remove(channel);
        String parkId = channelId2parkIdMap.remove(channel.id());
        if (StringUtils.isNotEmpty(parkId)) {
            parkId2channelIdMap.remove(parkId);
        }
        /** 清除redis */

        /**  车场状态变更以及后续操作处理  */
    }
}