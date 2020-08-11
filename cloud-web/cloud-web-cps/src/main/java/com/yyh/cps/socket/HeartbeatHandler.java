/**
 * 
 */
package com.yyh.cps.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yangyihuan
 *
 */
public class HeartbeatHandler extends ChannelHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class.getSimpleName());

	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if(evt instanceof IdleStateEvent){
            IdleStateEvent e = (IdleStateEvent) evt;
			if(e.state() == IdleState.READER_IDLE){
			    logger.info("长时间未接收到消息，关闭channel");
				ChannelRepository.getInstance().closeChannel(ctx.channel());
			}else if(e.state() == IdleState.WRITER_IDLE){
				ByteBuf buff = ctx.alloc().buffer();
                buff.writeBytes("writetimeout*".getBytes());  
                ChannelFuture writeAndFlush = ctx.writeAndFlush(buff);
                if(!writeAndFlush.isSuccess()){
                    logger.info("长时间未接收到消息，关闭channel");
                    ChannelRepository.getInstance().closeChannel(ctx.channel());
                }
			}
		}
	}
	
}
