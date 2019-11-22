package com.yyh.common.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;

@Data
public class BaseRequest<T> implements Serializable {

    private String k;

    @Valid
    private T data;

    private long ts;

    private int pageSize;

    private int pageNum;

    public static <T> BaseRequest<T> toObj(String json, Class<T> clazz) {
        return JSON.parseObject(json, new TypeReference<BaseRequest<T>>(clazz) {});
    }

}
