package com.yyh.cps.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class ParkChannelVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** channelId */
    private String channelId;

    /** 本机标识 */
    private String localIdentify;

    private String parkId;


    public ParkChannelVO (String channelId, String localIdentify, String parkId) {
        this.channelId = channelId;
        this.localIdentify = localIdentify;
        this.parkId = parkId;
    }

}
