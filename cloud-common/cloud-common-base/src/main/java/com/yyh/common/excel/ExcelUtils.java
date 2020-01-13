package com.yyh.common.excel;

import com.yyh.common.excel.account.alipay.AlipayUtils;
import com.yyh.common.excel.account.local.LocalUtils;
import com.yyh.common.excel.account.wechat.WechatUtils;

import java.util.HashSet;
import java.util.Set;


public class ExcelUtils {


    public static void main(String[] args) throws InterruptedException {
        LocalUtils.readLocalAccountExcel("C:\\Users\\Administrator\\Desktop\\测试\\local_alipay_26-31.xlsx");

//        Set<String> wechatPath = new HashSet<>();
//        wechatPath.add("C:\\Users\\Administrator\\Desktop\\测试\\T3微信12.26-12.31.xlsx");
//        WechatUtils.batchReadWechatAccountExcel(wechatPath);
//
//        WechatUtils.diffWechatAccount(3);

        Set<String> alipayPath = new HashSet<>();
        alipayPath.add("C:\\Users\\Administrator\\Desktop\\12月微信支付宝对账\\T2支付宝-26-31\\T2支付宝\\20191227_2088521542716393\\20885214551755170156_20191227_账务明细.xlsx");
        AlipayUtils.batchReadAlipayAccountExcel(alipayPath);

        AlipayUtils.diffAlipayAccount(1);

    }
}
