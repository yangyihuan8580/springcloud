package com.yyh.cps.executor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;


@Data
@AllArgsConstructor
public class TcpMessage {

    private ChannelHandlerContext ctx;

    private String parkId;

    private String message;

    public TcpUploadMessage parseData() {
        TcpUploadMessage uploadMessage = JSON.parseObject(message,new TypeReference<TcpUploadMessage<?>>() {});
        if (StringUtils.isNotEmpty(parkId)) {
            uploadMessage.setParkId(parkId);
        }
        if (ctx != null) {
            uploadMessage.setCtx(ctx);
        }
        return uploadMessage;
    }
}