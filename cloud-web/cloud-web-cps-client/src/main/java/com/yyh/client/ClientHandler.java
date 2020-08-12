package com.yyh.client;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String s) throws Exception {
        try {
            System.out.println("messageReceived:" + s);
            HashMap<String,Object> hashMap = JSON.parseObject(s, HashMap.class);
            String code = hashMap.get("code").toString();
            if (!code.equals("CPS001")
                    && !code.equals("CPS002")) {
                System.out.println(code);
                Map<String,Object> result = new HashMap<>();
                result.put("msgId", hashMap.get("msgId"));
                result.put("currentTime", System.currentTimeMillis());
                result.put("code", code);
                Map<String,Object> map = new HashMap<>();
                map.put("message", UUID.randomUUID().toString());
                result.put("data", map);
                ctx.writeAndFlush(JSON.toJSONString(result) + "*").addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            System.out.println("=====发送成功");
                        } else {
                            channelFuture.cause().printStackTrace();
                        }
                    }
                });


            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* *//** 请求的唯一标识 *//*
    private String msgId;

    *//** 请求时间戳 *//*
    private Long currentTime;

    *//** 请求编码 *//*
    private String code;

    *//** 请求内容 *//*
    private T data;*/
}