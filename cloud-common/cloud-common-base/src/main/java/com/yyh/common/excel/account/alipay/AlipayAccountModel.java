package com.yyh.common.excel.account.alipay;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayAccountModel {

    @ExcelProperty(index = 1,value="支付宝订单号")
    private String alipayPayCode;

    @ExcelProperty(index = 2,value="商户订单号")
    private String orderCode;

    @DateTimeFormat
    @ExcelProperty(index = 4,value="发生时间")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    @ExcelProperty(index = 6,value="收入金额")
    private BigDecimal orderMoney;
}
