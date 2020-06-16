package com.yyh.common.excel_pay.xinlibo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.DateUtils;
import com.yyh.common.excel_pay.OrderKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ThirdOrder {

    @ExcelProperty(index = 0,value="订单编号")
    private String orderId;

    @ExcelProperty(index = 1,value="出场通道")
    private String outCross;

    @ExcelProperty(index = 2,value="车牌号")
    private String plateNumber;

    @ExcelProperty(index = 3,value="交易号")
    private String transNo;

    @ExcelProperty(index = 4,value="入场通道号")
    private String inCross;

    @ExcelProperty(index = 5,value="入场时间")
    private Date entryTime;

    @ExcelProperty(index = 6,value="出场时间")
    private Date exitTime;

    @ExcelProperty(index = 7,value="支付方式")
    private String payType;

    @ExcelProperty(index = 8,value="订单金额")
    private BigDecimal orderMoney;

    @ExcelProperty(index = 9,value="优惠金额")
    private BigDecimal couponMoney;

    @ExcelProperty(index = 10,value="实收金额")
    private BigDecimal needMoney;

    @ExcelProperty(index = 11,value="订单编号")
    private String orderCode;

    @ExcelProperty(index = 12,value="航站楼ID")
    private Integer terminalId;

    @ExcelProperty(index = 13,value="备注")
    private String remark;

    @ExcelProperty(index = 14,value="日期")
    private String payDate;

    private Integer count = 1;

    public OrderKey getOrderKey() {
        Integer payType = 0;
        if (this.payType.equals("微信提前支付")) {
            payType = 101;
        } else if (this.payType.equals("微信付款码")) {
            payType = 105;
        } else if (this.payType.equals("微信无感")) {
            payType = 103;
        } else if (this.payType.equals("支付宝提前支付")) {
            payType = 102;
        } else if (this.payType.equals("支付宝付款码")) {
            payType = 106;
        } else if (this.payType.equals("支付宝无感")) {
            payType = 104;
        }
        return new OrderKey(orderCode, plateNumber, exitTime, entryTime, needMoney, payType);
    }

    public String getPayDate() {
        return DateUtils.format(this.exitTime, DateUtils.DATE_FORMAT_10);
    }

    public void setTerminalId(Integer terminalId) {
//        if (terminalId == 1 || terminalId == 2) {
//            terminalId = 1;
//        }
        this.terminalId = terminalId;
    }
}