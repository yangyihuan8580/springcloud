package com.yyh.common.excel.account;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yyh.common.excel.account.local.LocalAccountModel;
import com.yyh.common.excel.account.local.PayTypeEnum;
import com.yyh.common.excel.account.wechat.WechatAccountModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DiffTotalAccountModel {

    @ExcelProperty(index = 0,value="日期")
    private String date;

    @ExcelProperty(index = 1,value="航站楼ID")
    private Integer terminalId;

    @ExcelProperty(index = 2,value="支付方式")
    private Integer payType;

    @ExcelProperty(index = 3,value="第三方总笔数")
    private Integer thirdCount;

    @ExcelProperty(index = 4,value="第三方总金额")
    private BigDecimal thirdTotalMoney;

    @ExcelProperty(index = 5,value="本地总笔数")
    private Integer localCount;

    @ExcelProperty(index = 6,value="本地总金额")
    private BigDecimal localTotalMoney;


    public DiffTotalAccountModel (AccountTotalModel localAccountModel, AccountTotalModel thirdAccountModel, PayTypeEnum payTypeEnum, Integer terminalId) {
        this.date = thirdAccountModel.getDate();
        this.payType = payTypeEnum.getPayType();
        this.terminalId = terminalId;
        this.thirdCount = thirdAccountModel.getCount();
        this.thirdTotalMoney = thirdAccountModel.getTotalMoney();
        if (localAccountModel != null) {
            this.localCount = localAccountModel.getCount();
            this.localTotalMoney = localAccountModel.getTotalMoney();
        }
    }
}
