package com.yyh.cps.executor;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class TcpUploadMessage<T> {

    /** 请求的唯一标识 */
    private String msgId;

    /** 请求时间戳 */
    private Long currentTime;

    /** 请求编码 */
    private String code;

    /** 请求内容 */
    private T data;

    /** 车场Id */
    private String parkId;

    private ChannelHandlerContext ctx;
}
