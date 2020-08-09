package com.yyh.cps.socket;


import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static com.yyh.cps.socket.SocketConstant.DECODER;
import static com.yyh.cps.socket.SocketConstant.ENCODER;

@Component
public class CpsServer implements ApplicationRunner {

    private static Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class.getSimpleName());

    @Autowired
    private ServerConfig config;

    private NioEventLoopGroup bossGroup = null;

    private NioEventLoopGroup workerGroup = null;

    public void initServer() {
        logger.info("初始化netty server, config:{}", JSON.toJSONString(config));
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup(config.getBossGroupSize());
        workerGroup = new NioEventLoopGroup(config.getWorkGroupSize());
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        //通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        //保持长连接状态
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new LoggingHandler(LogLevel.INFO));
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                /** 分隔符 */
                setDelimiter(pipeline);
                /** 编码解码器 */
                setCoder(pipeline);
                /** 心跳配置 */
                setHeartbeat(pipeline);
                /** 业务处理器 */
                pipeline.addLast("textHandler", new TextSocketServerHandler(config));
            }
        });
        try {
            /** 增加钩子函数 */
            Runtime.getRuntime().addShutdownHook(new CloseThread());
            ChannelFuture future = bootstrap.bind(config.getPort()).sync();
            logger.info("netty server 启动成功");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private void setHeartbeat(ChannelPipeline pipeline) {
        boolean needHeart = config.isNeedHeart();
        if(needHeart){
            pipeline.addLast("idleStateHandler",new IdleStateHandler(
                    config.getReadIdleTime(),
                    config.getWriteIdleTime(),
                    config.getAllIdleTime(), TimeUnit.SECONDS));
            pipeline.addLast("heartbeat",new HeartbeatHandler());
        }
    }


    public void shutdown() {
        if (bossGroup != null && workerGroup != null) {
            if (!bossGroup.isShuttingDown() || !workerGroup.isShuttingDown()) {
                logger.info("=========开始关闭netty==========");
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                logger.info("=========关闭netty完毕==========");
            }
        }
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        initServer();
    }

    @PreDestroy
    public void destory() throws Exception {
        shutdown();
    }

    private void setDelimiter(ChannelPipeline pipeline) {
        //处理粘包，丢包
        String delimiter = config.getDelimiter();
        //分隔符
        if(StringUtils.isNotEmpty(delimiter)){
            ByteBuf _delimiter = Unpooled.copiedBuffer(delimiter.getBytes());
            int bufferSize = config.getBufferSize();
            if(bufferSize<1000){
                bufferSize = 1024;
            }
            pipeline.addLast(new DelimiterBasedFrameDecoder(bufferSize, _delimiter));
        }
    }

    private void setCoder(ChannelPipeline pipeline) {
        pipeline.addLast(DECODER,new StringDecoder(Charset.forName("utf-8")));
        pipeline.addLast(ENCODER,new StringEncoder(Charset.forName("utf-8")));
    }


    class CloseThread extends Thread {

        @Override
        public void run() {
            shutdown();
        }
    }
}