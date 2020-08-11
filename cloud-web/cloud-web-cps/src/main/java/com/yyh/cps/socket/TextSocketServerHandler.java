package com.yyh.cps.socket;

import com.yyh.common.context.SpringContextUtils;
import com.yyh.cps.executor.TcpMessage;
import com.yyh.cps.executor.TextSocketHelper;
import io.netty.channel.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class TextSocketServerHandler extends SimpleChannelInboundHandler {

    private static Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class.getSimpleName());

    private TextSocketHelper textSocketHelper;

    public TextSocketServerHandler(ServerConfig config) {

    }

    private TextSocketHelper getTextSocketHelper() {
        if (textSocketHelper == null) {
            textSocketHelper = SpringContextUtils.getBean(TextSocketHelper.class);
        }
        return textSocketHelper;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }



    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object o) throws Exception {
        String inMessage = o.toString();
        if (StringUtils.isEmpty(inMessage)) {
            logger.warn("messageReceived:: message为空");
            return;
        }
        String parkId = ChannelRepository.getInstance().getParkId(ctx.channel().id());
        /** 将消息放入业务处理池 */
        getTextSocketHelper().process(new TcpMessage(ctx, parkId, inMessage));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive::接收到客户端的断开信息================");
        Channel channel = ctx.channel();
        String parkId = ChannelRepository.getInstance().getParkId(channel.id());
        logger.info("channelInactive::接收到客户端的车场Id：{}", parkId);
        ChannelRepository.getInstance().closeChannel(channel);
        super.channelInactive(ctx);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.disconnect(ctx, promise);
    }
}