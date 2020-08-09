package com.yyh.cps.executor;


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
}