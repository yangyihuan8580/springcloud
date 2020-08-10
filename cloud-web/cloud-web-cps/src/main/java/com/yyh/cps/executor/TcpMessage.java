package com.yyh.cps.executor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class TcpMessage {

    private ChannelHandlerContext ctx;

    private String parkId;

    private String message;

    public UploadMessage parseData() {
        UploadMessage uploadMessage = JSON.parseObject(message,new TypeReference<UploadMessage<?>>() {});
        uploadMessage.setParkId(parkId);
        return uploadMessage;
    }
}