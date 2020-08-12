package com.yyh.cps.constant;

public enum CpsResultCodeEnum {

    SUCCESS("0000", "操作成功"),
    ERROR("9999", "操作成功"),
    PARAM_ERROR("1001", "报文格式错误"),
    UNREGISTERED("1002", "车场不在线"),
    CODE_ERROR("1003", "请求编码错误"),
    REQUEST_REPEAT("1004", "重复请求"),
    REQUEST_TIMEOUT("1005", "请求超时"),

    ;

    private String code;

    private String message;

    CpsResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
