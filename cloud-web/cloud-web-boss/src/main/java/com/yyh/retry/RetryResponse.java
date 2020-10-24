package com.yyh.retry;

/**
 * @ClassName RetryResponse
 * @Deacription TODO
 * @Author Administrator
 * @Date 2020/10/24 13:50
 * @Version 1.0
 **/
public class RetryResponse<T> {

    private int status;

    private T data;

    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean success() {
        return status == 200;
    }
}
