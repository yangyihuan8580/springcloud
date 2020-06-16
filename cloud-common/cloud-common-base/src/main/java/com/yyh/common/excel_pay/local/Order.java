package com.yyh.common.excel_pay.local;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.yyh.common.excel.account.converter.LocalBigDecimalConverter;
import com.yyh.common.excel_pay.Key;
import com.yyh.common.excel_pay.OrderKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/21
 * \* Time: 22:22
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
public class Order {


    @ExcelProperty(index = 0,value="订单Id")
    private String orderId;

    @ExcelProperty(index = 1,value="订单号")
    private String orderCode;

    @DateTimeFormat
    @ExcelProperty(index = 3,value="入场时间")
    private Date entryTime;

    @DateTimeFormat
    @ExcelProperty(index = 4,value="出场时间")
    private Date exitTime;

    @ExcelProperty(index = 6,value="应结订单金额", converter = LocalBigDecimalConverter.class)
    private BigDecimal orderMoney;

    @ExcelProperty(index = 7,value="订单状态")
    private Integer orderStatus;

    @ExcelProperty(index = 11,value="删除标识")
    private Integer d = 0;

    @ExcelProperty(index = 13,value="车牌号")
    private String plateNumber;

    @ExcelProperty(index = 14,value="停车记录编号")
    private String recordCode;

    /** 1 = (1,2)  3 = (3) */
    @ExcelProperty(index = 20,value="航站楼id")
    private Integer terminalId;

    @ExcelProperty(index = 24,value="实收金额")
    private BigDecimal needMoney;

    @ExcelProperty(index = 25,value="支付金额")
    private Date payTime;

    @ExcelProperty(index = 26,value="日期")
    private String payDate;

    @ExcelProperty(index = 27,value="支付方式")
    private Integer payType;

    public Key getKey() {
        return new Key(terminalId, payDate);
    }

//    public void setTerminalId(Integer terminalId) {
//        if (terminalId == 1 || terminalId == 2) {
//            terminalId = 1;
//        }
//        this.terminalId = terminalId;
//    }


    public OrderKey getOrderKey() {
        return new OrderKey(orderCode, plateNumber, payTime, entryTime, needMoney, payType);
    }
}