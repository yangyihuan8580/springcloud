package com.yyh.common.excel_pay.xinlibo.aipark;


import com.alibaba.excel.EasyExcel;
import com.yyh.common.excel_pay.local.LocalPayListerner;
import com.yyh.common.excel_pay.local.Order;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AiparkOrderPayUtils {

    public static void readLocalPayAccountExcel(String localAccountPath) {
        try {
            String fileName = localAccountPath.trim().substring(localAccountPath.lastIndexOf("\\")+1);
            Integer terminalId = Integer.valueOf(fileName.substring(1,2));
//            if (terminalId == 1 || terminalId == 2) {
//                terminalId = 1;
//            }
            EasyExcel.read(new FileInputStream(localAccountPath), AiparkOrder.class, new AiparkOrderListerner(terminalId)).sheet(0).doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}