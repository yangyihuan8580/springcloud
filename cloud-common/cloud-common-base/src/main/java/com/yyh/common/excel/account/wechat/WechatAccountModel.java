package com.yyh.common.excel.account.wechat;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.fastjson.annotation.JSONField;
import com.yyh.common.excel.account.converter.WechatBigDecimalConverter;
import com.yyh.common.excel.account.converter.WechatConverter;
import com.yyh.common.excel.account.converter.WechatDateConverter;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WechatAccountModel {

    @DateTimeFormat
    @ExcelProperty(index = 0,value="交易时间", converter = WechatDateConverter.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    @ExcelProperty(index = 5,value="微信订单号", converter = WechatConverter.class)
    private String wechatPayCode;

    @ExcelProperty(index = 6,value="商户订单号", converter = WechatConverter.class)
    private String orderCode;

    @ExcelProperty(index = 12,value="应结订单金额", converter = WechatBigDecimalConverter.class)
    private BigDecimal orderMoney;

    @ExcelProperty(index = 16,value="退款金额", converter = WechatBigDecimalConverter.class)
    private BigDecimal refundMoney;

}
