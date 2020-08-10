package com.yyh.cps.executor;

import com.alibaba.fastjson.JSON;
import com.yyh.cps.constant.CpsResultCodeEnum;
import lombok.Data;

@Data
public class TcpResult {

    private String responseMsgId;

    private Object data = new Object();

    private String code;

    private Long currentTime;

    private String resultCode;

    private String resultMessage;

    public TcpResult(Object data, String code, String responseMsgId, String resultCode, String resultMessage) {
        this.code = code;
        this.currentTime = System.currentTimeMillis();
        this.data = data;
        this.responseMsgId = responseMsgId;
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public TcpResult(Object data, String code, String responseMsgId, CpsResultCodeEnum cpsResultCodeEnum) {
        this.code = code;
        this.currentTime = System.currentTimeMillis();
        this.data = data;
        this.responseMsgId = responseMsgId;
        this.resultCode = cpsResultCodeEnum.getCode();
        this.resultMessage = cpsResultCodeEnum.getMessage();
    }

    public TcpResult() {

    }

    public static TcpResult success(Object data, String code, String responseMsgId) {
        return new TcpResult(data, code, responseMsgId, CpsResultCodeEnum.SUCCESS);
    }

    public static TcpResult success(String code, String responseMsgId) {
        return new TcpResult(new Object(), code, responseMsgId, CpsResultCodeEnum.SUCCESS);
    }

    public static TcpResult result(CpsResultCodeEnum cpsResultCodeEnum, Object data, String code, String responseMsgId) {
        return new TcpResult(data, code, responseMsgId, cpsResultCodeEnum);
    }

    public static TcpResult result(CpsResultCodeEnum cpsResultCodeEnum, String code, String responseMsgId) {
        return new TcpResult(new Object(), code, responseMsgId, cpsResultCodeEnum);
    }

    public static TcpResult error(Object data, String code, String responseMsgId) {
        return new TcpResult(data, code, responseMsgId, CpsResultCodeEnum.ERROR);
    }

    public static TcpResult error(String code, String responseMsgId) {
        return new TcpResult(new Object(), code, responseMsgId, CpsResultCodeEnum.ERROR);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }



}
