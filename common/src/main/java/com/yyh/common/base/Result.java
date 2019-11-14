package com.yyh.common.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(description = "通用响应返回对象")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -7254888630210798460L;

    public static final String SUCCESS_CODE = "0000";

    public static final String ERROR_CODE = "9999";

    public static final Object EMPTY_OBJECT = new Object();

    public static final Result SUCCESS = Result.success();

    public static final Result ERROR = Result.error();

    @ApiModelProperty(value = "结果编码")
    private String code;

    private String msg;

    private T data;

    public Result(String code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result() {
    }

    private static Result success() {
        return new Result(SUCCESS_CODE, "请求成功", EMPTY_OBJECT);
    }

    public static <T> Result success(T data) {
        return new Result(SUCCESS_CODE, "请求成功", data);
    }

    public static <T> Result success(T data, String msg) {
        return new Result(SUCCESS_CODE, msg, data);
    }

    public static Result error() {
        return new Result(ERROR_CODE, "请求失败", EMPTY_OBJECT);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, EMPTY_OBJECT);
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
