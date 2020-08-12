package com.yyh.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class CpsClient implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initClient();
    }

    private final String host = "localhost";

    private final int port = 3400;

    private Channel channel;

    public void initClient() throws InterruptedException {
        final EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)  // 使用NioSocketChannel来作为连接用的channel类
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("正在连接中...");
                        ChannelPipeline pipeline = ch.pipeline();
                        setDelimiter(pipeline);
                        pipeline.addLast(new StringDecoder(Charset.forName("utf-8")));
                        pipeline.addLast(new StringEncoder(Charset.forName("utf-8")));
                        pipeline.addLast(new ClientHandler()); //客户端处理类
                    }
                });
        //发起异步连接请求，绑定连接端口和host信息
        final ChannelFuture future = b.connect(host, port).sync();

        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture arg0) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接服务器成功");
                    sendHeartbeat();
                    sendMessage();
                } else {
                    System.out.println("连接服务器失败");
                    future.cause().printStackTrace();
                    group.shutdownGracefully(); //关闭线程组
                }
            }
        });

        this.channel = future.channel();
    }

    private void sendHeartbeat() {
        Timer timer = new Timer();
        Map<String,Object> map = new HashMap<>();
        map.put("code", "CPS002");
        map.put("currentTime", System.currentTimeMillis());
        map.put("data", new Object());
        map.put("msgId", "123456");
        map.put("parkId", "123456");
        String message = JSON.toJSONString(map);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                channel.writeAndFlush(message + "*");
            }
        }, 10000, 10000);
    }


    private void setDelimiter(ChannelPipeline pipeline) {
        //处理粘包，丢包
        String delimiter = "*";
        //分隔符
        if(StringUtils.isNotEmpty(delimiter)){
            ByteBuf _delimiter = Unpooled.copiedBuffer(delimiter.getBytes());
            int bufferSize = 1048576;
            pipeline.addLast(new DelimiterBasedFrameDecoder(bufferSize, _delimiter));
        }
    }

    public void sendMessage() throws InterruptedException {
        System.out.println("========================");
        Thread.sleep(2000);
        Map<String,Object> map = new HashMap<>();
        map.put("code", "CPS001");
        map.put("currentTime", System.currentTimeMillis());
        map.put("data", new Object());
        map.put("msgId", "123456");
        map.put("parkId", "123456");
        String message = JSON.toJSONString(map);
        message = message + "*";
        channel.writeAndFlush(message).addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("发送成功");
                } else {
                    System.out.println("发送失败");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
