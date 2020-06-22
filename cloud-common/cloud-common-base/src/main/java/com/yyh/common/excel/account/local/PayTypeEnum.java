package com.yyh.common.excel.account.local;

public enum PayTypeEnum {

    WECHAT(1),
    ALIPAY(2),

    ;

    private Integer payType;

    PayTypeEnum(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayType() {
        return payType;
    }
}
