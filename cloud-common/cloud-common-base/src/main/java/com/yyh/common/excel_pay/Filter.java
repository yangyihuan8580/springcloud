package com.yyh.common.excel_pay;

import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: yyh
 * \* Date: 2020/3/23
 * \* Time: 13:39
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class Filter {

    public static boolean filter(Key key, OrderKey orderKey) {
        List<Integer> terminalIdList = new ArrayList<>();
        terminalIdList.add(1);
        terminalIdList.add(2);
        String payDate = "2019-11";
//        if (!orderKey.getPlateNumber().equals("京N6UY00")) {
//            return false;
//        }
//        if (!orderKey.getOrderCode().equals("jingJAZ333381997295132086272")) {
//            return false;
//        }
        return terminalIdList.contains(key.getTerminalId()) && key.getPayDate().contains(payDate);
    }

    public static boolean filter(String path) {
        String terminalId_t1 = "T1";
        String terminalId_t2 = "T2";

        String month = "11";
        String payType = "支付宝";
        boolean a = (path.contains(terminalId_t1) || path.contains(terminalId_t2))
                && path.contains(month)
                && path.contains(payType);
        if (a) {
            System.out.println(path + "===" + a);
        }
        return a;
    }
}