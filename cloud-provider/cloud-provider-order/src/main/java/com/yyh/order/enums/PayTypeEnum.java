package com.yyh.order.enums;

import java.util.Arrays;
import java.util.List;

public enum PayTypeEnum {
    CASH(1,"现金");

    private int payType;

    private String desc;

    PayTypeEnum(int payType, String desc) {
        this.payType = payType;
        this.desc = desc;
    }


    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static void main(String[] args) {
        List<PayTypeEnum> payTypeEnums = Arrays.asList(PayTypeEnum.values());
        System.out.println(payTypeEnums.get(0).getPayType());
    }
}
