package com.yyh.cps.mq.listener;

import com.yyh.cps.entity.TcpPushMessage;
import com.yyh.cps.executor.future.FutureRepository;
import com.yyh.cps.executor.future.SyncWriteFuture;
import com.yyh.cps.socket.ChannelRepository;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MessagePushListener {

    public void consume(TcpPushMessage tcpPushMessage) {
        String parkId = tcpPushMessage.getParkId();
        if (StringUtils.isEmpty(parkId)) {
            log.info("parkId is null");
            return;
        }
        Channel channel = ChannelRepository.getInstance().getChannel(parkId);
        if (channel == null) {
            log.info("channel is null");
            return;
        }
        SyncWriteFuture future = FutureRepository.futureMap.get(tcpPushMessage.getMsgId());
        ChannelRepository.getInstance().sendMessageToPark(tcpPushMessage, future);
    }
}
