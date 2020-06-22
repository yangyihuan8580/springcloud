package com.yyh.common.excel.account;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yyh.common.excel.account.alipay.AlipayAccountModel;
import com.yyh.common.excel.account.local.LocalAccountModel;
import com.yyh.common.excel.account.local.PayTypeEnum;
import com.yyh.common.excel.account.wechat.WechatAccountModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DiffAccountModel {

    @ExcelProperty(index = 0,value="商户订单号")
    private String payCode;

    @ExcelProperty(index = 1,value="订单号")
    private String orderCode;

    @ExcelProperty(index = 2,value="支付时间")
    private Date payTime;

    @ExcelProperty(index = 3,value="第三方订单金额")
    private BigDecimal thirdOrderMoney;

    @ExcelProperty(index = 4,value="本地订单金额")
    private BigDecimal localOrderMoney;

    @ExcelProperty(index = 5,value="支付方式")
    private Integer payType;

    @ExcelProperty(index = 6,value="航站楼ID")
    private Integer terminalId;

    @ExcelProperty(index = 7,value="备注")
    private String remark;


    public DiffAccountModel(WechatAccountModel accountModel, LocalAccountModel localAccountModel, String remark, Integer terminalId) {
        this.orderCode = accountModel.getOrderCode();
        this.payCode = accountModel.getWechatPayCode();
        this.payTime = accountModel.getPayTime();
        this.thirdOrderMoney = accountModel.getOrderMoney();
        if (localAccountModel != null) {
            this.localOrderMoney = localAccountModel.getOrderMoney();
        }
        this.remark = remark;
        this.payType = PayTypeEnum.WECHAT.getPayType();
        this.terminalId = terminalId;
    }

    public DiffAccountModel(AlipayAccountModel accountModel, LocalAccountModel localAccountModel, String remark, Integer terminalId) {
        this.orderCode = accountModel.getOrderCode();
        this.payCode = accountModel.getAlipayPayCode();
        this.payTime = accountModel.getPayTime();
        this.thirdOrderMoney = accountModel.getOrderMoney();
        if (localAccountModel != null) {
            this.localOrderMoney = localAccountModel.getOrderMoney();
        }
        this.remark = remark;
        this.payType = PayTypeEnum.WECHAT.getPayType();
        this.terminalId = terminalId;
    }
}
