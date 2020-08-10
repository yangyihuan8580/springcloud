package com.yyh.common.base;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class Code implements Serializable {

    private String code;

    public Code(String code) {
        if (StringUtils.isEmpty(code)) {
            throw new RuntimeException("编号不能为空");
        }
        this.code = code;
    }


    public String getValue() {
        return this.code;
    }

    public void setValue(String code) {
        this.code = code;
    }
}
