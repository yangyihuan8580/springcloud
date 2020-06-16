package com.yyh.common.excel_pay.xinlibo.aipark;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.DateUtils;
import com.yyh.common.excel_pay.Key;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AiparkOrder {

    @ExcelProperty(index = 0,value="订单Id")
    private String orderId;

    @ExcelProperty(index = 1,value="出口")
    private String outCross;

    @ExcelProperty(index = 2,value="车牌号")
    private String plateNumber;

    @ExcelProperty(index = 3,value="交易号")
    private String transNo;

    @ExcelProperty(index = 5,value="入口")
    private String inCross;

    @ExcelProperty(index = 6,value="入场时间")
    private Date entryTime;

    @ExcelProperty(index = 7,value="出场时间")
    private Date exitTime;

    @ExcelProperty(index = 8,value="交易类型")
    private String payType;

    @ExcelProperty(index = 9,value="订单金额")
    private BigDecimal orderMoney;

    @ExcelProperty(index = 10,value="优惠金额")
    private BigDecimal couponMoney;

    @ExcelProperty(index = 11,value="实收金额")
    private BigDecimal needMoney;

    @ExcelProperty(index = 14,value="订单编号")
    private String orderCode;

    public Key getKey(Integer terminalId) {
        return new Key(terminalId, getPayDate());
    }

    public String getPayDate() {
        return DateUtils.format(exitTime, DateUtils.DATE_FORMAT_10);
    }
}