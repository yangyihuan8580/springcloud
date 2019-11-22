package com.yyh.cache.cache.vo;

import java.io.Serializable;

public class RedisObj implements Serializable {

    private Object value;

    private long createTime;

    public RedisObj(Object value) {
        createTime = System.currentTimeMillis();
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getCreateTime() {
        return createTime;
    }
}
