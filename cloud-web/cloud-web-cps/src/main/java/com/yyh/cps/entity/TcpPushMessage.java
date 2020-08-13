package com.yyh.cps.entity;

import lombok.Data;

@Data
public class TcpPushMessage<T> {

    /** 消息的标识 */
    private String msgId;

    /** 对应的停车编码 */
    private String code;

    /** 车场Id */
    private String parkId;

    /** 需要下发的数据 */
    private T data;

    /** 是否同步返回 */
    private boolean sync = true;

    /** 推送失败，是否需要重复推送 */
    private boolean repeat = true;

    /** 车场离线，数据是否需要存储（当车场再次上线时，推送到车场） */
    private boolean offline = true;

    /** 超时时间，默认6s */
    private long timeout = 6L;
}
