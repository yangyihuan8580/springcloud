package com.yyh.common.excel_pay.xinlibo.local;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.DateUtils;
import com.yyh.common.excel_pay.Key;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class XlbOrder {

    @ExcelProperty(index = 0,value="订单编号")
    private String orderCode;

    @ExcelProperty(index = 2,value="交易号")
    private String transNo;

    @ExcelProperty(index = 3,value="车牌号")
    private String plateNumber;

    @ExcelProperty(index = 4,value="入场时间")
    private Date entryTime;

    @ExcelProperty(index = 5,value="交易类型")
    private String payType;

    @ExcelProperty(index = 6,value="出场时间")
    private Date exitTime;

    @ExcelProperty(index = 7,value="订单金额")
    private BigDecimal orderMoney;

    @ExcelProperty(index = 8,value="实收金额")
    private BigDecimal needMoney;

    @ExcelProperty(index = 10,value="出口")
    private String outCross;


    public Key getKey(Integer terminalId) {
        return new Key(terminalId, getPayDate());
    }

    public String getPayDate() {
        return DateUtils.format(exitTime, DateUtils.DATE_FORMAT_10);
    }
}