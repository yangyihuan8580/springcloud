package com.yyh.common.exception;

public class IDRuntimeException extends RuntimeException{


    public IDRuntimeException() {
        super("id不能为负数");
    }

    public IDRuntimeException(String message) {
        super(message);
    }

    public IDRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IDRuntimeException(Throwable cause) {
        super(cause);
    }
}
