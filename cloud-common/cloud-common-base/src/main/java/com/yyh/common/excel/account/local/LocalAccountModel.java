package com.yyh.common.excel.account.local;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.annotation.JSONField;
import com.yyh.common.excel.account.converter.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LocalAccountModel {


    @DateTimeFormat
    @ExcelProperty(index = 0,value="支付时间")
    @JSONField(format= DateUtils.DATE_FORMAT_19 )
    private Date payTime;

    @ExcelProperty(index = 1,value="商户订单号")
    private String payCode;

    @ExcelProperty(index = 2,value="订单号")
    private String orderCode;

    @ExcelProperty(index = 3,value="应结订单金额", converter = LocalBigDecimalConverter.class)
    private BigDecimal orderMoney;

    /** 1 = (1,2)  3 = (3) */
    @ExcelProperty(index = 4,value="航站楼id")
    private Integer terminalId;

    @ExcelProperty(index = 5,value="删除标识")
    private Integer d;

    /** 1 微信 2 支付宝  */
    /** @see PayTypeEnum */
    @ExcelProperty(index = 6,value="支付方式")
    private Integer payType;


    public LocalKey localKey() {
        return new LocalKey(DateUtils.format(payTime, "yyyy-MM-dd"), payType, terminalId);
    }

    public CodeKey codeKey() {
        return new CodeKey(orderCode, payCode);
    }
}
